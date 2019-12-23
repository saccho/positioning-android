package jp.ac.niigata_u.eng.radio.indoorlocalization.ui.send

import android.content.Context
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
import jp.ac.niigata_u.eng.radio.indoorlocalization.databinding.FragmentSendBinding

class SendFragment : Fragment() {

  private val args: SendFragmentArgs by navArgs()
  private lateinit var binding: FragmentSendBinding
  private lateinit var viewModel: SendViewModel

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

  private fun setSendMessageListener() {
    binding.sendButton.setOnClickListener {
      val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
      imm.hideSoftInputFromWindow(view?.windowToken, 0)
      val message = binding.message.text.toString()
      viewModel.write(message)
    }
  }
}
