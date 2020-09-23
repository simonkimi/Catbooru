package ink.z31.catbooru.ui.adapter

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import ink.z31.catbooru.R
import ink.z31.catbooru.data.model.base.BooruPost


class PreviewAdapter(data: MutableList<BooruPost>) :
    BaseQuickAdapter<BooruPost, BaseViewHolder>(R.layout.item_preview, data),
    LoadMoreModule {
    companion object {
        private const val TAG = "PreviewAdapter"
    }

    fun setData(newData: MutableList<BooruPost>) {
        this.data = newData
    }

    fun getBooruData(): MutableList<BooruPost> {
        return this.data
    }

    override fun convert(holder: BaseViewHolder, item: BooruPost) {
        val img = holder.getView<ImageView>(R.id.previewImg)
        val screenWidth = context.resources.displayMetrics.widthPixels

        val fitWidth = screenWidth / 3 - 10
        val fitHeight = fitWidth * item.previewHeight / item.previewWidth
        holder.setText(R.id.previewText, "#${item.id}")
        Glide.with(context)
            .load(item.previewURL)
            .override(fitWidth, fitHeight)
            .thumbnail(
                Glide.with(context).load(R.mipmap.loading).override(fitWidth, fitHeight)
                    .centerCrop()
            )
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(img)
    }
}

