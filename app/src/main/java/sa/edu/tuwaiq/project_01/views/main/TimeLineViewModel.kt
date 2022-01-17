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
import sa.edu.tuwaiq.project_01.model.Users
import java.lang.Exception

private const val TAG = "TimeLineViewModel"
class TimeLineViewModel(context: Application): AndroidViewModel(context)  {
    val repo : Repo = Repo(context)

    val callPostsLiveData = MutableLiveData<List<Post>>()
    val callPostsErrorLiveData = MutableLiveData<String>()
    val selectedPostLiveData = MutableLiveData<Post>()
    var postsList = mutableListOf<Post>()

    var userInfoLiveData = MutableLiveData<Users>()

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
                postsList = mutableListOf()

            } catch (e: Exception) {
                Log.d(TAG, "Error - catch: ${e.message}")
                callPostsErrorLiveData.postValue(e.message)
            }
        }
    }


  /*  fun callFavorite() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repo.getFavorite()

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
                postsList = mutableListOf()

            } catch (e: Exception) {
                Log.d(TAG, "Error - catch: ${e.message}")
                callPostsErrorLiveData.postValue(e.message)
            }
        }
    }
*/
    fun getUserInfo(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repo.getUserInfo(userId, Users())
                userInfoLiveData = response as MutableLiveData<Users>

            } catch (e: Exception) {
                callPostsErrorLiveData.postValue(e.message)
            }
        }
    }
}