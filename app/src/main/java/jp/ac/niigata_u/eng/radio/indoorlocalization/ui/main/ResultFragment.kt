package jp.ac.niigata_u.eng.radio.indoorlocalization.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import jp.ac.niigata_u.eng.radio.indoorlocalization.R
import jp.ac.niigata_u.eng.radio.indoorlocalization.databinding.FragmentResultBinding
import jp.ac.niigata_u.eng.radio.indoorlocalization.di.ReadMessageViewModel
import kotlinx.android.synthetic.main.fragment_result.*

class ResultFragment : Fragment() {

  private val args: ResultFragmentArgs by navArgs()
  private lateinit var binding: FragmentResultBinding
  private lateinit var viewModel: ReadMessageViewModel

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_result, container, false)
    return binding.root
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)

    val factory = ReadMessageViewModel.Factory(
      args.ip, args.port
    )

    viewModel = ViewModelProviders.of(this, factory).get(ReadMessageViewModel::class.java)

    observeViewModel(viewModel)

    close_button.setOnClickListener {
      findNavController().navigate(R.id.action_resultFragment_pop)
    }
  }

  override fun onPause() {
    super.onPause()
    viewModel.closeSocketClient()
  }

  private fun observeViewModel(viewModel: ReadMessageViewModel) {
    viewModel.readMessageLiveData.observe(viewLifecycleOwner, Observer {
      if (it != null) {
        binding.readMessage = it
      }
    })
  }
}