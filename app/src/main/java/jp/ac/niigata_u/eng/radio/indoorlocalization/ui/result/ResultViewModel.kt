package jp.ac.niigata_u.eng.radio.indoorlocalization.ui.result

import android.os.Handler
import android.os.Message
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import jp.ac.niigata_u.eng.radio.indoorlocalization.data.api.SocketClient

class ResultViewModel(ip: String, port: Int) : ViewModel() {

  val readMessageLiveData: MutableLiveData<String> = MutableLiveData()
  private val socketClient: SocketClient

  init {
    val handler = object : Handler() {
      override fun handleMessage(msg: Message) {
        readMessageLiveData.postValue(msg.obj.toString())
      }
    }
    socketClient = SocketClient(ip, port, handler)
    Thread(socketClient).start()
  }

  fun closeSocketClient() {
    socketClient.close()
  }

  @Suppress("UNCHECKED_CAST")
  class Factory(private val ip: String, private val port: Int) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
      return ResultViewModel(ip, port) as T
    }
  }
}
