package forum.student.studentforumad;

import android.support.test.rule.ActivityTestRule;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class HomePageTest {

    @Rule
    public ActivityTestRule<HomePage> mHomeTestRule= new ActivityTestRule<HomePage>(HomePage.class);

    private HomePage mHomeTest = null;

    @Before
    public void setUp() throws Exception {
        mHomeTest = mHomeTestRule.getActivity();
    }

    @Test
    public void testLaunch(){
        View view = mHomeTest.findViewById(R.id.btnAddPost);
        assertNotNull(view);
    }
    @After
    public void tearDown() throws Exception {
        mHomeTest = null;
    }
}