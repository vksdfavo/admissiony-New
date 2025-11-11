package com.student.Compass_Abroad.chatModal

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.student.Compass_Abroad.ConversationInfoWrapper
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.modal.errorHandle.ErrorHandler
import com.student.Compass_Abroad.modal.getChatResponse.Record
import com.student.Compass_Abroad.modal.getChatResponse.getChatResponse
import com.student.Compass_Abroad.retrofit.ApiInterface
import com.student.Compass_Abroad.retrofit.RetrofitClient
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SingleChatViewModel(app: Application) : BaseViewModel(app) {
    private var newMessageEvent: String = "newApplicationMessage"
    private var joinRoomEvent: String = "joinApplicationRoom"
    private var joinedRoomEvent: String = "joinedApplicationRoom"

    val newMessageLiveData: MutableLiveData<List<Record>> = MutableLiveData()
    internal var arrayList: ArrayList<Record> = ArrayList()
    var apiInterface = RetrofitClient.retrofitCallerObject!!.create(ApiInterface::class.java)

    init {
        setEventNames()
    }
    fun setEventNames() {
        when (App.singleton?.chatStatus) {
            "1" -> {
                newMessageEvent = "newLeadMessage"
                joinRoomEvent = "joinLeadRoom"
                joinedRoomEvent = "joinedLeadRoom"
            }
            else -> {
                newMessageEvent = "newApplicationMessage"
                joinRoomEvent = "joinApplicationRoom"
                joinedRoomEvent = "joinedApplicationRoom"
            }
        }
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
        joinChat(App.singleton?.applicationIdentifierChat ?: "")
    }

    override fun onDisconnected(any: Any) {
        Log.i(TAG, "Disconnected from socket: $any")

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
            Log.e("EngineIOException", "Socket error: $any")
        }
    }

    private fun parseRecord(data: String): Record {
        Log.d("parseRecord", "Parsing JSON data: $data")
        val gson = Gson()
        return try {
            val wrapper = gson.fromJson(data, ConversationInfoWrapper::class.java)
            wrapper.conversationInfo
        } catch (e: JsonSyntaxException) {
            Log.e("parseRecord", "Failed to parse JSON: $data", e)
            // Return a default or empty Record if parsing fails
            Record(
                null, null, null, null, null, null, null, null, null, null, null, null, null, null,null
            )
        }
    }

    fun joinChat(chatIdentifier: String) {
        Log.i(TAG, "Attempting to join chat with identifier: $chatIdentifier")
        socket.off(joinedRoomEvent)
        socket.off(newMessageEvent)
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

    var getChatMutableLiveData: MutableLiveData<getChatResponse?>? = null

    fun getChatResponseLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        chatIdentifier: String,
        entity: String,
        page: String,
        sort: String
    ): LiveData<getChatResponse?> {

        getChatMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            apiInterface.getChat(
                client_number,
                device_number,
                accessToken,
                chatIdentifier,
                entity,
                page, sort
            )!!.enqueue(object : Callback<getChatResponse?> {
                override fun onResponse(
                    call: Call<getChatResponse?>,
                    response: Response<getChatResponse?>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        getChatMutableLiveData!!.postValue(response.body())
                    } else {
                        val apiError = ErrorHandler.parseError(response)
                        handleError39(response.code(), ErrorHandler.getErrorMessage(apiError))
                    }
                }

                override fun onFailure(call: Call<getChatResponse?>, t: Throwable) {
                    handleError39(0, "Network error: " + t.message)
                }
            })
        } else {
            handleError39(0, "No internet connection.")
        }
        return getChatMutableLiveData!!
    }

    private fun handleError39(code: Int, backendMessage: String?) {
        val getChatResponse = getChatResponse()
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
}
