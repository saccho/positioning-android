package jp.ac.niigata_u.eng.radio.indoorlocalization.ui.send

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import jp.ac.niigata_u.eng.radio.indoorlocalization.data.remote.SocketClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SendViewModel(ip: String, port: Int) : ViewModel() {

  private val socketClient = SocketClient(ip, port)

  override fun onCleared() {
    super.onCleared()
    closeSocketClient()
  }

  fun write(message: String) = viewModelScope.launch {
    withContext(Dispatchers.IO) {
      socketClient.write(message)
    }
  }

  private fun closeSocketClient() {
    socketClient.close()
  }

  @Suppress("UNCHECKED_CAST")
  class Factory(private val ip: String, private val port: Int) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
      return SendViewModel(ip, port) as T
    }
  }

  companion object {
    const val TAG = "SnedViewModel"
    const val SLEEP_TIME = 500L
  }
}
