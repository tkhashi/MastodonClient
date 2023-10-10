package io.keiji.sample.mastodonclient.ui.edit

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import io.keiji.sample.mastodonclient.BuildConfig
import io.keiji.sample.mastodonclient.R
import io.keiji.sample.mastodonclient.databinding.FragmentTootEditBinding

class TootEditFragment: Fragment(R.layout.fragment_toot_edit){
    companion object{
        fun newInstance(): TootEditFragment {
            return TootEditFragment()
        }

        val TAG = TootEditFragment::class.java.simpleName
    }

    private var binding: FragmentTootEditBinding? = null
    private val viewModel: TootEditViewModel by viewModels {
        TootEditViewModelFactory(
            BuildConfig.INSTANCE_URL,
            BuildConfig.USERNAME,
            lifecycleScope,
            requireContext()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bindingData: FragmentTootEditBinding? = DataBindingUtil.bind(view)
        binding = bindingData ?: return

        bindingData.lifecycleOwner = viewLifecycleOwner
        bindingData.viewModel = viewModel
    }

    override fun onDestroy() {
        super.onDestroy()

        binding?.unbind()
    }
}