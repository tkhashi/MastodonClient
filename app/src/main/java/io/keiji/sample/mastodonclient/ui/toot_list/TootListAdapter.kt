package io.keiji.sample.mastodonclient.ui.toot_list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import io.keiji.sample.mastodonclient.R
import io.keiji.sample.mastodonclient.databinding.ListItemTootBinding
import io.keiji.sample.mastodonclient.entity.Toot

class TootListAdapter (
    private val layoutInflater: LayoutInflater,
    private val tootList: ArrayList<Toot>,
    private val callback: Callback?
) : RecyclerView.Adapter<TootListAdapter.ViewHolder>()
{
    interface Callback{
        fun openDetail(toot: Toot)
        fun delete(toot: Toot)
    }
    override fun getItemCount(): Int = tootList.size
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val  binding = DataBindingUtil.inflate<ListItemTootBinding>(
            layoutInflater,
            R.layout.list_item_toot,
            parent,
            false
        )

        return ViewHolder(binding, callback)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(tootList[position])
    }

    class ViewHolder(
        private val binding: ListItemTootBinding,
        private val callback: Callback?
    ) : RecyclerView.ViewHolder(binding.root)
    {
        fun bind(toot: Toot){
            binding.toot = toot
            binding.root.setOnClickListener{
                callback?.openDetail(toot)
            }
            binding.more.setOnClickListener{
                PopupMenu(itemView.context, it).also { popupMenu ->
                    popupMenu.menuInflater.inflate(
                        R.menu.list_item_toot,
                        popupMenu.menu)
                    popupMenu.setOnMenuItemClickListener { menuItem ->
                        when(menuItem.itemId){
                            R.id.menu_delete -> callback?.delete(toot)
                        }
                        return@setOnMenuItemClickListener true
                    }
                }.show()
            }
        }
    }
}