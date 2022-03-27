package com.kincarta.app.presentation.views.CaseStudies

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.kincarta.app.R
import com.kincarta.app.databinding.ActivityStudiesCaseBinding
import com.kincarta.app.presentation.views.CaseStudiesDetailFragment
import com.kincarta.app.presentation.views.CaseStudiesPageAdapter
import com.kincarta.app.presentation.views.OnCaseStudyCallback
import com.kincarta.app.ui.views.CaseStudiesViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CaseStudiesActivity : AppCompatActivity(), OnCaseStudyCallback {
    private val viewModel: CaseStudiesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityStudiesCaseBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        init(binding)
    }

    private fun init(binding: ActivityStudiesCaseBinding) {

        binding.appbar.visibility = View.VISIBLE
        binding.caseStudiesContainer.visibility = View.GONE
        binding.caseStudiesViewModel = viewModel

        val viewPager = binding.tabViewpager
        val tabLayout = binding.tabTablayout

        val adapter = CaseStudiesPageAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = adapter

        TabLayoutMediator(
            tabLayout, viewPager
        ) { tab, position ->
            tab.text =
                when (position) {
                    0 -> "All"
                    1 -> "Favorite"
                    else -> "All"
                }
        }.attach()
    }

    override fun gotoDetailPageByCaseStudyId(imageView: ImageView, id: Long) {
        launchFragment(
            CaseStudiesDetailFragment.newInstance(id),
            CaseStudiesDetailFragment.FRAGMENT_NAME
        )
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val viewPager = ActivityStudiesCaseBinding.inflate(layoutInflater).tabViewpager
        if (viewPager.currentItem == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.

            findViewById<AppBarLayout>(R.id.appbar).visibility = View.VISIBLE
            init(ActivityStudiesCaseBinding.inflate(layoutInflater))

        } else {
            // Otherwise, select the previous step.
            viewPager.currentItem = viewPager.currentItem - 1
        }


    }

    private fun launchFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.case_studies_container,
                fragment,
                tag
            )
            .addToBackStack(null)
            .commitAllowingStateLoss()

        findViewById<AppBarLayout>(R.id.appbar).visibility = View.GONE
        findViewById<FragmentContainerView>(R.id.case_studies_container).visibility = View.VISIBLE
    }


    companion object {
        private const val KEY_CASE_STUDY_ID = "KEY_CASE_STUDY_ID"
    }
}