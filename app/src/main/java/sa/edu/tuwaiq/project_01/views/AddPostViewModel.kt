package sa.edu.tuwaiq.project_01.views

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sa.edu.tuwaiq.project_01.Repo
import sa.edu.tuwaiq.project_01.model.Post
import java.lang.Exception

private const val TAG = "AddPostViewModel"
class AddPostViewModel(context: Application): AndroidViewModel(context) {
    val repo : Repo = Repo(context)

    // Live data
    var postLiveData = MutableLiveData<String>()
    var postErrorLiveData = MutableLiveData<String>()

    var uploadImageLiveData = MutableLiveData<String>()

    // Add Post functionality ----------------------------------------------------------------------
    fun addPost(post: Post) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repo.addPost(post)

                response.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Post success")
                        postLiveData.postValue("success")
                    } else {
                        Log.d(TAG, "Post was not successful - else")
                        postErrorLiveData.postValue(response.exception?.message)
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "Post was not successful - catch")
                postErrorLiveData.postValue(e.message)
            }
        }
    }

    // Upload image to the firestore ---------------------------------------------------------------
    fun uploadPostImage(imageUri: Uri, filename: String) {
        try {
            val response = repo.uploadPostImage(imageUri, filename)

            response.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "upload image success")
                    uploadImageLiveData.postValue(filename)
                } else {
                    Log.d(TAG, "upload image fail - else")
                    postErrorLiveData.postValue(response.exception?.message)
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "upload image fail - catch")
            postErrorLiveData.postValue(e.message)
        }
    }
}