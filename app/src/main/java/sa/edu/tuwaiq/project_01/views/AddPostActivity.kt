package sa.edu.tuwaiq.project_01.views

import android.content.Intent
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import sa.edu.tuwaiq.project_01.MainActivity
import sa.edu.tuwaiq.project_01.R
import sa.edu.tuwaiq.project_01.databinding.ActivityAddPostBinding

class AddPostActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddPostBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Hide the action bar
        supportActionBar?.hide()

        binding.button.setOnClickListener {
            finish()
        }
    }
}