package io.keiji.sample.mastodonclient

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import io.keiji.sample.mastodonclient.databinding.FragmentMainBinding
import retrofit2.Retrofit

class MainFragment : Fragment (R.layout.fragment_main) {
    companion object {
        private val TAG = MainFragment::class.java.simpleName
        private const val API_BASE_URL = "https://androidbook2020.keiji.io"
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(API_BASE_URL)
        .build()
    private val api = retrofit.create(MastodonApi::class.java)

    private var binding: FragmentMainBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = DataBindingUtil.bind(view)
        binding?.button?.setOnClickListener {
            binding?.button?.text = "clicked"
            val response = api.fetchPublicTimeline()
                .execute().body()?.string().toString()
            Log.d(TAG, response)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        binding?.unbind()
    }
}