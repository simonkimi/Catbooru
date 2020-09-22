package ink.z31.catbooru.ui.fragment

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.*
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import ink.z31.catbooru.R
import ink.z31.catbooru.ui.viewModel.PostViewModel
import ink.z31.catbooru.util.BlurTransformation


class PostDetailFragment : Fragment() {
    companion object {
        private const val TAG = "PostDetailFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move)
    }

    private val postViewModel: PostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_post_detail, container, false)
        val imageView = view.findViewById<SubsamplingScaleImageView>(R.id.imageView)
        val progressBar = view.findViewById<ProgressBar>(R.id.postDetailLoading)
        val postDetailException = view.findViewById<TextView>(R.id.postDetailException)

        Glide.with(this)
            .load(postViewModel.booruPost.imgURL)
            .into(object : CustomViewTarget<SubsamplingScaleImageView, Drawable>(imageView) {
                override fun onLoadFailed(errorDrawable: Drawable?) {
                    imageView.setImage(ImageSource.resource(R.mipmap.die))
                    progressBar.visibility = View.GONE
                    postDetailException.text = errorDrawable.toString()
                }

                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    val bitmap: Bitmap = resource.toBitmap()
                    imageView.setImage(ImageSource.bitmap(bitmap))
                    progressBar.visibility = View.GONE
                }

                override fun onResourceCleared(placeholder: Drawable?) {

                }

            })
        return view
    }



}