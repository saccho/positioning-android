package jp.ac.niigata_u.eng.radio.indoorlocalization.data.remote

import android.util.Log
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.Socket

class SocketClient(private val ip: String, private val port: Int) {

  private lateinit var socket: Socket
  private lateinit var reader: BufferedReader
  val receivedResult = ReceivedResult(
    NetworkState.NO_DATA,
    mutableListOf()
  )
  private lateinit var writer: BufferedWriter
  val sendResult = SendResult(
    NetworkState.NO_DATA, ""
  )

  private fun connect() {
    try {
      socket = Socket(ip, port)
      Log.d(TAG, "connected socket")
    } catch (e: Exception) {
      receivedResult.state = NetworkState.ERROR
      Log.d(TAG, "$e")
    }
  }

  fun read() {
    connect()
    reader = BufferedReader(InputStreamReader(socket.getInputStream()))
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

  fun write(message: String) {
    connect()
    writer = BufferedWriter(OutputStreamWriter(socket.getOutputStream()))
    try {
      writer.use {
        it.write(message)
        Log.d(TAG, message)
      }
    } catch (e: Exception) {
      Log.d(TAG, "$e")
    }
  }

  fun close() {
    receivedResult.state = NetworkState.CLOSED
    if (::reader.isInitialized) {
      reader.close()
      Log.d(TAG, "closed reader")
    }
    sendResult.state = NetworkState.CLOSED
    if (::writer.isInitialized) {
      writer.close()
      Log.d(TAG, "closed writer")
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
