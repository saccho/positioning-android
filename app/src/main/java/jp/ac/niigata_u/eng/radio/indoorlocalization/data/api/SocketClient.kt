package jp.ac.niigata_u.eng.radio.indoorlocalization.data.api

import android.os.Handler
import android.os.Message
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.Socket


class SocketClient(
  private val ip: String,
  private val port: Int,
  private val handler: Handler
) :
  Runnable {

  private lateinit var socket: Socket
  private lateinit var reader: BufferedReader

  override fun run() {
    socket = Socket(ip, port)
    Log.d(TAG, "connected socket")
    reader = BufferedReader(InputStreamReader(socket.getInputStream()))
    Log.d(TAG, "created reader")

    // Socketのinputストリーム読み取り
    reader.use {
      while (it.readLine() != null) {
        val msg = Message()
        msg.what = READ
        msg.obj = it.readLine()
        // Mainスレッドに通知
        handler.sendMessage(msg)
      }
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
    const val READ = 0
  }
}
