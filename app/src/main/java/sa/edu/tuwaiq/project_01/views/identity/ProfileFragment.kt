package sa.edu.tuwaiq.project_01.views.identity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import sa.edu.tuwaiq.project_01.R
import sa.edu.tuwaiq.project_01.databinding.FragmentProfileBinding
import sa.edu.tuwaiq.project_01.model.Post
import sa.edu.tuwaiq.project_01.model.Users
import sa.edu.tuwaiq.project_01.util.BottomAppBarHelper
import sa.edu.tuwaiq.project_01.views.adapters.TimeLineAdapter
import sa.edu.tuwaiq.project_01.views.main.TimeLineViewModel
import sa.edu.tuwaiq.project_01.views.main.IMAGE
import java.io.File


class ProfileFragment : Fragment() {

    private lateinit var imageUrl: Uri

    private lateinit var articleList: MutableList<Post>
    private lateinit var articleAdapter: TimeLineAdapter
    private val timeLineViewModel: TimeLineViewModel by activityViewModels()
    val profileViewModel by lazy {
        ViewModelProvider(this).get(ProfileViewModel::class.java)
    }

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreEditor: SharedPreferences.Editor

    private lateinit var userInfo : Users

    lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences = requireActivity().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        sharedPreEditor = sharedPreferences.edit()

        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observer()

        val myID=FirebaseAuth.getInstance().currentUser?.uid

        //--------------User Photo-------------------------------------
        getUserPhoto()

        // Show the nav bar & the floating action bottom
        BottomAppBarHelper.get().show()

        // Get my posts
        profileViewModel.getMyPosts()

        articleAdapter = TimeLineAdapter(requireContext(), timeLineViewModel)
        binding.userRecyclerView.adapter = articleAdapter

        //----------------------getAllMyArticles-----------------------------------
//        profileViewModel.getAllMyArticles(myID.toString(), articleList, viewLifecycleOwner)
//            .observe(viewLifecycleOwner, {
//                articleAdapter = TimeLineAdapter(requireContext(), timeLineViewModel)
//                binding.userRecyclerView.adapter = articleAdapter
//
//                articleAdapter.submitList(it)
//
//               // binding.numberOrArticle.setText(articleList.size.toString())
//                articleAdapter.notifyDataSetChanged()
//            })





        //----------------------------getUserInformation-----------------------------------------------------------------
        userInfo= Users()
        profileViewModel.getUserInformation(myID.toString(),userInfo,viewLifecycleOwner).observe(viewLifecycleOwner,{
            binding.profileName.text = userInfo.userName
            binding.profileUserName.text= userInfo.userEmail.toString()
            binding.profileBio.text= userInfo.bio.toString()

        })


        binding.profileImage.setOnClickListener {
            selectImage()
        }



        binding.editProfileUser.setOnClickListener {

            profileViewModel.getUserInformation(myID.toString(),userInfo,viewLifecycleOwner).observe(viewLifecycleOwner,{
                upDateUserInfoBottomSheet()
            })
        }

    }

     fun upDateUserInfoBottomSheet() {
        val view: View = layoutInflater.inflate(R.layout.edit_user_information, null)
        val builder = BottomSheetDialog(requireView().context as Context)
        builder.setTitle("Up Date Information")

        val upDateInfoButton = view.findViewById<Button>(R.id.upDateInfoButton_xml)
        val userName = view.findViewById<TextInputEditText>(R.id.editTextTextUserName)
        val userBio = view.findViewById<TextInputEditText>(R.id.editTextBio)

        userName.setText(binding.profileName.text)
         userBio.setText(binding.profileBio.text)

        upDateInfoButton.setOnClickListener {
            if (userName.text.toString().isNotEmpty()
            ) {
                binding.profileUserName.setText(userName.text.toString())
                binding.profileBio.setText(userBio.text.toString())
                profileViewModel.upDateUserInformation(userName.text.toString(),userBio.text.toString())
                builder.dismiss()
            } else {
                Toast.makeText(context, "Please Enter Correct Information!!! ", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setContentView(view)
        builder.show()
    }

    //--------------------------------------------------------------------------------------
    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            imageUrl = data?.data!!

            binding.profileImage.setImageURI(imageUrl)

            upLoadImage()
        }

    }

    //repo or fire storage
    fun upLoadImage() {
        val myID=FirebaseAuth.getInstance().currentUser?.uid

        val storageReference = FirebaseStorage.getInstance().getReference("imagesUsers/${myID.toString()}")

        storageReference.putFile(imageUrl)
            .addOnSuccessListener {

                getUserPhoto()
            }.addOnFailureListener {
            }
    }

    fun getUserPhoto() {
        val imageName = "${FirebaseAuth.getInstance().currentUser?.uid}"
        val storageRef = FirebaseStorage.getInstance().reference
            .child("imagesUsers/$imageName")

        val localFile = File.createTempFile("tempImage", "jpg")
        storageRef.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)

            binding.profileImage.load(localFile)

            //TODO SHAared pref
            sharedPreEditor.putString(IMAGE, localFile.toString()).commit()

        }.addOnFailureListener {
        }
    }
    //--------------------------------------------------------------------------------------

    fun observer() {
        profileViewModel.myPostsLiveData.observe(viewLifecycleOwner, {
            it?.let {
                articleAdapter.submitList(it)
                profileViewModel.myPostsLiveData.postValue(null)
            }
        })
    }
}