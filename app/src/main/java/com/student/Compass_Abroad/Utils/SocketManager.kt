package com.student.Compass_Abroad.Utils
import android.content.Context
import com.student.Compass_Abroad.R
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject
import java.net.URISyntaxException


object SocketManager {

    private var socket: Socket? = null
    private var isInitialized = false

    fun initSocket(context: Context) {
        if (isInitialized) return

        val options = IO.Options().apply {
            forceNew = true
            reconnection = true
            query = "token=Bearer ${CommonUtils.accessToken}"
        }

        try {
            socket = IO.socket(context.getString(R.string.socket_base_url), options)
            isInitialized = true
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    fun isConnected(): Boolean {
        return socket?.connected() == true
    }

    fun connect() {
        if (!isConnected()) {
            socket?.connect()
        }
    }

    fun disconnect() {
        socket?.disconnect()
    }

    fun on(event: String, listener: Emitter.Listener) {
        socket?.off(event) // Remove any previous listener for the same event
        socket?.on(event, listener)
    }

    fun emit(event: String, data: JSONObject) {
        socket?.emit(event, data)
    }

    fun off(event: String) {
        socket?.off(event)
    }
}

