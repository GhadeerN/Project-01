package sa.edu.tuwaiq.project_01

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import sa.edu.tuwaiq.project_01.util.BottomAppBarHelper


class SplashScreen2Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash_screen2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        // Hide the nav bar & the floating action bottom
        BottomAppBarHelper.get().hide()


        Handler().postDelayed({
            findNavController().navigate(R.id.action_splashScreen2Fragment_to_loginFragment)


        }, 2000)

    }

}