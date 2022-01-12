package sa.edu.tuwaiq.project_01.views.identity

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sa.edu.tuwaiq.project_01.Repo

class LoginViewModel(context:Application):AndroidViewModel(context) {

        val repo : Repo = Repo(context)

    // Login
    fun signIn(userEmail:String,userPasword:String,view: View){
        viewModelScope.launch (Dispatchers.IO){

            repo.logInAuthentication(userEmail,userPasword,view)

        }

    }
}