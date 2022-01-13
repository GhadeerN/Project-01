package sa.edu.tuwaiq.project_01.views.identity

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sa.edu.tuwaiq.project_01.Repo
import sa.edu.tuwaiq.project_01.model.Post
import sa.edu.tuwaiq.project_01.model.Users


class ProfileViewModel(context:Application):AndroidViewModel(context) {

    val repo :Repo =Repo(context)



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




}