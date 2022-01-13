package sa.edu.tuwaiq.project_01.views.main

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.firebase.auth.FirebaseAuth
import sa.edu.tuwaiq.project_01.databinding.FragmentTimeLineBinding
import sa.edu.tuwaiq.project_01.util.BottomAppBarHelper
import sa.edu.tuwaiq.project_01.views.adapters.TimeLineAdapter
import sa.edu.tuwaiq.project_01.views.identity.FILE_NAME
const val NAME = "name"
const val IMAGE = "profile_image"
private const val TAG = "TimeLineFragment"
class TimeLineFragment : Fragment() {

    lateinit var binding: FragmentTimeLineBinding
    private val timeLineViewModel: TimeLineViewModel by activityViewModels()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var prefEditor: SharedPreferences.Editor
    lateinit var postsAdapter: TimeLineAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences = requireActivity().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        prefEditor = sharedPreferences.edit()

        // Inflate the layout for this fragment
        binding = FragmentTimeLineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observer()

        timeLineViewModel.callPosts()

        postsAdapter = TimeLineAdapter(requireContext(), timeLineViewModel)
        binding.postsRecyclerView.adapter = postsAdapter

        // Show the nav bar & the floating action bottom
        BottomAppBarHelper.get().show()

        timeLineViewModel.getUserInfo(FirebaseAuth.getInstance().currentUser?.uid!!)
    }

    fun observer() {
        timeLineViewModel.userInfoLiveData.observe(viewLifecycleOwner, {
            Log.d(TAG, "user: $it")
            prefEditor.putString(it.userName, "")
            prefEditor.putString(it.userImage, "")
            prefEditor.commit()
        })

        timeLineViewModel.callPostsLiveData.observe(viewLifecycleOwner, {
            it?.let {
                //TODO Dialog
                postsAdapter.submitList(it)
                timeLineViewModel.callPostsLiveData.postValue(null)
            }
        })
    }
}