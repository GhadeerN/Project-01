package sa.edu.tuwaiq.project_01.views.identity

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sa.edu.tuwaiq.project_01.Repo


class SignUpViewModel(context: Application): AndroidViewModel(context) {

    val repo : Repo = Repo(context)

    // signUp
    fun signUp(userEmail:String,password:String,userName:String,view: View){
        viewModelScope.launch (Dispatchers.IO){

            repo.signUpAuthentication(userEmail,password,userName,view)

        }

    }
}