package sa.edu.tuwaiq.project_01

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import sa.edu.tuwaiq.project_01.model.Post
import sa.edu.tuwaiq.project_01.model.Users

var fireStore : FirebaseFirestore = FirebaseFirestore.getInstance()
class Repo(val context: Context) {
    private val database = FirebaseFirestore.getInstance()
    private val imageRef = Firebase.storage.reference

    // Collections
    private val postCollection = database.collection("Posts")


    val myID = FirebaseAuth.getInstance().currentUser?.uid

    //logIn
    fun logInAuthentication(emailSignIn: String,passwordSignIn: String,view: View) {

        val email: String = emailSignIn.toString().trim { it <= ' ' }
        val password: String = passwordSignIn.toString().trim { it <= ' ' }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Navigation.findNavController(view)
                        .navigate(sa.edu.tuwaiq.project_01.R.id.action_loginFragment_to_timeLineFragment)
                } else {
                    Toast.makeText(
                        view.context,
                        task.exception!!.message.toString(),
                        Toast.LENGTH_LONG
                    ).show()

                }
            }
    }



    //class Firebase
    fun signUpAuthentication(email: String, password: String, userName: String, view: View) {

        //delete
        val email: String = email.toString().trim { it <= ' ' }
        val password: String = password.toString().trim { it <= ' ' }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    userInfo("${email}", "${userName}", view)

                } else {

                    Toast.makeText(context, task.exception!!.message.toString(), Toast.LENGTH_LONG)
                        .show()


                }
            }.addOnCompleteListener {}


    }


    //class
    fun userInfo(email: String, userName: String, view: View) {

        val userId = FirebaseAuth.getInstance().currentUser?.uid

        val user = Users()
        user.userID = userId.toString()
        user.userEmail = email
        user.userName = userName

        createUserFirestore(user, view)
    }


    //firebase class
    @SuppressLint("LongLogTag")
    fun createUserFirestore(user: Users, view: View) = CoroutineScope(Dispatchers.IO).launch {

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

    // Posts ---------------------------------------------------------------------------------------
    fun addPost(post: Post) = postCollection.document().set(post)

    fun uploadPostImage(image: Uri, fileName: String) =
        imageRef.child("imagesPosts/$fileName").putFile(image)

    suspend fun getPosts() = postCollection.get().await()

    //--Profile--
    fun getUserInfo(userID: String,userInfo :Users): LiveData<Users> {
        val user = MutableLiveData<Users>()
        fireStore.collection("Users").document("$userID")
            .get().addOnCompleteListener { it

                if (it.result?.exists()!!) {
                    userInfo.userName = it.result!!.getString("userName").toString()
                    userInfo.userEmail = it.result!!.getString("userEmail").toString()
                    userInfo.bio = it.result!!.getString("bio").toString()
                } else {
                }
                user.value= userInfo
            }
        return user
    }


    fun upDateUserInfo(editUserName: String, editUserBio: String) {
        val upDateUserData = Firebase.firestore.collection("Users")
        upDateUserData.document(myID.toString()).update(
            "userName", editUserName, "bio",editUserBio)

    }


    fun getUserPosts(userID: String, postList: MutableList<Post>): LiveData<MutableList<Post>> {
        val article = MutableLiveData<MutableList<Post>>()
        fireStore.collection("Posts").whereEqualTo("userId", userID)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.e("Firestore", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            postList.add(dc.document.toObject(Post::class.java))
                        }
                    }
                    article.value = postList
                }

            })
        return article
    }

    // Get User personal posts
    suspend fun getMyPosts() = postCollection.whereEqualTo("userId",myID).get().await()

//-----------------------------------------------------------------





}
