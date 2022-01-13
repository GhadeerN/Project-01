package sa.edu.tuwaiq.project_01.views.identity

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sa.edu.tuwaiq.project_01.Repo
import sa.edu.tuwaiq.project_01.model.Post
import sa.edu.tuwaiq.project_01.model.Users
import java.lang.Exception

private const val TAG = "ProfileViewModel"
class ProfileViewModel(context:Application):AndroidViewModel(context) {

    val repo :Repo =Repo(context)
    val myPostsLiveData = MutableLiveData<List<Post>>()


    fun getUserInformation(myID: String, userInfo: Users, viewLifecycleOwner: LifecycleOwner): LiveData<Users> {
        val user = MutableLiveData<Users>()
        repo.getUserInfo(myID,userInfo).observe(viewLifecycleOwner,{
            user.postValue(userInfo)
        })
        return user
    }




    fun upDateUserInformation(editUserName: String, editUserBio: String){
        viewModelScope.launch (Dispatchers.IO) {
            repo.upDateUserInfo(editUserName,editUserBio)
        }
    }


    fun getAllMyArticles(myID: String, articleList: MutableList<Post>, viewLifecycleOwner: LifecycleOwner): LiveData<MutableList<Post>> {
        // val userPosts = repo.getUserPosts(myID, articleList)
        val myArticles = MutableLiveData<MutableList<Post>>()
        repo.getUserPosts(myID, articleList).observe(viewLifecycleOwner, {
            myArticles.postValue(it)

        })
        return myArticles
    }


    // Retreve user posts
    fun getMyPosts() {
        val myPosts = mutableListOf<Post>()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repo.getMyPosts()

                for (document in response.documents) {
                    val post = document.toObject<Post>()

                    post?.let {
                        myPosts.add(it)
                    }
                }

                myPostsLiveData.postValue(myPosts)
                
            } catch (e: Exception) {
                Log.d(TAG, "Error - catch: ${e.message}")
            }
        }
    }

}