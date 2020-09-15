package ink.z31.catbooru.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import ink.z31.catbooru.R
import ink.z31.catbooru.ui.viewModel.PostViewModel
import ink.z31.catbooru.util.AppUtil
import kotlinx.android.synthetic.main.fragment_post_preview.*


class PostPreviewFragment : Fragment() {
    private val postViewModel: PostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_post_preview, container, false)
        val screenWidth = AppUtil.context.resources.displayMetrics.widthPixels
        val fitHeight = screenWidth * postViewModel.booruPost.previewHeight / postViewModel.booruPost.previewWidth
        Glide.with(requireActivity())
            .load(postViewModel.booruPost.sampleURL)
            .override(screenWidth, fitHeight)
            .thumbnail(
                Glide.with(requireActivity())
                    .load(R.mipmap.loading)
                    .centerCrop()
            )
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(view.findViewById(R.id.postSampleImage))
        return view
    }
}