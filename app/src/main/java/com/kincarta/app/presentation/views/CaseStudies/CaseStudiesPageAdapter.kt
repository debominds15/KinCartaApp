package com.kincarta.app.presentation.views

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class CaseStudiesPageAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    val mPageReferenceMap: HashMap<Int, Fragment> = HashMap()

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> {
                mPageReferenceMap[0] = CaseStudiesFragment()
                return CaseStudiesFragment.newInstance(0)
            }
            1 -> {
                mPageReferenceMap[1] = CaseStudiesFragment()
                return FavoriteCaseStudiesFragment.newInstance(0)
            }

        }
        return CaseStudiesFragment.newInstance(0)
    }

    fun getFragment(key: Int): Fragment? {
        return mPageReferenceMap[key]
    }

}