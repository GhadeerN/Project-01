package sa.edu.tuwaiq.project_01.views.identity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import sa.edu.tuwaiq.project_01.R
import sa.edu.tuwaiq.project_01.databinding.FragmentLoginBinding
import sa.edu.tuwaiq.project_01.util.BottomAppBarHelper
class LoginFragment : Fragment() {
    lateinit var binding: FragmentLoginBinding
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


        // Hide the nav bar & the floating action bottom
        BottomAppBarHelper.get().hide()


        // TODO For test purposes ONLY
        // Login Button
        binding.loginButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_timeLineFragment)
        }
    }
}