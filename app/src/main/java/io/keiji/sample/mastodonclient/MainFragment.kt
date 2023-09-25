package io.keiji.sample.mastodonclient

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import io.keiji.sample.mastodonclient.databinding.FragmentMainBinding

class MainFragment : Fragment (R.layout.fragment_main) {

    private var binding: FragmentMainBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = DataBindingUtil.bind(view)
        binding?.textview?.text = "Hello, Fragment"
    }

    override fun onDestroy() {
        super.onDestroy()

        binding?.unbind()
    }
}