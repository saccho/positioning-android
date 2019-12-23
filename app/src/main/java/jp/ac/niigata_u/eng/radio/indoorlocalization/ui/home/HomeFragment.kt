package jp.ac.niigata_u.eng.radio.indoorlocalization.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import jp.ac.niigata_u.eng.radio.indoorlocalization.R
import jp.ac.niigata_u.eng.radio.indoorlocalization.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

  lateinit var binding: FragmentHomeBinding
  lateinit var viewModel: HomeViewModel

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
    initView()
    return binding.root
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    val factory = HomeViewModel.Factory(
      requireContext()
    )
    viewModel = ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)
    setNetworkConfig()
  }

  private fun initView() {
    binding.connectButton.setOnClickListener {
      val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
      imm.hideSoftInputFromWindow(view?.windowToken, 0)
      val ip = binding.ip.text.toString()
      val port = binding.port.text.toString().toInt()
      viewModel.saveIP(ip)
      viewModel.savePort(port)
      findNavController().navigate(
        HomeFragmentDirections.actionHomeFragmentToSendFragment(
          ip = ip,
          port = port
        )
      )
    }
  }

  private fun setNetworkConfig() {
    val ip = viewModel.getIP()
    val port = viewModel.getPort()
    if (ip != "") {
      binding.ip.setText(ip, TextView.BufferType.NORMAL)
    }
    if (port != -1) {
      binding.port.setText(port.toString(), TextView.BufferType.NORMAL)
    }
  }

  companion object {
    const val TAG = "HomeFragment"
  }
}
