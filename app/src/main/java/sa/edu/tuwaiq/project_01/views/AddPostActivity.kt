package sa.edu.tuwaiq.project_01.views

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.internal.entity.CaptureStrategy
import sa.edu.tuwaiq.project_01.MainActivity
import sa.edu.tuwaiq.project_01.R
import sa.edu.tuwaiq.project_01.databinding.ActivityAddPostBinding
import sa.edu.tuwaiq.project_01.model.Post
import sa.edu.tuwaiq.project_01.util.Permissions
import sa.edu.tuwaiq.project_01.views.identity.FILE_NAME
import sa.edu.tuwaiq.project_01.views.main.IMAGE
import sa.edu.tuwaiq.project_01.views.main.NAME
import java.io.File

const val IMAGE_PICKER = 0

private const val TAG = "AddPostActivity"
class AddPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPostBinding
    private val postViewModel: AddPostViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences

    private var imageUri: Uri? = null
    lateinit var postContent: String
    private lateinit var progressDialog: ProgressDialog

    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = this.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Loading...")
        progressDialog.setCancelable(false)

        observer()

        // Hide the action bar
        supportActionBar?.hide()

        // Close post page
        binding.closePoatButton.setOnClickListener {
            finish()
        }

        // Post
        binding.postButton.setOnClickListener {
            postContent = binding.postContentEditText.text.toString().trim()
            val fileName = System.currentTimeMillis().toString()
            imageUri?.let {
                progressDialog.show()
                postViewModel.uploadPostImage(imageUri!!, fileName)
            }
            if (postContent.isNotBlank() && imageUri == null) {
                binding.addedPostImage.visibility = View.GONE
                progressDialog.show()
                postViewModel.addPost(
                    Post(
                        postContent,
                                "",
                        sharedPreferences.getString(NAME, "")!!,
                        sharedPreferences.getString(IMAGE, "")!!,
                        firebaseAuth.currentUser!!.uid,
                    )
                )
            }

            if (postContent.isBlank() && imageUri == null) {
                binding.addedPostImage.visibility = View.GONE
                MaterialAlertDialogBuilder(this)
//                    .setTitle(resources.getString(R.string.title))
                    .setMessage(resources.getString(R.string.supporting_text))
                    .setNeutralButton(resources.getString(R.string.ok)) { dialog, _ ->
                        // Respond to neutral button press
                        dialog.dismiss()
                    }.show()
            }
//            finish()
        }

        binding.addImageImageButton.setOnClickListener {
            // TODO NOT SURE!
            Permissions.checkPermission(this, this)
            showImagePicker()
        }
    }

    private fun showImagePicker() {
        Matisse.from(this)
            .choose(MimeType.ofImage(), false) // image or image and video or whatever
            .captureStrategy(CaptureStrategy(true, "sa.edu.tuwaiq.project_01"))
            .forResult(IMAGE_PICKER)
    }

    // Catch the image picker result to store it in the db
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_PICKER && resultCode == RESULT_OK) {
            // Get the image uri from the Matisse library - [0] because we will take the 1st img only
            imageUri = Matisse.obtainResult(data)[0]

            binding.addedPostImage.setImageURI(imageUri)
            binding.addedPostImage.visibility = View.VISIBLE

            // TODO maybe this code for camera
//            val imagePath = Matisse.obtainPathResult(data)[0] //[0] index 0 to take first index of the array of photo selected
//            val imageFile = File(imagePath)
//            val uri = Uri.fromFile(imageFile)
        }
    }

    fun observer() {
        postViewModel.uploadImageLiveData.observe(this, {
            it?.let {

                Log.d(TAG, "File name: $it")
                val imageUrl =
                    "https://firebasestorage.googleapis.com/v0/b/postly-c7ca0.appspot.com/o/imagesPosts%2F$it?alt=media&token=65c25c89-c84e-47af-a96e-7fea617515d0"
                postViewModel.uploadImageLiveData.postValue(null)

                postViewModel.addPost(
                    Post(
                        postContent,
                        imageUrl,
                        sharedPreferences.getString(NAME, "")!!,
                        sharedPreferences.getString(IMAGE, "")!!,
                        firebaseAuth.currentUser!!.uid,
                    )
                )
            }
        })

        postViewModel.postLiveData.observe(this, {
            it?.let {
                progressDialog.dismiss()
                postViewModel.postLiveData.postValue(null)
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                finish()
            }
        })

        postViewModel.postErrorLiveData.observe(this, {
            it?.let {
                progressDialog.dismiss()
                postViewModel.postErrorLiveData.postValue(null)
                Toast.makeText(
                    this,
                    "Sorry something went wrong, Please try again later",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}