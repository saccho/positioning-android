package jp.ac.niigata_u.eng.radio.indoorlocalization.ui.result

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import jp.ac.niigata_u.eng.radio.indoorlocalization.data.api.SocketClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ResultViewModel(ip: String, port: Int) : ViewModel() {

  val readMessageLiveData: MutableLiveData<String> = MutableLiveData()
  private val socketClient = SocketClient(ip, port)

  init {
    read()
  }

  private fun read() = viewModelScope.launch {
    kotlin.runCatching {
      withContext(Dispatchers.Default) {
        socketClient.read()
      }
    }.onSuccess {
      readMessageLiveData.postValue(socketClient.receivedResult.data.toString())
    }
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
