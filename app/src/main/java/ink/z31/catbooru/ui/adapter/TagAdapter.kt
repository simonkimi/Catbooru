package ink.z31.catbooru.ui.adapter

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import ink.z31.catbooru.R
import ink.z31.catbooru.data.model.base.BooruPost


class TagAdapter(data: MutableList<BooruPost>) :
    BaseQuickAdapter<BooruPost, BaseViewHolder>(R.layout.item_preview, data) {

    fun setData(newData: MutableList<BooruPost>) {
        this.data = newData
    }

    override fun convert(holder: BaseViewHolder, item: BooruPost) {
        val img = holder.getView<ImageView>(R.id.previewImg)
        val screenWidth = context.resources.displayMetrics.widthPixels
        val width = screenWidth / 3 - 10
        holder.setText(R.id.previewText, "#${item.id}")
        Glide.with(context).load(item.previewURL).override(width,SIZE_ORIGINAL).into(img)
    }

}

