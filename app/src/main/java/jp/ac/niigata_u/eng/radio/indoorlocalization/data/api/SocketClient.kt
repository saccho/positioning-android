package jp.ac.niigata_u.eng.radio.indoorlocalization.data.api

import android.util.Log
import jp.ac.niigata_u.eng.radio.indoorlocalization.data.NetworkState
import jp.ac.niigata_u.eng.radio.indoorlocalization.data.ReceivedResult
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.Socket

class SocketClient(private val ip: String, private val port: Int) {

  private lateinit var socket: Socket
  private lateinit var reader: BufferedReader
  lateinit var receivedResult: ReceivedResult

  private fun connect() {
    try {
      socket = Socket(ip, port)
      receivedResult = ReceivedResult(NetworkState.NO_DATA, mutableListOf())
      Log.d(TAG, "connected socket")
    } catch (e: Exception) {
      receivedResult = ReceivedResult(NetworkState.ERROR, mutableListOf())
      Log.d(TAG, "$e")
    }
  }

  fun read() {
    connect()
    receivedResult.state = NetworkState.LOADING
    reader = BufferedReader(InputStreamReader(socket.getInputStream()))
    Log.d(TAG, "created reader")

    // Socketのinputストリーム読み取り
    try {
      reader.use {
        while (true) {
          receivedResult.state = NetworkState.NO_DATA
          val message: String? = it.readLine()
          if (message != null) {
            Log.d(TAG, "$message (in while)")
            receivedResult.state = NetworkState.SUCCESS
            receivedResult.data.add(message)
          } else break
        }
      }
    } catch (e: Exception) {
      receivedResult.state = NetworkState.ERROR
      Log.d(TAG, "$e")
    }
  }

  fun close() {
    if (::reader.isInitialized) {
      reader.close()
      Log.d(TAG, "closed reader")
    }
    if (::socket.isInitialized) {
      socket.close()
      Log.d(TAG, "closed socket")
    }
  }

  companion object {
    const val TAG = "SocketClient"
  }
}
