package io.keiji.sample.mastodonclient.ui.toot_list

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.keiji.sample.mastodonclient.BuildConfig
import io.keiji.sample.mastodonclient.R
import io.keiji.sample.mastodonclient.databinding.FragmentTootListBinding
import io.keiji.sample.mastodonclient.entity.Account
import io.keiji.sample.mastodonclient.entity.Toot
import io.keiji.sample.mastodonclient.ui.edit.TootEditActivity
import io.keiji.sample.mastodonclient.ui.toot_detail.TootDetailActivity
import io.keiji.sample.mastodonclient.ui.toot_detail.TootDetailFragment

class TootListFragment : Fragment(R.layout.fragment_toot_list)
    , TootListAdapter.Callback {
    companion object {
        val TAG = TootListFragment::class.java.simpleName
        private const val REQUEST_CODE_TOOT_EDIT = 0x01
        
        private const val BUNDLE_KEY_TIMELINE_TYPE_ORDINAL = "timeline_type_ordinal"
        
        @JvmStatic
        fun newInstance(timelineType: TimelineType): TootListFragment {
            val args = Bundle().apply {
                putInt(BUNDLE_KEY_TIMELINE_TYPE_ORDINAL, timelineType.ordinal)
            }

            return TootListFragment().apply {
                arguments = args
            }
        }
    }

    private var binding: FragmentTootListBinding? = null

    private lateinit var adapter: TootListAdapter
    private lateinit var layoutManager: LinearLayoutManager

    private var timelineType = TimelineType.PublicTimeline

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireArguments().also {
            val typeOrdinal = it.getInt(
                BUNDLE_KEY_TIMELINE_TYPE_ORDINAL,
                TimelineType.PublicTimeline.ordinal
            )
            timelineType = TimelineType.values()[typeOrdinal]
        }
    }

    private val viewModel: TootListViewModel by viewModels {
       TootListViewModelFactory(
           BuildConfig.INSTANCE_URL,
           BuildConfig.USERNAME,
           timelineType,
           lifecycleScope,
           requireContext()
       )
    }

    private val loadNextScrollListener = object : RecyclerView
        .OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val isLoadingSnapshot = viewModel.isLoading.value ?: return
                if (isLoadingSnapshot || !viewModel.hasNext) return

                val visibleItemCount = recyclerView.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager
                    .findFirstVisibleItemPosition()

                if ((totalItemCount - visibleItemCount) <= firstVisibleItemPosition){
                    viewLifecycleOwner.lifecycle.addObserver(viewModel)
                }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tootListSnapshot = viewModel.tootList.value ?: ArrayList<Toot>().also {
            viewModel.tootList.value = it
        }
        adapter = TootListAdapter(layoutInflater, tootListSnapshot, this)
        layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        val bindingData: FragmentTootListBinding? = DataBindingUtil.bind(view)
        binding = bindingData ?: return

        bindingData.recyclerView.also{
            it.layoutManager = layoutManager
            it.adapter = adapter
            it.addOnScrollListener(loadNextScrollListener)
        }
        bindingData.swipeRefreshLayout.setOnRefreshListener {
            viewModel.clear()
            viewModel.loadNext()
        }
        bindingData.fab.setOnClickListener{
            launchTootEditActivity()
        }

        viewModel.isLoading.observe(viewLifecycleOwner, Observer{
            binding?.swipeRefreshLayout?.isRefreshing = it
        })
        viewModel.accountInfo.observe(viewLifecycleOwner, Observer{
            showAccountInfo(it)
        })
        viewModel.tootList.observe(viewLifecycleOwner, Observer {
            adapter.notifyDataSetChanged()
        })

        viewLifecycleOwner.lifecycle.addObserver(viewModel)
    }

    private fun launchTootEditActivity() {
        val intent = TootEditActivity.newInstance(requireContext())
        startActivityForResult(intent, REQUEST_CODE_TOOT_EDIT)
    }

    private fun showAccountInfo(accountInfo: Account){
        val activity = requireActivity()
        if (activity is AppCompatActivity){
            activity.supportActionBar?.subtitle = accountInfo.username
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_TOOT_EDIT
            && resultCode == Activity.RESULT_OK){
            viewModel.clear()
            viewModel.loadNext()
        }
    }

    override fun openDetail(toot: Toot) {
        val intent = TootDetailActivity.newInstance(requireContext(), toot)
        startActivity(intent)
    }

    override fun delete(toot:Toot){
        viewModel.delete(toot)
    }
}