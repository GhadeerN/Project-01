package sa.edu.tuwaiq.project_01.views.identity

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore

import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


import sa.edu.tuwaiq.project_01.R
import sa.edu.tuwaiq.project_01.databinding.FragmentSignUpBinding
import sa.edu.tuwaiq.project_01.model.Users
import sa.edu.tuwaiq.project_01.util.BottomAppBarHelper

class SignUpFragment : Fragment() {
    lateinit var binding: FragmentSignUpBinding

    val  sigUpViewModel by lazy {
        ViewModelProvider(this).get(SignUpViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Go to login
        binding.loginTextView.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }



        binding.registerButton.setOnClickListener {
            registerUser()
        }

        // Hide the nav bar
        BottomAppBarHelper.get().hide()
    }


    private fun registerUser() {
        when {
            TextUtils.isEmpty(binding.registerFullName.text.toString().trim { it <= ' ' }) -> {
                binding.outlinedTextFieldName.helperText = "Please Enter Your Name "
            }

            TextUtils.isEmpty(binding.registerEmail.text.toString().trim { it <= ' ' }) -> {
                binding.outlinedTextFieldEmail.helperText = "Please Enter Your Email"

            }
            TextUtils.isEmpty(binding.registerPassword.text.toString().trim { it <= ' ' }) -> {
                binding.outlinedTextFieldPass.helperText = "Please Enter Your Password"

            }
            TextUtils.isEmpty(binding.registerConfirmPassword.text.toString().trim { it <= ' ' }) -> {
                binding.outlinedTextFieldRePass.helperText = "Please Enter Your Confirm Password"

            }
            else -> {
                if (binding.registerPassword.text.toString().equals(binding.registerConfirmPassword.text.toString())) {


                    view?.let {

                        sigUpViewModel.signUp(
                            binding.registerEmail.text.toString(),
                            binding.registerPassword.text.toString(),
                            binding.registerFullName.text.toString(), view!!
                        )
//                    registerUser(
//                        "${binding.registerEmail.text.toString()}",
//                        "${binding.registerPassword.text.toString()}"
//                    )
                    }

                } else {
                    binding.outlinedTextFieldRePass.helperText = "......"


                }
            }
        }

    }




//    //class Firebase
//    fun registerUser(email: String, password: String) {
//
//        //delete
//        val email: String = email.toString().trim { it <= ' ' }
//        val password: String = password.toString().trim { it <= ' ' }
//
//        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener { task ->
//
//                if (task.isSuccessful) {
//
//                   userInfo("${email.toString()}", "${binding.registerFullName.text.toString()}")
//
//                } else {
//
//                    Toast.makeText(context, task.exception!!.message.toString(), Toast.LENGTH_LONG)
//                        .show()
//
//
//                }
//            }.addOnCompleteListener {}
//
//
//    }
//
//
//    //class
//    fun userInfo(email: String, userName: String) {
//
//        val userId = FirebaseAuth.getInstance().currentUser?.uid
//
//        val user = Users()
//        user.userID = userId.toString()
//        user.userEmail = email
//        user.userName = userName
//
//        createUserFirestore(user)
//    }
//
//
//    //firebase class
//    @SuppressLint("LongLogTag")
//    fun createUserFirestore(user:Users) = CoroutineScope(Dispatchers.IO).launch {
//
//        try {
//            val userRef = Firebase.firestore.collection("Users")
//            //-----------UID------------------------
//
//            userRef.document("${user.userID}").set(user).addOnCompleteListener { it
//                when {it.isSuccessful -> {
//
//                    findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
//
//                    Toast.makeText(context, "Welcome", Toast.LENGTH_LONG).show()
//                    }
//                    else -> {
//                        Toast.makeText(context, "is not Successful", Toast.LENGTH_LONG)
//                            .show()
//                    }
//                }
//            }
//
//        } catch (e: Exception) {
//            withContext(Dispatchers.Main) {
//                Log.e("FUNCTION createUserFirestore", "${e.message}")
//            }
//        }
//    }
//

}