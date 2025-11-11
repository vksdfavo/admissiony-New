package com.student.Compass_Abroad.uniteli

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.chatModal.BaseViewModel
import com.student.Compass_Abroad.modal.ambassadorGetChat.AmbassadorGetChatData
import com.student.Compass_Abroad.modal.ambassadorGetChat.Record
import com.student.Compass_Abroad.modal.errorHandle.ErrorHandler
import com.student.Compass_Abroad.retrofit.ApiInterface
import com.student.Compass_Abroad.retrofit.RetrofitClient
import com.student.Compass_Abroad.uniteli.adapter.ConversationInfoAmbassadorWrapper
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AmbassadorChatViewModel(app: Application) : BaseViewModel(app) {
    private var newMessageEvent: String = "newAmbassadorMessage"
    private var joinRoomEvent: String = "joinRoom"
    private var joinedRoomEvent: String = "joinedAmbassadorRoom"
    val newMessageLiveData: MutableLiveData<List<Record>> = MutableLiveData()
    internal var arrayList: ArrayList<Record> = ArrayList()
    var apiInterface = RetrofitClient.retrofitCallerObject!!.create(ApiInterface::class.java)

    init {
        setEventNames()
    }
    fun setEventNames() {

        newMessageEvent = "newAmbassadorMessage"
        joinRoomEvent = "joinRoom"
        joinedRoomEvent = "joinedAmbassadorRoom"

        Log.i(TAG, "Event names set - newMessageEvent: $newMessageEvent, joinRoomEvent: $joinRoomEvent, joinedRoomEvent: $joinedRoomEvent")
    }

    fun connect() {
        setEventNames()
        if (!socket.connected()) {
            socket.connect()
            Log.i(TAG, "Connecting to socket...")
        } else {
            Log.i(TAG, "Socket already connected")
        }
    }

    override fun onConnected() {
        Log.i(TAG, "Connected to socket")
        joinChat(App.singleton?.relationIdentifier ?: "")
    }

    // Callback when socket is disconnected
    override fun onDisconnected(any: Any) {
        Log.i(TAG, "Disconnected from socket: $any")
        // Optionally, add logic to reconnect or handle disconnection
    }
    fun disconnect() {
        if (socket.connected()) {
            socket.off(joinedRoomEvent)
            socket.off(newMessageEvent)
            socket.disconnect()
            Log.i(TAG, "Disconnecting from socket...")
        } else {
            Log.i(TAG, "Socket already disconnected")
        }
    }

    // Callback for handling common messages
    override fun onCommonMessage(any: Any) {
        Log.i(TAG, "Common message: $any")
    }

    // Callback for handling general errors
    override fun onError(any: Any) {
        Log.i(TAG, "Error: $any")
    }

    // Callback for handling socket-specific errors
    override fun onSocketError(any: Any) {
        if (any is Throwable) {
            Log.e("EngineIOException", "Socket error: ${any.message}", any)
        } else {
            Log.e("EngineIOException", "Socket error: ${any.toString()}")
        }
    }

    private fun parseRecord(data: String): Record {
        Log.d("parseRecord", "Parsing JSON data: $data")
        val gson = Gson()
        return try {
            // Parse the JSON into the wrapper class
            val wrapper = gson.fromJson(data, ConversationInfoAmbassadorWrapper::class.java)
            // Return the Record object inside the wrapper
            wrapper.conversationInfo
        }finally {

        }
    }

    fun joinChat(chatIdentifier: String) {
        Log.i(TAG, "Attempting to join chat with identifier: $chatIdentifier")

        // Remove any previous listeners before adding new ones
        socket.off(joinedRoomEvent)
        socket.off(newMessageEvent)

        // Listen for the event that confirms joining the chat room
        socket.on(joinedRoomEvent) { args ->
            Log.i(TAG, "Joined chat room successfully: ${args[0]}")
        }

        // Listen for new messages in the chat room
        socket.on(newMessageEvent) { args ->
            val data = args[0].toString()
            Log.i(TAG, "New message received: $data")
            try {
                val newMessage = parseRecord(data)
                arrayList.add(newMessage)
                newMessageLiveData.postValue(arrayList) // Post the entire list
            } catch (e: Exception) {
                Log.e(TAG, "Error processing new message: ", e)
            }
        }

        try {
            val joinRoomObject = JSONObject().put("identifier", chatIdentifier)
            socket.emit(joinRoomEvent, joinRoomObject)
            Log.i(TAG, "Joining chat room with identifier: $chatIdentifier")
        } catch (e: JSONException) {
            Log.e(TAG, "Error joining chat: ", e)
        }
    }

    var getChatMutableLiveData: MutableLiveData<AmbassadorGetChatData?>? = null

    fun getChatResponseLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        chatIdentifier: String,
        page: String,
        sort: String
    ): LiveData<AmbassadorGetChatData?> {

        getChatMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            apiInterface.getAmbassadorChat(
                client_number,
                device_number,
                accessToken,
                chatIdentifier,
                page,
                sort
            )!!.enqueue(object : Callback<AmbassadorGetChatData?> {
                override fun onResponse(
                    call: Call<AmbassadorGetChatData?>,
                    response: Response<AmbassadorGetChatData?>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        getChatMutableLiveData!!.postValue(response.body())
                    } else {
                        val apiError = ErrorHandler.parseError(response)
                        handleError39(response.code(), ErrorHandler.getErrorMessage(apiError))
                    }
                }

                override fun onFailure(call: Call<AmbassadorGetChatData?>, t: Throwable) {
                    handleError39(0, "Network error: " + t.message)
                }
            })
        } else {
            handleError39(0, "No internet connection.")
        }
        return getChatMutableLiveData!!
    }

    private fun handleError39(code: Int, backendMessage: String?) {
        var getChatResponse = AmbassadorGetChatData()
        getChatResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> "Internal Server Error. Please try again later."
            else -> backendMessage ?: "Error $code"
        }
        getChatResponse.message = errorMessage
        Log.e("API Error", getChatResponse.message!!)
        getChatMutableLiveData!!.postValue(getChatResponse)
    }

    override fun onCleared() {
        super.onCleared()
        disconnect()
    }

    companion object {
        private const val TAG = "SingleChatViewModel"
    }

}
