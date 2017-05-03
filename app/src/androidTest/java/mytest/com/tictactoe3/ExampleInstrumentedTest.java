package mytest.com.tictactoe2;

import android.app.Activity;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ExampleInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);

    public void doOne(int id){
        onView(withId(id)).perform(click());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private Activity getCurrentActivity() {
        final Activity[] activity = new Activity[1];
        onView(isRoot()).check(new ViewAssertion() {
            @Override
            public void check(View view, NoMatchingViewException noViewFoundException) {
                activity[0] = (Activity) view.getContext();
            }
        });
        return activity[0];
    }
    public int getAvailableButtonIndex(List<Button> btns){
        for(int j=0;j<9;j++){
            if(btns.get(j).getText().equals("")) return j;
        }
        return -1;
    }
    @Test
    public void checkOneWholeGame() {

        //Unfortunately, Espresso doesn't support conditional login in Espresso Code, so,
        //This is a work around. Preferable to maybe use Robotium instead, which does allow.
        MainActivity activity = (MainActivity)getCurrentActivity();
        List<Button> btns = activity.btns;
        for(int iCurrentMove=0;iCurrentMove<5;iCurrentMove++){
            int index = getAvailableButtonIndex(btns);
            if(index!= -1)doOne(btns.get(index).getId());
            if(iCurrentMove>2){
                Log.i("myapp","iCurrentMove:"+iCurrentMove);
                if(activity.presenter.game_over) {
                    onView(withText("Do you want to start a new game?")).check(matches(isDisplayed()));
                    onView(withText("YES")).check(matches(isDisplayed()));
                    onView(withText("YES")).perform(click());
                    return;
                }
            }
        }
    }
}
