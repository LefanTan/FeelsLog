package io.github.cmput301f19t19.legendary_fiesta;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.cmput301f19t19.legendary_fiesta.ui.FragmentEmptyClass;
import io.github.cmput301f19t19.legendary_fiesta.ui.ProfileFragment;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class ProfileTest {

    private ProfileFragment fragment;

    @Rule
    public ActivityTestRule<FragmentEmptyClass> mainActivityRule = new ActivityTestRule<>(FragmentEmptyClass.class);

    @Before
    public void init(){
        fragment = new ProfileFragment();
        mainActivityRule.getActivity().getSupportFragmentManager().beginTransaction()
                .add(1, fragment, null).commit();
    }

    @Test
    public void LaunchFragmentTest(){
        //check if Profile fragment is launched correctly
        onView(withId(R.id.user_profile)).check(matches(isDisplayed()));
    }

    @Test
    public void UsernameTextTest(){
        //basic text input
        onView(withId(R.id.userEditText)).check(matches(isDisplayed())).perform(typeText("Johnathan"));
    }

    @Test
    public void BirthDateTest(){

    }

    @Test
    public void BioTextTest(){

    }

    @Test
    public void DoneCancelTest(){

    }

}