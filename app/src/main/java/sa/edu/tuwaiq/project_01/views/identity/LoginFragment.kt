package sa.edu.tuwaiq.project_01.views.identity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import sa.edu.tuwaiq.project_01.R
import sa.edu.tuwaiq.project_01.databinding.FragmentLoginBinding
import sa.edu.tuwaiq.project_01.util.BottomAppBarHelper

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



        binding.loginButton.setOnClickListener {
            checkOfText()
        }

        // Hide the nav bar & the floating action bottom
        BottomAppBarHelper.get().hide()


        // TODO For test purposes ONLY
        // Login Button
        binding.loginButton.setOnClickListener {
            checkOfText()
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
        sharedPreferences = this.requireActivity().getSharedPreferences("preference", Context.MODE_PRIVATE)
        isRemembered = sharedPreferences.getBoolean("CHECKBOX", false)

        if (isRemembered) {
            findNavController().navigate(R.id.action_loginFragment_to_timeLineFragment)
        }    }
}