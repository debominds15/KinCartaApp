package com.kincarta.app.presentation.views

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.kincarta.app.R
import com.kincarta.app.data.repository.FakeCaseStudiesData
import com.kincarta.app.presentation.views.CaseStudies.CaseStudiesActivity
import com.kincarta.app.util.CountingIdlingResourceSingleton
import com.kincarta.app.util.TestUtils.atPosition
import org.hamcrest.Matchers.anyOf
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class MovieListFragmentTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(CaseStudiesActivity::class.java)

    companion object {
        const val LIST_ITEM_IN_TEST = 0
        val CASE_STUDY_IN_TEST = FakeCaseStudiesData.caseStudies[LIST_ITEM_IN_TEST]
    }


    @Before
    fun setup() {
        IdlingRegistry.getInstance()
            .register(CountingIdlingResourceSingleton.countingIdlingResource)
        waitUntilLoad()
    }

    @After
    fun release() {
        IdlingRegistry.getInstance()
            .unregister(CountingIdlingResourceSingleton.countingIdlingResource)
    }


    @Test
    fun test_isListFragmentVisible_onAppLaunch() {
        onView(withId(R.id.case_studies_recycler_view)).check(matches(isDisplayed()))
    }

    fun waitUntilLoad() {
        try {
            Thread.sleep(2000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    @Test
    fun test_selectListItem_isCaseDetailsFragmentVisible() {
        // Click list item #LIST_ITEM_IN_TEST
        // Kindly switch to the Developers options in Settings and turn all animations off (window animation scale, transition animation scale, animator duration scale)

        //registerIdlingResources(/*put your IdlingResource here*/);
        // val loaderIdlingResource = LoaderIdlingResource(activityRule.scenariogetActivity().getSupportLoaderManager())
        //IdlingRegistry.getInstance().register(loaderIdlingResource)


        onView(withId(R.id.case_studies_recycler_view))
            .perform(
                actionOnItemAtPosition<CaseStudyAdapter.CaseStudyViewHolder>(
                    LIST_ITEM_IN_TEST,
                    click()
                )
            )


        // Confirm nav to DetailFragment and display title
        onView(withId(R.id.my_composable)).check(matches(isDisplayed()))
    }

    @Test
    fun test_backNavigation_toCaseStudyListFragment() {
        // Click list item #LIST_ITEM_IN_TEST
        onView(withId(R.id.case_studies_recycler_view))
            .perform(
                actionOnItemAtPosition<CaseStudyAdapter.CaseStudyViewHolder>(
                    LIST_ITEM_IN_TEST,
                    click()
                )
            )

        // Confirm nav to DetailFragment and display title
        //onView(withId(R.id.movie_title)).check(matches(withText(MOVIE_IN_TEST.title)))
        onView(withId(R.id.my_composable)).check(matches(isDisplayed()))

        pressBack()

        // Confirm MovieListFragment in view
        onView(withId(R.id.case_studies_recycler_view)).check(matches(isDisplayed()))
    }

    @Test
    fun parallaxToolbarTest() {
        //Go to the screen to perform the swipe action
        // Click list item #LIST_ITEM_IN_TEST
        onView(withId(R.id.case_studies_recycler_view))
            .perform(
                actionOnItemAtPosition<CaseStudyAdapter.CaseStudyViewHolder>(
                    LIST_ITEM_IN_TEST,
                    click()
                )
            )

        //Verify that image is indeed collapsed
        onView(withId(R.id.detail_toolbar_image_view)).check(matches(withContentDescription(R.string.content_description_expanded_image)))

        //perform is sequential so click and swipeUp = drag up gesture
        onView(withId(R.id.detail_app_bar_layout)).perform(click(), swipeUp())

        //We can't be really sure if the swiping has finished by the time we come to this point on all devices (slow)
        //so either a collaped or collapsing state passes the test
        onView(withId(R.id.detail_toolbar_image_view)).check(
            matches(
                anyOf(
                    withContentDescription(R.string.content_description_collapsed_image)
                )
            )
        )

        onView(withId(R.id.tv_toolbar_title)).check(matches(withText(CASE_STUDY_IN_TEST.title)))

    }

    @Test
    fun test_navFavoritesFragment_validateFlow_withoutFavoriteData() {

        // Click list item #LIST_ITEM_IN_TEST
        onView(withId(R.id.case_studies_recycler_view))
            .perform(
                actionOnItemAtPosition<CaseStudyAdapter.CaseStudyViewHolder>(
                    LIST_ITEM_IN_TEST,
                    click()
                )
            )

        // Confirm nav to DetailFragment and display title
        onView(withId(R.id.fav_view)).check(matches(isDisplayed()))
        // Nav to DirectorsFragment
        //onView(withId(R.id.fav_view)).perform(click())

        pressBack()

        onView(withId(R.id.tab_viewpager)).check(matches(isDisplayed()))

        //Swipe right to switch to Favorites tab
        onView(withId(R.id.tab_viewpager)).perform(swipeLeft())

        var itemCount = 0
        activityRule.scenario.onActivity { activityScenarioRule ->
            val recyclerView =
                activityScenarioRule.findViewById<RecyclerView>(R.id.fav_case_studies_recycler_view)
            itemCount = recyclerView.adapter?.itemCount ?: 0
        }

        if (itemCount == 0) {
            onView(withId(R.id.tv_no_items)).check(matches(isDisplayed()))
            onView(withId(R.id.fav_case_studies_recycler_view)).check(matches(not(isDisplayed())))
        }
    }

    @Test
    fun test_navFavoritesFragment_validateFlowWithData() {
        // Click list item #LIST_ITEM_IN_TEST
        onView(withId(R.id.case_studies_recycler_view))
            .perform(
                actionOnItemAtPosition<CaseStudyAdapter.CaseStudyViewHolder>(
                    LIST_ITEM_IN_TEST,
                    click()
                )
            )

        // Confirm nav to DetailFragment and display title
        onView(withId(R.id.detail_collapse_toolbar)).check(matches(hasDescendant(withText("A World-First For Apple iPad"))))
        //onView(withId(R.id.movie_title)).check(matches(withText(MOVIE_IN_TEST.title)))

        onView(withId(R.id.fav_view)).check(matches(isDisplayed()))
        // Nav to DirectorsFragment
        onView(withId(R.id.fav_view)).perform(click())

        pressBack()

        onView(withId(R.id.tab_viewpager)).check(matches(isDisplayed()))

        //Swipe right to switch to Favorites tab
        onView(withId(R.id.tab_viewpager)).perform(swipeLeft())

        //onView(withId(R.id.tv_no_items)).check(matches(isDisplayed()))

        try {
            Thread.sleep(2000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        // Is favorite item visible in favorites section


        onView(withId(R.id.tv_no_items)).check(matches(not(isDisplayed())))

        onView(withId(R.id.fav_case_studies_recycler_view))
            .check(
                matches(
                    atPosition(
                        LIST_ITEM_IN_TEST,
                        hasDescendant(withText(CASE_STUDY_IN_TEST.title))
                    )
                )
            )

        onView(withId(R.id.fav_case_studies_recycler_view))
            .perform(
                actionOnItemAtPosition<CaseStudyAdapter.CaseStudyViewHolder>(
                    LIST_ITEM_IN_TEST,
                    click()
                )
            )
    }

    @Test
    fun test_navCaseStudiesFragment_listAlphabetically() {
        // Click list item #LIST_ITEM_IN_TEST
        onView(withId(R.id.case_studies_recycler_view))
            .check(matches(atPosition(0, withText("A World-First For Apple iPad"))));


        onView(withId(R.id.iv_filter)).perform(click())

        onView(withId(R.id.layout_filters)).check(matches(isDisplayed()))

        //Performing sorting the case studies alphabetically
        onView(withId(R.id.chip_alphabetically)).perform(click())
    }

    @Test
    fun test_navFavCaseStudiesFragment_listAlphabetically() {
        // Click list item #LIST_ITEM_IN_TEST
        onView(withId(R.id.case_studies_recycler_view))
            .perform(
                actionOnItemAtPosition<CaseStudyAdapter.CaseStudyViewHolder>(
                    LIST_ITEM_IN_TEST,
                    click()
                )
            )

        // Confirm nav to DetailFragment and display title
        onView(withId(R.id.detail_collapse_toolbar)).check(matches(hasDescendant(withText("A World-First For Apple iPad"))))

        onView(withId(R.id.fav_view)).check(matches(isDisplayed()))
        // Nav to DirectorsFragment
        onView(withId(R.id.fav_view)).perform(click())

        pressBack()

        onView(withId(R.id.tab_viewpager)).check(matches(isDisplayed()))

        //Swipe right to switch to Favorites tab
        onView(withId(R.id.tab_viewpager)).perform(swipeLeft())

        try {
            Thread.sleep(2000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        // Is favorite item visible in favorites section

        onView(withId(R.id.tv_no_items)).check(matches(not(isDisplayed())))


        onView(withId(R.id.iv_filter)).perform(click())

        onView(withId(R.id.layout_filters)).check(matches(isDisplayed()))

        //Performing sorting the favorite case studies alphabetically
        onView(withId(R.id.chip_alphabetically)).perform(click())
    }
}