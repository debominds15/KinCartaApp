package com.kincarta.app.presentation.views

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.chip.Chip
import com.kincarta.app.R
import com.kincarta.app.databinding.FragmentStudyCaseFavoriteBinding
import com.kincarta.app.domain.model.SortTypeEnum
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteCaseStudiesFragment : Fragment(), OnCaseStudiesAdapterListener {

    private lateinit var fragmentStudyCaseFavBinding: FragmentStudyCaseFavoriteBinding
    private var adapter: CaseStudyAdapter? = null
    private var mCallback: OnCaseStudyCallback? = null
    private val viewModel: CaseStudyDetailViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnCaseStudyCallback) {
            mCallback = context
        } else throw ClassCastException(context.toString() + "must implement OnCaseStudyCallback!")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = CaseStudyAdapter(this)
        arguments?.getLong(KEY_CASE_STUDY_ID)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentStudyCaseFavBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_study_case_favorite,
                container,
                false
            )
        fragmentStudyCaseFavBinding.caseStudyDetailViewModel = viewModel
        fragmentStudyCaseFavBinding.favCaseStudiesRecyclerView.adapter = adapter
        filterListener()

        viewModel.favCaseStudyData.observe(
            activity!!,
            { entities ->
                entities?.let {
                    adapter?.addData(it)
                    fragmentStudyCaseFavBinding.favsProgressBar.visibility = View.GONE

                    if (it.isNotEmpty()) {
                        fragmentStudyCaseFavBinding.favCaseStudiesRecyclerView.visibility =
                            View.VISIBLE
                        fragmentStudyCaseFavBinding.tvNoItems.visibility = View.GONE
                        fragmentStudyCaseFavBinding.mainLayout.visibility = View.VISIBLE
                    } else {
                        fragmentStudyCaseFavBinding.favCaseStudiesRecyclerView.visibility =
                            View.GONE
                        fragmentStudyCaseFavBinding.tvNoItems.visibility = View.VISIBLE
                        fragmentStudyCaseFavBinding.mainLayout.visibility = View.GONE
                    }
                }
            }
        )

        viewModel.filterByCategories.observe(
            viewLifecycleOwner,
            {
                addChipsDynamic(it)
                sortCaseStudies(it)
            }
        )

        return fragmentStudyCaseFavBinding.root
    }

    private fun sortCaseStudies(categories: List<String>) {
        fragmentStudyCaseFavBinding.btnClearFilters.setOnClickListener {
            fragmentStudyCaseFavBinding.chipCategoryGroup.clearCheck()
            fragmentStudyCaseFavBinding.chipSortGroup.clearCheck()
            viewModel.clearFilters()
        }

        fragmentStudyCaseFavBinding.chipSortGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == fragmentStudyCaseFavBinding.chipAlphabetically.id) {
                viewModel.filterBasedOnParameters(SortTypeEnum.ASCENDING, categories)
            } else {
                viewModel.filterBasedOnParameters(SortTypeEnum.DESCENDING, categories)
            }
        }
    }

    private fun addChipsDynamic(categories: List<String>) {
        fragmentStudyCaseFavBinding.chipCategoryGroup.removeAllViews()
        for (i in categories.indices) {
            val mChip: Chip =
                layoutInflater.inflate(R.layout.item_chip_category, null, false) as Chip
            mChip.text = categories[i]
            mChip.id = i + 1
            mChip.tag = i
            val paddingDp = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 10f,
                resources.displayMetrics
            ).toInt()
            mChip.setPadding(paddingDp, 0, paddingDp, 0)
            mChip.setOnCheckedChangeListener { compoundButton, b ->
                val tag = compoundButton.tag as Int
                viewModel.categoryFlags[tag] = b

                val checkSortType =
                    if (fragmentStudyCaseFavBinding.chipSortGroup.checkedChipId == fragmentStudyCaseFavBinding.chipAlphabetically.id) SortTypeEnum.ASCENDING
                    else if (fragmentStudyCaseFavBinding.chipSortGroup.checkedChipId == fragmentStudyCaseFavBinding.chipAlphabetically.id) SortTypeEnum.DESCENDING
                    else SortTypeEnum.NONE
                viewModel.filterBasedOnParameters(checkSortType, categories)
            }
            fragmentStudyCaseFavBinding.chipCategoryGroup.addView(mChip)
        }
    }

    private fun filterListener() {
        fragmentStudyCaseFavBinding.ivFilter.setOnClickListener {
            if (fragmentStudyCaseFavBinding.layoutFilters.visibility == View.GONE) {
                fragmentStudyCaseFavBinding.layoutFilters.visibility = View.VISIBLE
                fragmentStudyCaseFavBinding.ivFilter.setImageResource((R.drawable.ic_baseline_filter_alt_off_24))
            } else {
                fragmentStudyCaseFavBinding.layoutFilters.visibility = View.GONE
                fragmentStudyCaseFavBinding.ivFilter.setImageResource((R.drawable.ic_baseline_filter_alt_24))
            }
        }
    }

    override fun gotoDetailPage(imageView: ImageView, id: Long) {
        mCallback?.gotoDetailPageByCaseStudyId(imageView, id)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllFavoriteCaseStudies()
    }

    override fun onDetach() {
        super.onDetach()
        mCallback = null
        adapter = null
    }

    companion object {

        const val KEY_CASE_STUDY_ID = "KEY_CASE_STUDY_ID"
        val FRAGMENT_NAME: String = FavoriteCaseStudiesFragment::class.java.name

        @JvmStatic
        fun newInstance(id: Long) = FavoriteCaseStudiesFragment().apply {
            arguments = Bundle().apply {
                putLong(KEY_CASE_STUDY_ID, id)
            }
        }
    }
}