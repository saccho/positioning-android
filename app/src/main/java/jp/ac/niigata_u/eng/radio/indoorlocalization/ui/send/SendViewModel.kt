package jp.ac.niigata_u.eng.radio.indoorlocalization.ui.send

import android.hardware.Sensor
import android.hardware.SensorEvent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import jp.ac.niigata_u.eng.radio.indoorlocalization.data.local.Acceleration
import jp.ac.niigata_u.eng.radio.indoorlocalization.data.remote.SocketClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SendViewModel(ip: String, port: Int) : ViewModel() {

  private val socketClient = SocketClient(ip, port)
  private val acceleration = Acceleration(0f, 0f, 0f)

  override fun onCleared() {
    super.onCleared()
    closeSocketClient()
  }

  fun write(message: String) = viewModelScope.launch {
    withContext(Dispatchers.IO) {
      socketClient.write(message)
    }
  }

  fun getAcceleration(event: SensorEvent): Acceleration {
    if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
      acceleration.x = event.values[0]
      acceleration.y = event.values[1]
      acceleration.z = event.values[2]
    }

    return acceleration
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
  }
}
