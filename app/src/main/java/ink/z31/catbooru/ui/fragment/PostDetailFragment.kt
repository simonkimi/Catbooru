package ink.z31.catbooru.ui.fragment

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import ink.z31.catbooru.R
import ink.z31.catbooru.ui.viewModel.PostViewModel


class PostDetailFragment : Fragment() {
    companion object {
        private const val TAG = "PostDetailFragment"
    }

    private val postViewModel: PostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_post_detail, container, false)
        val imageViewLoading = view.findViewById<ImageView>(R.id.imageViewLoading)
        val imageView = view.findViewById<SubsamplingScaleImageView>(R.id.imageView)

        Glide.with(this)
            .load(R.mipmap.loading)
            .into(imageViewLoading)

        Glide.with(this)
            .load(postViewModel.booruPost.imgURL)
            .into(object : CustomViewTarget<SubsamplingScaleImageView, Drawable>(imageView) {
                override fun onLoadFailed(errorDrawable: Drawable?) {
                    Glide.with(this@PostDetailFragment)
                        .load(R.mipmap.die)
                        .into(imageViewLoading)
                }

                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    println("$TAG Finish")
                    imageViewLoading.visibility = View.GONE
                    imageView.visibility = View.VISIBLE
                    val bitmap: Bitmap = resource.toBitmap()
                    imageView.setImage(ImageSource.bitmap(bitmap))
                }

                override fun onResourceCleared(placeholder: Drawable?) {

                }

            })
        return view
    }

}