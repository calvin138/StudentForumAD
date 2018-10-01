package forum.student.studentforumad;

import android.support.test.rule.ActivityTestRule;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class LoginPasswordTest {

    @Rule
    public ActivityTestRule<LoginPage> mLoginTestRule= new ActivityTestRule<LoginPage>(LoginPage.class);

    private LoginPage mLoginTest = null;

    @Before
    public void setUp() throws Exception {
        mLoginTest = mLoginTestRule.getActivity();
    }

    @Test
    public void testLaunch(){
        View view = mLoginTest.findViewById(R.id.etPassword);
        assertNotNull(view);
    }
    @After
    public void tearDown() throws Exception {
        mLoginTest = null;
    }
}