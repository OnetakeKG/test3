package com.geekbrains.tests

import android.content.Context
import android.os.Build
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.geekbrains.tests.view.details.DetailsActivity
import com.geekbrains.tests.view.search.MainActivity
import junit.framework.TestCase
import kotlinx.android.synthetic.main.activity_main.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class MainActivityTest {
    private lateinit var scenario: ActivityScenario<MainActivity>
    private lateinit var context: Context

    @Before
    fun setUp() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun `activity not null`() {
        scenario.onActivity {
            TestCase.assertNotNull(it)
        }
    }

    @Test
    fun `activity is resumed`() {
        TestCase.assertEquals(Lifecycle.State.RESUMED, scenario.state)
    }

    @Test
    fun `progressBar not null`() {
        scenario.onActivity {
            TestCase.assertNotNull(it.findViewById<TextView>(R.id.progressBar))
        }
    }

    @Test
    fun `ToDetailsActivityButton is visible`() {
        scenario.onActivity {
            TestCase.assertEquals(
                View.VISIBLE,
                it.findViewById<Button>(R.id.toDetailsActivityButton)?.visibility
            )
        }
    }

    @Test
    fun `ToDetailsActivityButton works`() {
        scenario.onActivity { activity ->
            activity.toDetailsActivityButton.performClick()
            val startedIntent = shadowOf(activity).nextStartedActivity
            val shadowIntent = shadowOf(startedIntent)
            TestCase.assertEquals(DetailsActivity::class.java, shadowIntent.intentClass)
        }
    }

}