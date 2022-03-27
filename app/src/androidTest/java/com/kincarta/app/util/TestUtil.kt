package com.kincarta.app.util

import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.util.HumanReadables
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import java.lang.IllegalStateException
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description


/**
 * Created by dannyroa on 5/9/15.
 */
object TestUtils {

    fun atPosition(position: Int, @NonNull itemMatcher: Matcher<View?>): Matcher<View?>? {
        checkNotNull(itemMatcher)
        return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has item at position $position: ")
                itemMatcher.describeTo(description)
            }

            override fun matchesSafely(view: RecyclerView): Boolean {
                val viewHolder = view.findViewHolderForAdapterPosition(position)
                    ?: // has no item on such position
                    return false
                return itemMatcher.matches(viewHolder.itemView)
            }
        }
    }
    /*fun <VH : RecyclerView.ViewHolder?> actionOnItemViewAtPosition(
        position: Int,
        @IdRes viewId: Int,
        viewAction: ViewAction
    ): ViewAction {
        return ActionOnItemViewAtPositionViewAction<VH?>(position, viewId, viewAction)
    }

    fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher {
        return RecyclerViewMatcher(recyclerViewId)
    }

    private class ActionOnItemViewAtPositionViewAction<VH : RecyclerView.ViewHolder?>(
        private val position: Int,
        @param:IdRes private val viewId: Int,
        private val viewAction: ViewAction
    ) : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return Matchers.allOf(
                arrayOf<Matcher<*>>(
                    ViewMatchers.isAssignableFrom(RecyclerView::class.java),
                    ViewMatchers.isDisplayed()
                )
            )
        }

        override fun getDescription(): String {
            return ("actionOnItemAtPosition performing ViewAction: "
                    + viewAction.description
                    + " on item at position: "
                    + position)
        }

        override fun perform(uiController: UiController, view: View) {
            val recyclerView = view as RecyclerView
            ScrollToPositionViewAction(position).perform(uiController, view)
            uiController.loopMainThreadUntilIdle()
            val targetView = recyclerView.getChildAt(position).findViewById<View>(
                viewId
            )
            if (targetView == null) {
                throw PerformException.Builder().withActionDescription(this.toString())
                    .withViewDescription(
                        HumanReadables.describe(view)
                    )
                    .withCause(
                        IllegalStateException(
                            "No view with id "
                                    + viewId
                                    + " found at position: "
                                    + position
                        )
                    )
                    .build()
            } else {
                viewAction.perform(uiController, targetView)
            }
        }
    }

    private class ScrollToPositionViewAction(private val position: Int) :
        ViewAction {
        override fun getConstraints(): Matcher<View> {
            return Matchers.allOf(
                arrayOf<Matcher>()(
                    ViewMatchers.isAssignableFrom(RecyclerView::class.java),
                    ViewMatchers.isDisplayed()
                )
            )
            *//*return Matchers.allOf<Any>(
                *arrayOf<Matcher<*>>(
                    ViewMatchers.isAssignableFrom(RecyclerView::class.java),
                    ViewMatchers.isDisplayed()
                )
            )*//*
        }

        override fun getDescription(): String {
            return "scroll RecyclerView to position: $position"
        }

        override fun perform(uiController: UiController?, view: View) {
            val recyclerView = view as RecyclerView
            recyclerView.scrollToPosition(position)
        }
    }*/
}