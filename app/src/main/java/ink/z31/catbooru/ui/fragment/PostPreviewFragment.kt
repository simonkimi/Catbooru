package ink.z31.catbooru.ui.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import ink.z31.catbooru.R
import ink.z31.catbooru.ui.interfaces.IOpenPostDetail
import ink.z31.catbooru.ui.viewModel.PostViewModel
import ink.z31.catbooru.util.AppUtil
import ink.z31.catbooru.util.BlurTransformation


class PostPreviewFragment : Fragment() {
    private val postViewModel: PostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_post_preview, container, false)
        val screenWidth = AppUtil.context.resources.displayMetrics.widthPixels
        val fitHeight =
            screenWidth * postViewModel.booruPost.previewHeight / postViewModel.booruPost.previewWidth
        val postSampleImage = view.findViewById<ImageView>(R.id.postSampleImage)
        val postSampleLoader = view.findViewById<ProgressBar>(R.id.postSampleLoading)

        Glide.with(requireActivity())
            .load(postViewModel.booruPost.sampleURL)
            .override(screenWidth, fitHeight)
            .thumbnail(
                Glide.with(requireActivity())
                    .load(postViewModel.booruPost.previewURL)
                    .thumbnail(
                        Glide.with(requireActivity())
                            .load(postViewModel.booruPost.previewURL)
                    )
                    .override(screenWidth, fitHeight)
                    .apply(RequestOptions.bitmapTransform(BlurTransformation(20, 1)))
            )
            .transition(DrawableTransitionOptions.withCrossFade())
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    postSampleLoader.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    postSampleLoader.visibility = View.GONE
                    return false
                }

            })
            .into(postSampleImage)
        postSampleImage.setOnClickListener {
            (activity as IOpenPostDetail).openPostDetail()
        }
        return view
    }
}