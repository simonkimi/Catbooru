package ink.z31.catbooru.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.chad.library.adapter.base.module.DraggableModule
import ink.z31.catbooru.R
import ink.z31.catbooru.data.database.Booru
import ink.z31.catbooru.data.database.BooruType


class BooruItemAdapter(data: MutableList<Booru>) :
    BaseQuickAdapter<Booru, BaseViewHolder>(R.layout.item_booru_manager, data),
    DraggableModule {
    override fun convert(holder: BaseViewHolder, item: Booru) {
        holder.setText(R.id.booru_item_title, item.name)
        holder.setText(R.id.booru_item_host, item.host)
        holder.setText(
            R.id.booru_item_type,
            BooruType.get(item.type)?.getBooruString() ?: "unknown"
        )
    }

    fun setData(data: MutableList<Booru>) {
        this.data = data
    }

}