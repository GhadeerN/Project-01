package sa.edu.tuwaiq.project_01.views.main
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager

import com.google.android.material.tabs.TabLayout
import sa.edu.tuwaiq.project_01.R
import sa.edu.tuwaiq.project_01.views.adapters.TapLayout


class TabBarFragment : Fragment() {
        var myFragment: View? = null
        var viewPager: ViewPager? = null
        var tabLayout: TabLayout? = null
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // Inflate the layout for this fragment
            myFragment = inflater.inflate(R.layout.fragment_tab_bar, container, false)
            viewPager = myFragment!!.findViewById(R.id.viewPager)
            tabLayout = myFragment!!.findViewById(R.id.tabLayout)
            return myFragment
        }

        //Call onActivity Create method
        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)
            setUpViewPager(viewPager)
            tabLayout!!.setupWithViewPager(viewPager)
            tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {}
                override fun onTabUnselected(tab: TabLayout.Tab) {}
                override fun onTabReselected(tab: TabLayout.Tab) {}
            })
        }

        private fun setUpViewPager(viewPager: ViewPager?) {
            val adapter = TapLayout(childFragmentManager)
            adapter.addFragment(TimeLineFragment(), "Post")
            adapter.addFragment(FavoriteFragment(), "myFavorite")

            viewPager!!.adapter = adapter
        }

        companion object {
            val instance: TabBarFragment
                get() = TabBarFragment()
        }
    }
