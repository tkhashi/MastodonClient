package io.keiji.sample.mastodonclient.ui.edit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import io.keiji.sample.mastodonclient.BuildConfig
import io.keiji.sample.mastodonclient.R
import io.keiji.sample.mastodonclient.databinding.FragmentTootEditBinding
import io.keiji.sample.mastodonclient.ui.login.LoginActivity

class TootEditFragment: Fragment(R.layout.fragment_toot_edit){
    companion object{
        fun newInstance(): TootEditFragment {
            return TootEditFragment()
        }

        val TAG = TootEditFragment::class.java.simpleName
        private const val REQUEST_CODE_LOGIN = 0x01
        private const val REQUEST_CHOOSE_MEDIA = 0x02
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

    interface Callback{
        fun onPostComplete()
    }

    private var callback: Callback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setHasOptionsMenu(true)

        if (context is Callback){
            callback = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bindingData: FragmentTootEditBinding? = DataBindingUtil.bind(view)
        binding = bindingData ?: return

        bindingData.addMedia.setOnClickListener{
            openMediaChooser()
        }
        bindingData.lifecycleOwner = viewLifecycleOwner
        bindingData.viewModel = viewModel

        viewModel.loginRequired.observe(viewLifecycleOwner, Observer {
            if (it){
                launchLoginActivity()
            }
        })

        viewModel.postComplete.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), "投稿完了しました", Toast.LENGTH_LONG)
                .show()
            callback?.onPostComplete()
        })
        viewModel.errorMessasge.observe(viewLifecycleOwner, Observer {
            Snackbar.make(view, it, Snackbar.LENGTH_LONG)
                .show()
        })
    }

    private fun openMediaChooser() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }
        startActivityForResult(intent, REQUEST_CHOOSE_MEDIA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val uri = data?.data

        if (requestCode == REQUEST_CHOOSE_MEDIA
            && resultCode == Activity.RESULT_OK
            &&uri != null){
            viewModel.addMedia(uri)
        }
        else{
        }
    }

    private fun launchLoginActivity() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_LOGIN)
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