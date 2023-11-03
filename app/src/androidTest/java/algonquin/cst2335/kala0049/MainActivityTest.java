package algonquin.cst2335.kala0049;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * The MainActivityTest class contains tests for the MainActivity class.
 * @author Sujal Kalathiya
 * @version 1.0
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    /**
     * This test case checks the behavior when the password entered does not meet the complexity requirements.
     * It enters a simple numeric password and verifies that the appropriate message is displayed.
     */
    @Test
    public void mainActivityTest() {

        ViewInteraction appCompatEditText = onView( withId(R.id.editTextTextPassword) );
        appCompatEditText.perform(replaceText("12345"), closeSoftKeyboard());

        ViewInteraction materialButton = onView( withId(R.id.login_button) );
        materialButton.perform(click());

        ViewInteraction textView = onView( withId(R.id.textView) );
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * This test case checks the behavior when the password is missing an uppercase letter.
     * It enters a password without any uppercase letter and verifies that the appropriate message is displayed.
     */
    @Test
    public void testFindMissingUpperCase() {

        ViewInteraction appCompatEditText = onView( withId(R.id.editTextTextPassword) );
        appCompatEditText.perform(replaceText("password123#$*"));

        ViewInteraction materialButton = onView( withId(R.id.login_button) );
        materialButton.perform(click());

        ViewInteraction textView = onView( withId(R.id.textView) );
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * This test case checks the behavior when the password is missing a lowercase letter.
     * It enters a password without any lowercase letter and verifies that the appropriate message is displayed.
     */
    @Test
    public void testFindMissingLowerCase() {

        ViewInteraction appCompatEditText = onView( withId(R.id.editTextTextPassword) );
        appCompatEditText.perform(replaceText("PASSWORD123#$*"));

        ViewInteraction materialButton = onView( withId(R.id.login_button) );
        materialButton.perform(click());

        ViewInteraction textView = onView( withId(R.id.textView) );
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * This test case checks the behavior when the password is missing a number.
     * It enters a password without any numeric character and verifies that the appropriate message is displayed.
     */
    @Test
    public void testFindMissingNumber() {

        ViewInteraction appCompatEditText = onView( withId(R.id.editTextTextPassword) );
        appCompatEditText.perform(replaceText("Password#$*"));

        ViewInteraction materialButton = onView( withId(R.id.login_button) );
        materialButton.perform(click());

        ViewInteraction textView = onView( withId(R.id.textView) );
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * This test case checks the behavior when the password is missing a special character.
     * It enters a password without any special character and verifies that the appropriate message is displayed.
     */
    @Test
    public void testFindMissingSpecialChar() {

        ViewInteraction appCompatEditText = onView( withId(R.id.editTextTextPassword) );
        appCompatEditText.perform(replaceText("Password123"));

        ViewInteraction materialButton = onView( withId(R.id.login_button) );
        materialButton.perform(click());

        ViewInteraction textView = onView( withId(R.id.textView) );
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * This test case checks the behavior when the password meets all complexity requirements.
     * It enters a password that includes an uppercase letter, a lowercase letter, a number, and a special character,
     * and verifies that the appropriate message is displayed.
     */
    @Test
    public void testAllpassed() {

        ViewInteraction appCompatEditText = onView( withId(R.id.editTextTextPassword) );
        appCompatEditText.perform(replaceText("Password123#$*"));

        ViewInteraction materialButton = onView( withId(R.id.login_button) );
        materialButton.perform(click());

        ViewInteraction textView = onView( withId(R.id.textView) );
        textView.check(matches(withText("Your password meets the requirements")));
    }

}
