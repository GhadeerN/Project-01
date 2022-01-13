package sa.edu.tuwaiq.project_01.util

import android.app.Activity
import android.view.View
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import sa.edu.tuwaiq.project_01.R

/* This helper class is to manage the show/hide state for the bottom app bar inorder to reduce code redundancy */
class BottomAppBarHelper(private val activity: Activity) {
    companion object {
        var INSTANCE: BottomAppBarHelper? = null

        fun init(activity: Activity) {
            INSTANCE = BottomAppBarHelper(activity)
        }

        fun get(): BottomAppBarHelper {
            return INSTANCE ?: throw Exception("BottomAppBarHelper has not been initialized")
        }
    }

    // Functions to control the bottom app bar visibility
    // Login & register -> Hide, Others -> Show
    fun hide() {
        // Hide the nav bar & the floating action bottom
        activity.findViewById<BottomAppBar>(R.id.bottomAppBar)?.visibility =
            View.GONE
        activity.findViewById<FloatingActionButton>(R.id.addPost_floatingActionButton)?.visibility =
            View.GONE
    }

    fun show() {
        // Show the nav bar & the floating action bottom
        activity.findViewById<BottomAppBar>(R.id.bottomAppBar)?.visibility =
            View.VISIBLE
        activity.findViewById<FloatingActionButton>(R.id.addPost_floatingActionButton)?.visibility =
            View.VISIBLE
    }
}