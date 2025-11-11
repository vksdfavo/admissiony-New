package com.student.Compass_Abroad.chatModal

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils.accessToken
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.Polling
import io.socket.engineio.client.transports.WebSocket
import java.net.URISyntaxException

abstract class BaseViewModel(app: Application) : AndroidViewModel(app) {
    val TAG = "BaseViewModel"

    protected val socket: Socket by lazy {

        initializeSocket()

    }

    private fun initializeSocket(): Socket {
        val options = IO.Options().apply {
            transports = arrayOf(WebSocket.NAME, Polling.NAME)
            extraHeaders = mapOf(
                "Authorization" to listOf("$accessToken")
            )
        }

    return try {
            IO.socket(AppConstants.SocketIO.BASE_URL, options).apply {
                on(Socket.EVENT_CONNECT) {
                    onConnected()
                }
                on(Socket.EVENT_DISCONNECT) { args ->
                    onDisconnected(args[0])
                }
                on(Socket.EVENT_CONNECT_ERROR) { args ->
                    onError(args[0])
                }
                on(io.socket.engineio.client.Socket.EVENT_MESSAGE) { args ->
                    onCommonMessage(args[0])
                }
                on(io.socket.engineio.client.Socket.EVENT_ERROR) { args ->
                    onSocketError(args[0])
                }
            }
        } catch (e: URISyntaxException) {
            Log.e(TAG, "Socket URI Syntax Error: ${e.message}", e)
            throw RuntimeException("Failed to initialize socket", e)
        }
    }

    abstract fun onConnected()
    abstract fun onDisconnected(any: Any)
    abstract fun onCommonMessage(any: Any)
    open fun onError(any: Any) {

        Log.e("EngineIOException", "Error: $any")
    }

    open fun onSocketError(any: Any) {
        if (any is Throwable) {
            Log.e("EngineIOException", "Socket error: ${any.message}", any)
        } else {
            Log.e("EngineIOException", "Socket error: $any")
        }
    }
}
