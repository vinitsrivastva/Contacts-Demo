package com.contactlist

import androidx.test.InstrumentationRegistry
import androidx.test.filters.SdkSuppress
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.runner.AndroidJUnit4


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        Assert.assertEquals("com.contactlist", appContext.packageName)
    }

}
