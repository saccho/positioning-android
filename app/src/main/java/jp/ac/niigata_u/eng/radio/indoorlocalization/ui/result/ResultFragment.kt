package jp.ac.niigata_u.eng.radio.indoorlocalization.ui.result

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

class ResultFragment : Fragment() {

  private val args: ResultFragmentArgs by navArgs()
  private lateinit var binding: FragmentResultBinding
  private lateinit var viewModel: ResultViewModel

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

    val factory = ResultViewModel.Factory(
      args.ip, args.port
    )

    viewModel = ViewModelProviders.of(this, factory).get(ResultViewModel::class.java)

    observeViewModel(viewModel)

    binding.closeButton.setOnClickListener {
      findNavController().navigate(R.id.action_resultFragment_pop)
    }
  }

  override fun onPause() {
    super.onPause()
    viewModel.closeSocketClient()
  }

  private fun observeViewModel(viewModel: ResultViewModel) {
    viewModel.readMessageLiveData.observe(viewLifecycleOwner, Observer {
      if (it != null) {
        binding.readMessage = it
      }
    })
  }
}
