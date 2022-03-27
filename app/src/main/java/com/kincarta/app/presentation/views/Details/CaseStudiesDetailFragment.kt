package com.kincarta.app.presentation.views

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.kincarta.app.R
import com.kincarta.app.data.source.local.entity.CaseStudyEntity
import com.kincarta.app.data.source.local.entity.FavoriteCaseStudyEntity
import com.kincarta.app.data.source.local.entity.SectionEntity
import com.kincarta.app.databinding.FragmentCaseStudyDetailBinding
import com.kincarta.app.presentation.loadImage
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CaseStudiesDetailFragment : Fragment(), View.OnClickListener {

    private lateinit var fragmentCaseStudyDetailBinding: FragmentCaseStudyDetailBinding
    private var mCallback: OnCaseStudyCallback? = null
    private val viewModel: CaseStudyDetailViewModel by viewModels()
    private lateinit var caseStudyEntity: CaseStudyEntity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnCaseStudyCallback) {
            mCallback = context
        } else throw ClassCastException(context.toString() + "must implement OnCaseStudyCallback!")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val caseStudyId = arguments?.getLong(KEY_CASE_STUDY_ID)
        viewModel.getCaseStudyDetail(caseStudyId ?: 0)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentCaseStudyDetailBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_case_study_detail, container, false)
        fragmentCaseStudyDetailBinding.caseStudiesDetailViewModel = viewModel



        fragmentCaseStudyDetailBinding.apply {
            backView.setOnClickListener(this@CaseStudiesDetailFragment)
            backViewImg.setOnClickListener(this@CaseStudiesDetailFragment)
            favView.setOnClickListener(this@CaseStudiesDetailFragment)
            favViewImg.setOnClickListener(this@CaseStudiesDetailFragment)

            detailAppBarLayout.addOnOffsetChangedListener(object :
                OnOffsetChangedListener {
                var isShow = true
                var scrollRange = -1
                override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                    if (scrollRange == -1) {
                        scrollRange = appBarLayout.totalScrollRange
                    }
                    if (scrollRange + verticalOffset == 0) {
                        layoutCollapseToolbar.visibility = View.VISIBLE
                        layoutToolbar.visibility = View.GONE
                        detailToolbarImageView.contentDescription =
                            getString(R.string.content_description_expanded_image)

                        isShow = true
                    } else if (isShow) {
                        isShow = false
                        layoutCollapseToolbar.visibility = View.GONE
                        layoutToolbar.visibility = View.VISIBLE
                        detailToolbarImageView.contentDescription =
                            getString(R.string.content_description_collapsed_image)

                    }
                }
            })
        }

        viewModel.caseStudyDetailData.observe(
            viewLifecycleOwner,
            {
                it?.let {
                    caseStudyEntity = it.first
                    fragmentCaseStudyDetailBinding.apply {
                        detailCollapseToolbar.title = it.first.title
                        tvToolbarTitle.text = it.first.title

                        detailToolbarImageView.loadImage(
                            it.first.heroImage ?: "",
                            fragmentCaseStudyDetailBinding.photoProgressBar
                        )

                        favViewImg.apply {
                            if (caseStudyEntity.isFab == 1) {
                                setImageResource(R.drawable.ic_baseline_favorite_red_24)
                            } else {
                                setImageResource(R.drawable.ic_baseline_favorite_border_24)
                            }
                        }

                        favView.apply {
                            if (caseStudyEntity.isFab == 1) {
                                setImageResource(R.drawable.ic_baseline_favorite_red_24)
                            } else {
                                setImageResource(R.drawable.ic_baseline_favorite_border_24)
                            }
                        }
                    }

                    updateDynamicViews(it)
                }
            }
        )



        return fragmentCaseStudyDetailBinding.root
    }

    private fun updateDynamicViews(data: Pair<CaseStudyEntity, List<SectionEntity>>) {
        fragmentCaseStudyDetailBinding.myComposable.setContent {
            MaterialTheme {
                Surface {
                    Column(
                        Modifier
                            .background(colorResource(R.color.colorPrimaryLight))
                            .fillMaxWidth()
                    ) {
                        data.second.forEach {
                            val body = it.bodyElements.replace("#", "\n\n")
                            val urls = it.bodyImageUrls.split("#").toList()

                            DynamicUI(body, it.title ?: "", urls)
                        }
                    }
                }
            }
        }
    }


    // below is the Composable function
    // which we have created for our TextView.
    @Composable
    fun DynamicUI(body: String, title: String, imageUrls: List<String>) {
        Card(
            shape = RoundedCornerShape(10.dp),
            backgroundColor = MaterialTheme.colors.surface,
            modifier = Modifier.padding(all = Dp(10.0F))
        ) {
            Column(
                // we are using column to align
                // our textview to center of the screen.
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),

                // below line is used for specifying
                // vertical arrangement.
                verticalArrangement = Arrangement.Center,

                // below line is used for specifying
                // horizontal arrangement.
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // TextView widget.

                Text(
                    text = title,
                    color = Color.Black,
                    style = MaterialTheme.typography.h5,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(all = Dp(20.0F))
                )

                Text(

                    // below line is for displaying
                    // text in our text view.
                    text = body,

                    // below line is used to add
                    // style to our text view.
                    style = TextStyle(
                        // color is used to provide green
                        // color to our text view.
                        color = Color.Black,

                        // font size to change the
                        // size of our text view.
                        fontSize = 14.sp,

                        // shadow to make our
                        // text view elevated.
                        shadow = Shadow(color = Color.Gray),

                        // textALign to align our text view
                        // to center position.
                        textAlign = TextAlign.Left,
                    ),
                    // modifier is use to give
                    // padding to our text view.
                    modifier = Modifier.padding(all = Dp(20.0F))
                )

                imageUrls.forEach {
                    Image(
                        painter = rememberImagePainter(it,
                            builder = {
                                size(OriginalSize)
                            }
                        ),
                        contentDescription = "My content description",

                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxWidth()

                    )
                }
            }
        }
    }


    companion object {

        const val KEY_CASE_STUDY_ID = "KEY_CASE_STUDY_ID"
        val FRAGMENT_NAME: String = CaseStudiesDetailFragment::class.java.name

        @JvmStatic
        fun newInstance(id: Long) = CaseStudiesDetailFragment().apply {
            arguments = Bundle().apply {
                putLong(KEY_CASE_STUDY_ID, id)
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fav_view -> {
                favClickListener()
            }
            R.id.fav_view_img -> {
                favClickListener()
            }
            R.id.back_view -> {
                activity?.onBackPressed()
            }
            R.id.back_view_img -> {
                activity?.onBackPressed()
            }
        }
    }

    private fun favClickListener() {
        if (this::caseStudyEntity.isInitialized) {
            fragmentCaseStudyDetailBinding.apply {
                if (viewModel.caseStudyDetailData.value?.first?.isFab == 0) {
                    viewModel.caseStudyDetailData.value?.first?.isFab = 1
                    favViewImg.setImageResource(R.drawable.ic_baseline_favorite_red_24)
                    favView.setImageResource(R.drawable.ic_baseline_favorite_red_24)
                } else {
                    viewModel.caseStudyDetailData.value?.first?.isFab = 0
                    favViewImg.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                    favView.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                }
            }

            viewModel.markCaseStudyDetailFav(
                FavoriteCaseStudyEntity(
                    viewModel.caseStudyDetailData.value?.first?.id ?: 0,
                    viewModel.caseStudyDetailData.value?.first?.isFab ?: 0
                )
            )
        }
    }
}