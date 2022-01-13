package sa.edu.tuwaiq.project_01.views.identity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.UiModeManager.MODE_NIGHT_YES
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import sa.edu.tuwaiq.project_01.R
import sa.edu.tuwaiq.project_01.databinding.FragmentSettingsBinding
import java.util.*


class SettingsFragment : Fragment() {
    lateinit var binding: FragmentSettingsBinding

    private val sharedPreferences by lazy {
        this.requireActivity().getSharedPreferences("preference", Context.MODE_PRIVATE)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("WrongConstant")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        //---------------------Switch Dark Mode----------------------------------------------------
        binding.switchDarkMode.isChecked = sharedPreferences.getBoolean("MODE", false)
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
            } else {

                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            sharedPreferences.edit().putBoolean("MODE", isChecked).apply()
        }

        binding.cardUserProfile.setOnClickListener {
            findNavController().navigate(R.id.profileFragment)
        }



        binding.buttonLogOutXml.setOnClickListener {

            showDialogLogout()
        }

        binding.changeLanguage.setOnClickListener {
            bottomSheetChangeLanguage()
        }

        binding.tvChangePasswordXml.setOnClickListener {
            bottomSheetChangePassword()
        }


    }


    fun showDialogLogout() {
        val builder = android.app.AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.login))
        builder.setIcon(R.drawable.ic_baseline_logout_24)
        builder.setMessage(getString(R.string.areyousureyouwanttologout))

        builder.setPositiveButton(getString(R.string.login)) { _, _ ->
            signOut()
        }
        builder.setNegativeButton(getString(R.string.cancel), { _, _ -> })

        builder.show()
    }


    //------------------------Bottom Sheet Change Language------------------------------------------
    private fun bottomSheetChangeLanguage() {
        val view: View = layoutInflater.inflate(R.layout.change_langauge, null)
        val builder = BottomSheetDialog(requireView().context!!)
        builder.setTitle("change_language")
        val btnChangeLanguage = view.findViewById<Button>(R.id.btnChangeLanguage)
        var radioGroup = view.findViewById<RadioGroup>(R.id.radioGroup)

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            var selectedLanguage: RadioButton = view.findViewById(checkedId)

            btnChangeLanguage.setOnClickListener {
                if (selectedLanguage.text.toString() == "عربي") {
                    setLocale("ar")
                } else if (selectedLanguage.text.toString() == "English") {
                    setLocale("en")
                }
            }
        }
        builder.setContentView(view)
        builder.show()
    }

    private fun setLocale(localeName: String) {
        val locale = Locale(localeName)
        Locale.setDefault(locale)
        val resources = activity?.resources
        val config: Configuration = resources!!.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        ActivityCompat.recreate(context as Activity)

    }


    private fun signOut() {
        sharedPreferences.getString("EMAIL", "")
        sharedPreferences.getString("PASSWORD", "")
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

        findNavController().navigate(R.id.action_settingsFragment_to_loginFragment)
        FirebaseAuth.getInstance().signOut()
    }

//------------------------------------------------------------------------------

    fun bottomSheetChangePassword() {

        val view: View = layoutInflater.inflate(R.layout.change_password_bottom_sheet, null)

        val builder = BottomSheetDialog(requireView().context!!)
        val oldPassword = view.findViewById<TextInputEditText>(R.id.etOldPassword_xml)
        val etNewPassword = view.findViewById<TextInputEditText>(R.id.etNewPassword_xml)
        val confirmNewPassword = view.findViewById<TextInputEditText>(R.id.etConfirmNewPassword_xml)
        val btnChangePasswor = view.findViewById<Button>(R.id.buttonChangePassword_xml)

        builder.setContentView(view)

        btnChangePasswor.setOnClickListener {
            changePassword(
                "${oldPassword.text.toString()}",
                "${etNewPassword.text.toString()}",
                "${confirmNewPassword.text.toString()}"
            )

            builder.dismiss()

        }
        builder.show()

    }

    //------------------------------------------------------------------------------
    private fun changePassword(
        oldPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ) {
        if (oldPassword.isNotEmpty() &&
            newPassword.isNotEmpty() &&
            confirmNewPassword.isNotEmpty()
        ) {

            if (newPassword.equals(confirmNewPassword)) {

                val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
                val userEmail = FirebaseAuth.getInstance().currentUser!!.email
                if (user.toString() != null && userEmail.toString() != null) {

                    val credential: AuthCredential = EmailAuthProvider
                        .getCredential(user?.email.toString(), oldPassword)

                    user?.reauthenticate(credential)
                        ?.addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(context, "Auth Successful ", Toast.LENGTH_SHORT)
                                    .show()

                                user.updatePassword("${newPassword.toString()}")
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(
                                                context, "isSuccessful , Update password",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            // view?.etOldPassword_xml?.setText("")
                                            //  view?.etConfirmNewPassword_xml?.setText("")
                                            // view?.etNewPassword_xml?.setText("")
                                        }
                                    }
                            } else {

                                Toast.makeText(context, "Auth Failed ", Toast.LENGTH_SHORT).show()

                            }
                        }

                } else {
                    Toast.makeText(
                        context,
                        "كلمة المرور القديمة غير صحيحه ",
                        Toast.LENGTH_SHORT
                    ).show()

                }

            } else {
                Toast.makeText(
                    context,
                    "New Password is not equals Confirm New Password.",
                    Toast.LENGTH_SHORT
                ).show()
            }

        } else {
            Toast.makeText(context, "Please enter all the fields", Toast.LENGTH_SHORT).show()
        }

    }

    //--------------------------------------------------------------------------------------------


}