package ink.z31.catbooru.ui.fragment

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import ink.z31.catbooru.R
import ink.z31.catbooru.data.model.base.BooruPost
import ink.z31.catbooru.ui.viewModel.PostViewModel
import kotlinx.android.synthetic.main.fragment_post_detail.*


class PostDetailFragment : Fragment() {
    companion object {
        private const val TAG = "PostDetailFragment"
    }

    private val postViewModel: PostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Glide.with(this)
            .load(R.mipmap.loading)
            .into(this.imageViewLoading)

        Glide.with(this)
            .load(postViewModel.booruPost.imgURL)
            .into(object : CustomViewTarget<SubsamplingScaleImageView, Drawable>(imageView) {
                override fun onLoadFailed(errorDrawable: Drawable?) {
                    Glide.with(this@PostDetailFragment)
                        .load(R.mipmap.die)
                        .into(this@PostDetailFragment.imageViewLoading)
                }

                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    println("$TAG Finish")
                    this@PostDetailFragment.imageViewLoading.visibility = View.GONE
                    this@PostDetailFragment.imageView.visibility = View.VISIBLE
                    val bitmap: Bitmap = resource.toBitmap()
                    imageView.setImage(ImageSource.bitmap(bitmap))
                }

                override fun onResourceCleared(placeholder: Drawable?) {

                }

            })
        return inflater.inflate(R.layout.fragment_post_detail, container, false)
    }
}