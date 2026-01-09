package com.latihan.latihansplashscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DataContainerFragment : Fragment() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_data_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabLayout = view.findViewById(R.id.tab_layout)
        viewPager = view.findViewById(R.id.view_pager)

        // Setup ViewPager with fragments
        val fragments = listOf(
            DataFragment(),
            UsersFragment()
        )
        
        val titles = listOf(
            "Mahasiswa",
            "API Users"
        )

        val adapter = ViewPagerAdapter(requireActivity(), fragments, titles)
        viewPager.adapter = adapter

        // Link TabLayout with ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()
        
        // Animations
        val tvTitle = view.findViewById<View>(R.id.tv_title)
        val tvSubtitle = view.findViewById<View>(R.id.tv_subtitle)
        
        tvTitle.alpha = 0f
        tvTitle.translationY = -30f
        tvTitle.animate().alpha(1f).translationY(0f).setDuration(500).start()
        
        tvSubtitle.alpha = 0f
        tvSubtitle.animate().alpha(1f).setDuration(500).setStartDelay(100).start()
        
        tabLayout.alpha = 0f
        tabLayout.animate().alpha(1f).setDuration(500).setStartDelay(200).start()
    }
}
