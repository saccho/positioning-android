package jp.ac.niigata_u.eng.radio.indoorlocalization.ui.result

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import jp.ac.niigata_u.eng.radio.indoorlocalization.data.NetworkState
import jp.ac.niigata_u.eng.radio.indoorlocalization.data.api.SocketClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ResultViewModel(ip: String, port: Int) : ViewModel() {

  val readMessageLiveData: MutableLiveData<String> = MutableLiveData()
  private val socketClient = SocketClient(ip, port)

  init {
    viewModelScope.launch {
      withContext(Dispatchers.IO) {
        socketClient.connect()
        read()
      }
    }
    observeReceivedResult()
  }

  override fun onCleared() {
    super.onCleared()
    closeSocketClient()
  }

  private fun read() = viewModelScope.launch {
    withContext(Dispatchers.IO) {
      socketClient.read()
    }
  }

  private fun observeReceivedResult() = viewModelScope.launch {
    withContext(Dispatchers.Default) {
      while (true) {
        Log.d(TAG, socketClient.receivedResult.state.toString())
        when (socketClient.receivedResult.state) {
          NetworkState.SUCCESS -> {
            readMessageLiveData.postValue(socketClient.receivedResult.data.toString())
          }
          NetworkState.NO_DATA -> {
            readMessageLiveData.postValue(socketClient.receivedResult.state.toString())
          }
          NetworkState.CLOSED -> return@withContext
          NetworkState.ERROR -> {
            readMessageLiveData.postValue(socketClient.receivedResult.state.toString())
            return@withContext
          }
          else -> {
          }
        }
        Thread.sleep(SLEEP_TIME)
      }
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

  companion object {
    const val TAG = "ResultViewModel"
    const val SLEEP_TIME = 500L
  }
}
