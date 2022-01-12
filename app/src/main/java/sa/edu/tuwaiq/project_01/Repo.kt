package sa.edu.tuwaiq.project_01

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sa.edu.tuwaiq.project_01.model.Users

var fireStore : FirebaseFirestore = FirebaseFirestore.getInstance()
class Repo(val context: Context) {






    //logIn
    fun logInAuthentication(emailSignIn: String,passwordSignIn: String,view: View) {

        val email: String = emailSignIn.toString().trim { it <= ' ' }
        val password: String = passwordSignIn.toString().trim { it <= ' ' }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Navigation.findNavController(view).navigate(sa.edu.tuwaiq.project_01.R.id.action_loginFragment_to_timeLineFragment)
                } else {
                    Toast.makeText(view.context, task.exception!!.message.toString(), Toast.LENGTH_LONG).show()

                }
            }
    }



    //class Firebase
    fun signUpAuthentication(email: String, password: String,userName: String,view: View) {

        //delete
        val email: String = email.toString().trim { it <= ' ' }
        val password: String = password.toString().trim { it <= ' ' }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    userInfo("${email}", "${userName}",view)

                } else {

                    Toast.makeText(context, task.exception!!.message.toString(), Toast.LENGTH_LONG)
                        .show()


                }
            }.addOnCompleteListener {}


    }


    //class
    fun userInfo(email: String, userName: String,view: View) {

        val userId = FirebaseAuth.getInstance().currentUser?.uid

        val user = Users()
        user.userID = userId.toString()
        user.userEmail = email
        user.userName = userName

        createUserFirestore(user,view)
    }


    //firebase class
    @SuppressLint("LongLogTag")
    fun createUserFirestore(user: Users,view: View) = CoroutineScope(Dispatchers.IO).launch {

        try {
            val userRef = Firebase.firestore.collection("Users")
            //-----------UID------------------------

            userRef.document("${user.userID}").set(user).addOnCompleteListener { it
                when {it.isSuccessful -> {

                    Navigation.findNavController(view).navigate(sa.edu.tuwaiq.project_01.R.id.action_signUpFragment_to_loginFragment)

                    Toast.makeText(context, "Welcome", Toast.LENGTH_LONG).show()
                }
                    else -> {
                        Toast.makeText(context, "is not Successful", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Log.e("FUNCTION createUserFirestore", "${e.message}")
            }
        }
    }
/*
    fun signUpAuthentication(emailSignIn: String,userName: String,view: View) {

        val email: String = emailSignIn.toString().trim { it <= ' ' }
        val password: String = userName.toString().trim { it <= ' ' }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Navigation.findNavController(view).navigate(sa.edu.tuwaiq.project_01.R.id.action_loginFragment_to_timeLineFragment)
                } else {
                    Toast.makeText(view.context, task.exception!!.message.toString(), Toast.LENGTH_LONG).show()

                }
            }
    }



 */
}
