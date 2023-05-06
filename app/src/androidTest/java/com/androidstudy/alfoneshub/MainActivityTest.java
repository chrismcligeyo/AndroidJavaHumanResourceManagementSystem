package com.androidstudy.alfoneshub;


import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<WelcomeActivity> mActivityTestRule = new ActivityTestRule<>(WelcomeActivity.class);

    @Test
    public void mainActivityTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(900);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.edit_text_email),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                2)));
        appCompatEditText.perform(scrollTo(), click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.edit_text_email),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                2)));
        appCompatEditText2.perform(scrollTo(), replaceText("orimboeugine@gmaul.com"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.edit_text_password),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                3)));
        appCompatEditText3.perform(scrollTo(), replaceText("18q8gzb4"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.btn_login), withText("Login"),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                4)));
        appCompatButton.perform(scrollTo(), click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.confirm_button), withText("Ok"),
                        childAtPosition(
                                withId(R.id.buttons_container),
                                2),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.edit_text_email), withText("orimboeugine@gmaul.com"),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                2)));
        appCompatEditText4.perform(scrollTo(), replaceText("orimboeugine@gmail.com"));

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.edit_text_email), withText("orimboeugine@gmail.com"),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                2),
                        isDisplayed()));
        appCompatEditText5.perform(closeSoftKeyboard());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.btn_login), withText("Login"),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                4)));
        appCompatButton3.perform(scrollTo(), click());

        DataInteraction linearLayout = onData(anything())
                .inAdapterView(withId(R.id.gridview))
                .atPosition(0);
        linearLayout.perform(scrollTo(), click());

        ViewInteraction materialSpinner = onView(
                allOf(withId(R.id.spinner_date), withText("2019-01-15"),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                1)));
        materialSpinner.perform(scrollTo(), click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(
                                withId(R.id.action_bar),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(
                                withId(R.id.action_bar),
                                1),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(
                                withId(R.id.action_bar),
                                1),
                        isDisplayed()));
        appCompatImageButton3.perform(click());

        ViewInteraction appCompatImageButton4 = onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(
                                withId(R.id.action_bar),
                                1),
                        isDisplayed()));
        appCompatImageButton4.perform(click());

        ViewInteraction appCompatImageButton5 = onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(
                                withId(R.id.action_bar),
                                1),
                        isDisplayed()));
        appCompatImageButton5.perform(click());

        pressBack();

        DataInteraction linearLayout2 = onData(anything())
                .inAdapterView(withId(R.id.gridview))
                .atPosition(1);
        linearLayout2.perform(scrollTo(), click());

        ViewInteraction cardView = onView(
                allOf(withId(R.id.card_view_general_expense),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                1),
                        isDisplayed()));
        cardView.perform(click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.button_add_item), withText("ADD ITEM"),
                        childAtPosition(
                                withId(R.id.linear_layout_expense_item),
                                0)));
        appCompatButton4.perform(scrollTo(), click());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.edit_text_item_name),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                1)));
        appCompatEditText6.perform(scrollTo(), click());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.edit_text_item_name),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                1)));
        appCompatEditText7.perform(scrollTo(), replaceText("Moneyy"), closeSoftKeyboard());

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.edit_text_item_descrption),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                3)));
        appCompatEditText8.perform(scrollTo(), replaceText("Sample description"), closeSoftKeyboard());

        ViewInteraction appCompatEditText9 = onView(
                allOf(withId(R.id.edit_text_item_supplier),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                5)));
        appCompatEditText9.perform(scrollTo(), replaceText("Sample supplier"), closeSoftKeyboard());

        ViewInteraction appCompatEditText10 = onView(
                allOf(withId(R.id.edit_text_item_unit_cost),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                7)));
        appCompatEditText10.perform(scrollTo(), replaceText("2000"), closeSoftKeyboard());

        ViewInteraction appCompatEditText11 = onView(
                allOf(withId(R.id.edit_text_item_quantity),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                9)));
        appCompatEditText11.perform(scrollTo(), replaceText("20"), closeSoftKeyboard());

        pressBack();

        ViewInteraction appCompatEditText12 = onView(
                allOf(withId(R.id.edit_text_item_days),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                11)));
        appCompatEditText12.perform(scrollTo(), click());

        ViewInteraction appCompatEditText13 = onView(
                allOf(withId(R.id.edit_text_item_days),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                11)));
        appCompatEditText13.perform(scrollTo(), replaceText("1"), closeSoftKeyboard());

        pressBack();

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.dialog_info_create), withText("CREATE"),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                0)));
        appCompatButton5.perform(scrollTo(), click());

        pressBack();

        ViewInteraction tabView = onView(
                allOf(childAtPosition(
                        withClassName(is("com.google.android.material.tabs.TabLayout$SlidingTabStrip")),
                        1),
                        isDisplayed()));
        tabView.perform(click());

        pressBack();

        pressBack();

        pressBack();

        ViewInteraction linearLayout3 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.buttonContainer),
                                childAtPosition(
                                        withId(R.id.buttonMenu),
                                        0)),
                        0),
                        isDisplayed()));
        linearLayout3.check(matches(isDisplayed()));

        ViewInteraction linearLayout4 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.buttonContainer),
                                childAtPosition(
                                        withId(R.id.buttonMenu),
                                        0)),
                        0),
                        isDisplayed()));
        linearLayout4.check(matches(isDisplayed()));

        ViewInteraction linearLayout5 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.buttonContainer),
                                childAtPosition(
                                        withId(R.id.buttonMenu),
                                        0)),
                        0),
                        isDisplayed()));
        linearLayout5.check(matches(isDisplayed()));

        ViewInteraction linearLayout6 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.buttonContainer),
                                childAtPosition(
                                        withId(R.id.buttonMenu),
                                        0)),
                        0),
                        isDisplayed()));
        linearLayout6.check(matches(isDisplayed()));

        ViewInteraction linearLayout7 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.buttonContainer),
                                childAtPosition(
                                        withId(R.id.buttonMenu),
                                        0)),
                        0),
                        isDisplayed()));
        linearLayout7.check(matches(isDisplayed()));

        ViewInteraction linearLayout8 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.buttonContainer),
                                childAtPosition(
                                        withId(R.id.buttonMenu),
                                        0)),
                        0),
                        isDisplayed()));
        linearLayout8.check(matches(isDisplayed()));

        DataInteraction linearLayout9 = onData(anything())
                .inAdapterView(withId(R.id.gridview))
                .atPosition(0);
        linearLayout9.perform(scrollTo(), click());

        ViewInteraction linearLayout10 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                0),
                        0),
                        isDisplayed()));
        linearLayout10.check(matches(isDisplayed()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.text_view_location_name), withText("NAIROBI CINEMA"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0),
                                1),
                        isDisplayed()));
        textView.check(matches(withText("NAIROBI CINEMA")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.text_view_profile), withText("N"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText("N")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.text_view_team_leader_name), withText("Edwin K.Gitau"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        1),
                                1),
                        isDisplayed()));
        textView3.check(matches(isDisplayed()));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.text_view_interactions), withText("0"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        3),
                                1),
                        isDisplayed()));
        textView4.check(matches(isDisplayed()));

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.text_view_sales), withText("0"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        5),
                                1),
                        isDisplayed()));
        textView5.check(matches(isDisplayed()));

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.text_view_sales), withText("0"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        5),
                                1),
                        isDisplayed()));
        textView6.check(matches(isDisplayed()));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
