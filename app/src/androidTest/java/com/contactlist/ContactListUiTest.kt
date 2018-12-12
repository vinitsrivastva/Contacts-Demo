package com.contactlist

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import org.junit.runner.RunWith
import androidx.test.espresso.Espresso.onView
import androidx.test.rule.ActivityTestRule
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.InstrumentationRegistry.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.uiautomator.*
import org.junit.*
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiScrollable
import com.contactlist.model.SimpleContact
import org.junit.runners.MethodSorters
import androidx.test.espresso.Espresso.onView
import androidx.test.uiautomator.UiObjectNotFoundException
import java.lang.AssertionError
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.SearchCondition




@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class ContactListUiTest {

    private lateinit var mDevice: UiDevice
    val ALLOW = "ALLOW"
    val DENY = "DENY"
    val DONT_ASK_AGAIN = "Don't ask again"

    @Rule @JvmField val activity =
            ActivityTestRule<MainActivity>(MainActivity::class.java)

    /**
     * Add contacts mentioned below in the contact list , duplicate contacts should appear at once only.
     */

    val AVAILABLE_CONTACTS = arrayOf<SimpleContact>(

            SimpleContact("A", "8510977618"),
            SimpleContact("B", "8076449107")
    )

    val REMOVED_CONTACTS = arrayOf<SimpleContact>(

            SimpleContact("C", "+918076449107"),
            SimpleContact("D", "08510977618"),
            SimpleContact("E", "851-097-7618")
    )

    @Before
    fun setUp() {

        mDevice = UiDevice.getInstance(getInstrumentation())

    }

    /**
     * Test dialog at startup.
     */

    @Test
    @Throws(Exception::class)
    fun a_displayDialogAtStartup(){

        val allowBtn: UiObject = mDevice.findObject(
                UiSelector().text(ALLOW).className("android.widget.Button")
        )

        val denyBtn: UiObject = mDevice.findObject(
                UiSelector().text(DENY).className("android.widget.Button")
        )

        Assert.assertTrue( "View with $ALLOW not found" , allowBtn.exists())
        Assert.assertTrue( "View with $DENY not found" , denyBtn.exists())


        denyCurrentPermission()
    }

    /**
     * Test short rationale if permission is denied.
     */

    @Test
    @Throws(Exception::class)
    fun b_displayPermissionDeniedShortRationale(){

        denyCurrentPermission()

        val permissionView: UiObject = mDevice.findObject(
                UiSelector().resourceId("com.contactlist:id/permission_denied_rationale").className("android.widget.TextView")
        )

        val permissionButton: UiObject = mDevice.findObject(
                UiSelector().resourceId("com.contactlist:id/grant_permission_button").className("android.widget.Button")
        )

        Assert.assertTrue( "Permission View not found" , permissionView.waitForExists(5000))

        Assert.assertTrue( "Permission Button not found" , permissionButton.exists())


    }

    /**
     * Test long rationale if permission is denied permanently.
     */

    @Test
    @Throws(Exception::class)
    fun c_displayPermissionDeniedLongRationale(){

        val dontAskAgainCheckbox: UiObject = mDevice.findObject(
                UiSelector().text(DONT_ASK_AGAIN).className("android.widget.CheckBox")
        )

        if (dontAskAgainCheckbox.exists()) {

            dontAskAgainCheckbox.click()
        }

        denyCurrentPermission()

        val permissionView: UiObject = mDevice.findObject(
                UiSelector().resourceId("com.contactlist:id/permission_denied_rationale").className("android.widget.TextView")
        )

        val permissionButton: UiObject = mDevice.findObject(
                UiSelector().resourceId("com.contactlist:id/grant_permission_button").className("android.widget.Button")
        )


        Assert.assertTrue( "Permission View not found" , permissionView.exists())

        Assert.assertTrue( "Permission Button not found" , permissionButton.exists())


        if (permissionButton.exists()) {

            permissionButton.clickAndWaitForNewWindow()
        }

        val permissions: UiObject = mDevice.findObject(
                UiSelector().text("Permissions")
        )
        permissions.click()

        val contactToggle: UiObject = mDevice.findObject(
                UiSelector().text("Contacts")
        )
        contactToggle.click()


    }

    /**
     * Test contacts display when permission is granted.
     */

    @Test
    @Throws(Exception::class)
    fun d_displayContactsWhenPermissionGranted(){

        val listView = UiScrollable(
                UiSelector().className("android.support.v7.widget.RecyclerView"))
        listView.setMaxSearchSwipes(60)

        for (contact:SimpleContact in AVAILABLE_CONTACTS) {

            val num = contact.phoneNumber
            val numView: UiObject = listView.getChildByText(
                    UiSelector().className("android.widget.TextView"),
                    num
            )

            Assert.assertTrue("$num not found", numView.exists());
        }


        for (contact:SimpleContact in REMOVED_CONTACTS) {

            val num = contact.phoneNumber
            var numView: UiObject? = null
            try {

                numView = listView.getChildByText(
                        UiSelector().className("android.widget.TextView"),
                        num , true
                )

                if(numView.exists())
                    throw AssertionError("View with $num found")


            }catch (e:UiObjectNotFoundException) {

                Assert.assertTrue("View with $num not found", (numView == null));

            }
        }

    }

    // helper methods

    @Throws(UiObjectNotFoundException::class)
    fun denyCurrentPermission() {

        val denyBtn: UiObject = mDevice.findObject(
                UiSelector().text(DENY).className("android.widget.Button")
        )

        if (denyBtn.exists() && denyBtn.isEnabled) {
            denyBtn.click()
        }
    }


}