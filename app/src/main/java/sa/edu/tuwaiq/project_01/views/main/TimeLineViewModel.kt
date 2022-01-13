package sa.edu.tuwaiq.project_01.views.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sa.edu.tuwaiq.project_01.Repo
import sa.edu.tuwaiq.project_01.model.Post
import java.lang.Exception

private const val TAG = "TimeLineViewModel"
class TimeLineViewModel(context: Application): AndroidViewModel(context)  {
    val repo : Repo = Repo(context)

    val callPostsLiveData = MutableLiveData<List<Post>>()
    val callPostsErrorLiveData = MutableLiveData<String>()
    val selectedPostLiveData = MutableLiveData<Post>()
    val postsList = mutableListOf<Post>()

    fun callPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repo.getPosts()

                for (document in response.documents) {
                    val post = document.toObject<Post>()

//                    if (item != null) {
//                        item.documentId = document.id
//                        Log.d(TAG, item.documentId)
//                    }
                    post?.let {
                        postsList.add(it)
                        Log.d(TAG, postsList.toString())
                    }
                }

                callPostsLiveData.postValue(postsList)

            } catch (e: Exception) {
                Log.d(TAG, "Error - catch: ${e.message}")
                callPostsErrorLiveData.postValue(e.message)
            }
        }
    }
}