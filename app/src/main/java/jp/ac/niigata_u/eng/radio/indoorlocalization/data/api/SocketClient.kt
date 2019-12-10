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
  val receivedResult = ReceivedResult(NetworkState.NO_DATA, mutableListOf())

  fun connect() {
    try {
      socket = Socket(ip, port)
      Log.d(TAG, "connected socket")
    } catch (e: Exception) {
      receivedResult.state = NetworkState.ERROR
      Log.d(TAG, "$e")
    }
  }

  fun read() {
    reader = BufferedReader(InputStreamReader(socket.getInputStream()))
    Log.d(TAG, "created reader")

    // Socketのinputストリーム読み取り
    try {
      reader.use {
        while (true) {
          receivedResult.state = NetworkState.LOADING
          val message: String? = it.readLine()
          if (message != null) {
            Log.d(TAG, message)
            receivedResult.state = NetworkState.SUCCESS
            receivedResult.data.add(message)
          } else {
            receivedResult.state = NetworkState.CLOSED
            break
          }
          Thread.sleep(SLEEP_TIME)
        }
      }
    } catch (e: Exception) {
      receivedResult.state = NetworkState.ERROR
      Log.d(TAG, "$e")
    }
  }

  fun close() {
    receivedResult.state = NetworkState.CLOSED
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
    const val SLEEP_TIME = 500L
  }
}
