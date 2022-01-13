package sa.edu.tuwaiq.project_01.views.adapters

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
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
        TODO("bind view with data")
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class ViewHolder(val binding: PostItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(post: Post) {
                binding.itemName.text = post.
            }
    }
}