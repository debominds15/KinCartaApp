package com.kincarta.app.presentation.views

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.chip.Chip
import com.kincarta.app.R
import com.kincarta.app.databinding.FragmentStudyCaseBinding
import com.kincarta.app.domain.model.SortTypeEnum
import com.kincarta.app.ui.views.CaseStudiesViewModel
import com.kincarta.app.util.AlertUtil
import com.kincarta.app.util.NetworkUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CaseStudiesFragment : Fragment(), OnCaseStudiesAdapterListener {

    private lateinit var fragmentStudyCaseBinding: FragmentStudyCaseBinding
    private var adapter: CaseStudyAdapter? = null
    private var mCallback: OnCaseStudyCallback? = null
    private val viewModel: CaseStudiesViewModel by activityViewModels()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnCaseStudyCallback) {
            mCallback = context
        } else throw ClassCastException(context.toString() + "must implement OnCaseStudyCallback!")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = CaseStudyAdapter(this)
        if (!NetworkUtil.isInternetAvailable(activity!!.baseContext)) {
            viewModel.getCachedCaseStudies()
            AlertUtil.createDialog(activity!!)
        } else {
            viewModel.loadCaseStudies()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentStudyCaseBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_study_case, container, false)
        fragmentStudyCaseBinding.caseStudiesViewModel = viewModel
        fragmentStudyCaseBinding.caseStudiesRecyclerView.adapter = adapter

        filterListener()
        viewModel.isLoad.observe(
            viewLifecycleOwner,
            {
                it?.let { visibility ->
                    fragmentStudyCaseBinding.photosProgressBar.visibility =
                        if (visibility) View.GONE else View.VISIBLE
                }
            }
        )

        viewModel.caseStudyListReceivedLiveData.observe(
            viewLifecycleOwner,
            {
                it?.let {
                    fragmentStudyCaseBinding.photosProgressBar.visibility = View.GONE
                    fragmentStudyCaseBinding.tvNoData.visibility =
                        if (it.caseStudies.isEmpty()) View.VISIBLE else View.GONE
                    fragmentStudyCaseBinding.caseScrollView.visibility =
                        if (it.caseStudies.isNotEmpty()) View.VISIBLE else View.GONE
                    fragmentStudyCaseBinding.swipeRefreshCases.isRefreshing =
                        false   // reset the SwipeRefreshLayout (stop the loading spinner)


                    it.caseStudies.let { it1 ->
                        adapter?.addData(
                            it1
                        )
                    }

                }
            }
        )

        viewModel.areAllDataSaved.observe(
            viewLifecycleOwner,
            {
                val message = if (it) getString(R.string.cached_successfully) else getString(R.string.cached_unsuccessful)
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()

            }
        )

        viewModel.filterByCategories.observe(
            viewLifecycleOwner,
            {
                addChipsDynamic(it)
                sortCaseStudies(it)
            }
        )

        return fragmentStudyCaseBinding.root
    }

    private fun filterListener() {

        fragmentStudyCaseBinding.swipeRefreshCases.setOnRefreshListener {
            if (NetworkUtil.isInternetAvailable(activity!!.baseContext)) {
                viewModel.loadCaseStudies()
            } else {
                AlertUtil.createDialog(activity!!)
                fragmentStudyCaseBinding.swipeRefreshCases.isRefreshing = false
            }
        }

        fragmentStudyCaseBinding.btnClearFilters.setOnClickListener {
            fragmentStudyCaseBinding.chipCategoryGroup.clearCheck()
            fragmentStudyCaseBinding.chipSortGroup.clearCheck()
            viewModel.clearFilters()
        }

        fragmentStudyCaseBinding.ivFilter.setOnClickListener {
            if (fragmentStudyCaseBinding.layoutFilters.visibility == View.GONE) {
                fragmentStudyCaseBinding.layoutFilters.visibility = View.VISIBLE
                fragmentStudyCaseBinding.ivFilter.setImageResource((R.drawable.ic_baseline_filter_alt_off_24))
            } else {
                fragmentStudyCaseBinding.layoutFilters.visibility = View.GONE
                fragmentStudyCaseBinding.ivFilter.setImageResource((R.drawable.ic_baseline_filter_alt_24))
            }
        }
    }

    private fun sortCaseStudies(categories: List<String>) {
        fragmentStudyCaseBinding.chipSortGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == fragmentStudyCaseBinding.chipAlphabetically.id) {
                viewModel.filterBasedOnParameters(SortTypeEnum.ASCENDING, categories)
            } else {
                viewModel.filterBasedOnParameters(SortTypeEnum.DESCENDING, categories)
            }
        }
    }

    override fun gotoDetailPage(imageView: ImageView, id: Long) {
        mCallback?.gotoDetailPageByCaseStudyId(imageView, id)
    }

    private fun addChipsDynamic(categories: List<String>) {
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
                    if (fragmentStudyCaseBinding.chipSortGroup.checkedChipId == fragmentStudyCaseBinding.chipAlphabetically.id) SortTypeEnum.ASCENDING
                    else if (fragmentStudyCaseBinding.chipSortGroup.checkedChipId == fragmentStudyCaseBinding.chipAlphabetically.id) SortTypeEnum.DESCENDING
                    else SortTypeEnum.NONE
                viewModel.filterBasedOnParameters(checkSortType, categories)
            }
            fragmentStudyCaseBinding.chipCategoryGroup.addView(mChip)
        }
    }

/*    fun syncApi(activity: Activity){

    }*/

    override fun onDetach() {
        super.onDetach()
        mCallback = null
        adapter = null
    }

    companion object {

        const val KEY_CASE_STUDY_ID = "KEY_CASE_STUDY_ID"
        val FRAGMENT_NAME: String = CaseStudiesFragment::class.java.name

        @JvmStatic
        fun newInstance(id: Long) = CaseStudiesFragment().apply {
            arguments = Bundle().apply {
                putLong(KEY_CASE_STUDY_ID, id)
            }
        }
    }
}