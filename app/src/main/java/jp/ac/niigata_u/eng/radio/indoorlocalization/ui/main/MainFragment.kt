package jp.ac.niigata_u.eng.radio.indoorlocalization.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import jp.ac.niigata_u.eng.radio.indoorlocalization.R
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    return inflater.inflate(R.layout.fragment_main, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    connect_button.setOnClickListener {
      val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
      imm.hideSoftInputFromWindow(view.windowToken, 0)
      val ipAddress = ip.text.toString()
      val portNo = port.text.toString().toInt()
      findNavController().navigate(
        MainFragmentDirections.actionMainFragmentToResultFragment(
          ip = ipAddress,
          port = portNo
        )
      )
    }
  }
}
