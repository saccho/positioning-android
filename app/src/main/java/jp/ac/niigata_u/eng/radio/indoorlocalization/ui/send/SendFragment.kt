package jp.ac.niigata_u.eng.radio.indoorlocalization.ui.send

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import jp.ac.niigata_u.eng.radio.indoorlocalization.R
import jp.ac.niigata_u.eng.radio.indoorlocalization.data.local.Acceleration
import jp.ac.niigata_u.eng.radio.indoorlocalization.databinding.FragmentSendBinding

class SendFragment : Fragment(), SensorEventListener {

  private val args: SendFragmentArgs by navArgs()
  private lateinit var binding: FragmentSendBinding
  private lateinit var viewModel: SendViewModel
  private lateinit var sensorManager: SensorManager
  private lateinit var sensor: Sensor
  private val acceleration = Acceleration(0f, 0f, 0f)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    sensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
    sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_send, container, false)
    return binding.root
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    val factory = SendViewModel.Factory(
      args.ip, args.port
    )
    viewModel = ViewModelProviders.of(this, factory).get(SendViewModel::class.java)
    setSendMessageListener()
  }

  override fun onResume() {
    super.onResume()
    sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
  }

  override fun onPause() {
    super.onPause()
    sensorManager.unregisterListener(this)
  }

  private fun setSendMessageListener() {
    binding.sendButton.setOnClickListener {
      val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
      imm.hideSoftInputFromWindow(view?.windowToken, 0)
      val message = binding.message.text.toString()
      viewModel.write(message)
    }
  }

  override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
  }

  override fun onSensorChanged(event: SensorEvent?) {
    if (event != null && event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
      acceleration.x = event.values[0]
      acceleration.y = event.values[1]
      acceleration.z = event.values[2]
      binding.acceleration = acceleration
    }
  }

  companion object {
    const val TAG = "SendFragment"
  }
}
