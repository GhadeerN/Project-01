package sa.edu.tuwaiq.project_01.views.identity

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import sa.edu.tuwaiq.project_01.R
import sa.edu.tuwaiq.project_01.databinding.FragmentLoginBinding
import sa.edu.tuwaiq.project_01.util.BottomAppBarHelper

const val FILE_NAME = "preference"
class LoginFragment : Fragment() {
    lateinit var binding: FragmentLoginBinding
    val  loginViewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    private lateinit var sharedPreferences: SharedPreferences
    var isRemembered = false
    private lateinit var rememberMe: CheckBox

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Go to signup
        binding.signupTextView.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }

        rememberMe = view.findViewById(R.id.rememberMe)

        //Remembered
        remembered()
        //-------------------------------------------------------------------------------------------------------------


        binding.forgetPasswordTextView.setOnClickListener {
            forgotPasswordDialog()
        }

        binding.loginButton.setOnClickListener {
            checkOfText()
        }

        // Hide the nav bar & the floating action bottom
        BottomAppBarHelper.get().hide()


        // TODO For test purposes ONLY
        // Login Button
//        binding.loginButton.setOnClickListener {
//            checkOfText()
//        }
    }




    private fun forgotPasswordDialog(){

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Forgot Password")
        val view: View = layoutInflater.inflate(R.layout.dialog_forgot_password, null)
        val userEmail: EditText = view.findViewById(R.id.etForgotPassword)
        builder.setView(view)
        builder.setPositiveButton("Rest") { _, _ ->
            forgotPassword(userEmail)
        }
        builder.setNegativeButton("Close", { _, _ -> })
        builder.show()
    }


    private fun forgotPassword(userEmail: EditText) {

        val firebseAuth = FirebaseAuth.getInstance()
        if (userEmail.text.toString().isEmpty()){return}

        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail.text.toString()).matches()) {return}
        firebseAuth.sendPasswordResetEmail(userEmail.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Send password Reset Email", Toast.LENGTH_SHORT).show()
                }
            }


    }


    private fun checkOfText() {

        when {
            TextUtils.isEmpty(binding.loginEmail.text.toString().trim { it <= ' ' }) -> {
                binding.outlinedTextFieldEmailLogin.helperText = "Please Enter Your Email"
            }
            TextUtils.isEmpty(binding.loginPassword.text.toString().trim { it <= ' ' }) -> {
                binding.outlinedTextFieldPassLogin.helperText = "Please Enter Your Password"

            }
            else -> {
                view?.let {

                    loginViewModel.signIn(
                        binding.loginEmail.text.toString(),
                        binding.loginPassword.text.toString(), it
                    )

                    //RememberMe
                    rememberMe()
                }
            }


        }



    }

    private fun rememberMe(){
        val emailPreference: String = binding.loginEmail.text.toString()
        val passwordPreference: String = binding.loginPassword.text.toString()
        val checked: Boolean = rememberMe.isChecked
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("EMAIL", emailPreference)
        editor.putString("PASSWORD", passwordPreference)
        editor.putBoolean("CHECKBOX", checked)
        editor.apply()
    }

    fun remembered() {
        sharedPreferences = this.requireActivity().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        isRemembered = sharedPreferences.getBoolean("CHECKBOX", false)

        if (isRemembered) {
            findNavController().navigate(R.id.tabBarFragment)
        }
    }
}