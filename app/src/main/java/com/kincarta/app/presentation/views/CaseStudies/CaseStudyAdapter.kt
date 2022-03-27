package com.kincarta.app.presentation.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.kincarta.app.R
import com.kincarta.app.databinding.HolderCaseStudyBinding
import com.kincarta.app.domain.model.CaseStudies
import com.kincarta.app.presentation.loadImage
import com.kincarta.app.presentation.views.CaseStudyAdapter.CaseStudyViewHolder

/**
 * [RecyclerView.Adapter] to adapt
 * [Case Studies] items into [RecyclerView] via [CaseStudyViewHolder]
 *
 *
 * Created by Debojyoti on 16/03/2022.
 */
internal class CaseStudyAdapter(val mListener: OnCaseStudiesAdapterListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val caseStudies: MutableList<CaseStudies> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val holderCaseStudyBinding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context), R.layout.holder_case_study, parent, false
        )
        return CaseStudyViewHolder(holderCaseStudyBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CaseStudyViewHolder).onBind(getItem(position))
    }

    private fun getItem(position: Int): CaseStudies {
        return caseStudies[position]
    }

    override fun getItemCount(): Int {
        return caseStudies.size
    }

    fun addData(list: List<CaseStudies>) {
        this.caseStudies.clear()
        this.caseStudies.addAll(list)
        notifyDataSetChanged()
    }

    inner class CaseStudyViewHolder(private val dataBinding: ViewDataBinding) :
        RecyclerView.ViewHolder(dataBinding.root) {

        fun onBind(case: CaseStudies) {
            val holderCaseStudyBinding = dataBinding as HolderCaseStudyBinding
            with(holderCaseStudyBinding) {
                caseStudyViewModel = CaseStudyViewModel(case)
                photoProgressBar.visibility = View.VISIBLE
                caseStudyImageView.loadImage(
                    caseStudyViewModel?.caseStudy?.heroImage ?: "",
                    holderCaseStudyBinding.photoProgressBar
                )
            }

            itemView.setOnClickListener {
                mListener.gotoDetailPage(
                    holderCaseStudyBinding.caseStudyImageView,
                    caseStudies[adapterPosition].id?.toLong() ?: 0
                )
            }
        }
    }
}