package sa.edu.tuwaiq.project_01.views.adapters

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import sa.edu.tuwaiq.project_01.R
import sa.edu.tuwaiq.project_01.databinding.PostItemLayoutBinding
import sa.edu.tuwaiq.project_01.model.Post
import sa.edu.tuwaiq.project_01.views.main.TimeLineViewModel

private const val TAG = "TimeLineAdapter"

class TimeLineAdapter(val context: Context, viewModel: TimeLineViewModel) :
    RecyclerView.Adapter<TimeLineAdapter.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            Log.d(TAG, "areItemsTheSame")
            return oldItem.postImage == newItem.postImage
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    // Submit the posts to the Differ
    fun submitList(list: List<Post>) {
        differ.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLineAdapter.ViewHolder {
        return ViewHolder(
            PostItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)
//        holder.binding.itemPostImage.load()
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class ViewHolder(val binding: PostItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(post: Post) {
                val userImage = post.userImage
                val postImage = post.postImage

                binding.itemName.text = post.username
                binding.itemPostContent.text = post.postContent

                if (userImage.isNotBlank()) {
                    Glide.with(context).load(userImage)
                        .placeholder(R.drawable.ic_outline_account_circle_24)
                        .into(binding.itemUserImage)
                } else
                    binding.itemUserImage.setImageResource(R.drawable.ic_outline_account_circle_24)

                if (postImage.isNotBlank()) {
                    binding.itemPostImage.visibility = View.VISIBLE
                    Glide.with(context).load(postImage)
                        .placeholder(R.drawable.ic_outline_account_circle_24)
                        .into(binding.itemPostImage)

//                    binding.itemPostImage.

                    Log.d(TAG, "post image: $postImage")
                } else
                    binding.itemPostImage.visibility = View.GONE
            }
    }
}