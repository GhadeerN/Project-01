package sa.edu.tuwaiq.project_01.views.adapters

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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
        val firestore = FirebaseFirestore.getInstance()
        val myID = FirebaseAuth.getInstance().currentUser?.uid

        fun bind(post: Post) {
                val userImage = post.userImage
                val postImage = post.postImage

                binding.itemName.text = post.username
                binding.itemPostContent.text = post.postContent

                //favorite.. check if post favorite or not
                firestore.collection("Users").document(myID.toString()).collection("Favorite")
                    .document(post.postID.toString()).get()
                    .addOnCompleteListener {
                        if (it.result?.exists()!!) {
                            binding.favoriteImageView.setImageResource(R.drawable.ic_baseline_favorite_24)
                        } else {
                            binding.favoriteImageView.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                        }
                    }





            binding.favoriteImageView.setOnClickListener {
                upDateFavorite(post.postID,post)

            }





            if (userImage.isNotBlank()) {
                    Glide.with(context).load(userImage)
                        .placeholder(R.drawable.ic_outline_account_circle_24)
                        .into(binding.itemUserImage)
                } else
                    binding.itemUserImage.setImageResource(R.drawable.ic_outline_account_circle_24)

                if (postImage.isNotBlank()) {
                    binding.itemPostImage.visibility = View.VISIBLE
                    Glide.with(context).load(postImage)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .placeholder(R.drawable.ic_outline_account_circle_24)
                        .into(binding.itemPostImage)

//                    binding.itemPostImage.

                    Log.d(TAG, "post image: $postImage")
                } else
                    binding.itemPostImage.visibility = View.GONE
            }

        //---------upDateFavorite------------------------------------------------------------------------------------------
        fun upDateFavorite(articleID: String, article: Post) {
            firestore.collection("Users").document(myID.toString()).collection("Favorite")
                .document(articleID).get()
                .addOnCompleteListener {
                    if (it.result?.exists()!!) {

                        deleteFavorite("${articleID}")
                        binding.favoriteImageView.setImageResource(R.drawable.ic_baseline_favorite_border_24)

                    } else {

                        binding.favoriteImageView.setImageResource(R.drawable.ic_baseline_favorite_24)
                        addFavorite("${articleID}", article)

                    }
                }
        }

        //---------deleteFavorite------------------------------------------------------------------------------------------
        fun deleteFavorite(articleID: String) {
           /* val deleteFavoriteArticle = FirebaseFirestore.getInstance()
            deleteFavoriteArticle.collection("Articles").document(articleID)
                .collection("Favorite").document(myID.toString()).delete()
                .addOnCompleteListener {
                    when {
                        it.isSuccessful -> {
                            Log.e("Delete Article ", "Delete From Articles Favorite")
                        }
                    }
                }*/
            //-------------deleteFavoriteArticleUser----------------------------------------------------------
            val deleteFavoriteArticleUser = FirebaseFirestore.getInstance()
            deleteFavoriteArticleUser.collection("Users").document(myID.toString())
                .collection("Favorite").document("${articleID.toString()}").delete()
                .addOnCompleteListener {
                    when {
                        it.isSuccessful -> {
                            Log.e("Delete Article ", "Delete From User Favorite")
                        }
                    }
                }
        }

        //---------------addFavorite-------------------------------------------------------------------------------------------
        fun addFavorite(articleID: String, article: Post) {
            val addFavorite = hashMapOf(
                "articleID" to "${article.postID}",
                "userId" to "${article.userId}",
            )
            //---------------------------------------------------------------------------------
            val articleRef = Firebase.firestore.collection("Users")
            articleRef.document(myID.toString()).collection("Favorite")
                .document("${articleID.toString()}")
                .set(addFavorite).addOnCompleteListener {it
                    when {it.isSuccessful -> {
                        Log.d("Add Article", "Done to add User Favorite")
                    }
                        else -> {
                            Log.d("Error", "is not Successful fire store")
                        }
                    }

                    //---------------------------------------------------------------------------------
                  /*  val addToArticle = Firebase.firestore.collection("Articles")
                    addToArticle.document(articleID.toString()).collection("Favorite")
                        .document("${userId.toString()}").set(addFavorite)*/
                    //---------------------------------------------------------------------------------

                }
        }


    }
}