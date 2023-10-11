package io.keiji.sample.mastodonclient.ui.edit

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bindingData: FragmentTootEditBinding? = DataBindingUtil.bind(view)
        binding = bindingData ?: return

        bindingData.lifecycleOwner = viewLifecycleOwner
        bindingData.viewModel = viewModel

        viewModel.postComplete.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), "投稿完了しました", Toast.LENGTH_LONG)
                .show()
        })
        viewModel.errorMessasge.observe(viewLifecycleOwner, Observer {
            Snackbar.make(view, it, Snackbar.LENGTH_LONG)
                .show()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toot_edit, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menu_post -> {
                viewModel.postToot()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onDestroy() {
        super.onDestroy()

        binding?.unbind()
    }
}