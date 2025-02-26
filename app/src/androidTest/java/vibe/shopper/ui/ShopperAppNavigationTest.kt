package vibe.shopper.ui

import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import vibe.shopper.MainActivity
import vibe.shopper.R
import vibe.shopper.util.assertCurrentRouteName

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ShopperAppNavigationTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var navController: TestNavHostController

    @Before
    fun setup() {
        hiltRule.inject()

        composeTestRule.activity.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }
            ShopperNavigation(navController)
        }

        composeTestRule.runOnIdle {
            assertNotNull(navController) // Ensure navController is initialized
        }

        composeTestRule.waitForIdle() // Wait for Compose to stabilize
    }

    @Test
    fun homeScreen_isDisplayed_onLaunch() {
        composeTestRule.onNodeWithText("Products").assertIsDisplayed()
    }

    @Test
    fun clickingCart_navigatesToCartScreen() {
        val labelCart = composeTestRule.activity.getString(R.string.cart)
        composeTestRule.onNodeWithContentDescription(labelCart).performClick()

        composeTestRule.waitForIdle() // Wait for UI stabilization

        composeTestRule.runOnIdle {
            navController.assertCurrentRouteName(Screen.Cart.route)
        }
    }
}
