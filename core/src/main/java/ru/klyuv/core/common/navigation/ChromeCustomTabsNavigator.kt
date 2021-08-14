package ru.klyuv.core.common.navigation

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.AttributeSet
import androidx.annotation.ColorRes
import androidx.annotation.NavigationRes
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.NavHostFragment
import ru.klyuv.core.R
import ru.klyuv.core.common.extensions.sendLog
import kotlin.collections.mutableMapOf
import kotlin.collections.set


@Navigator.Name("chrome")
class ChromeCustomTabsNavigator(private val context: Context) :
    Navigator<ChromeCustomTabsNavigator.Destination>() {

    private val urisInProgress = mutableMapOf<Uri, Long>()

    /**
     * Prevent the user from repeatedly launching Chrome Custom Tabs for the same URL. Throttle
     * rapid repeats unless the URL has finished loading, or this timeout has passed (just in
     * case something went wrong with detecting that the page finished loading).
     * Feel free to change this value with [Fragment.findChromeCustomTabsNavigator.throttleTimeout()]
     * if you feel the need, or for testing purposes.
     * Defaults to two seconds.
     */
    @SuppressWarnings("WeakerAccess")
    var throttleTimeout: Long = 2000L

    override fun createDestination(): Destination = Destination(this)

    override fun navigate(
        destination: Destination,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Extras?
    ): NavDestination? {
        val uri = args?.getParcelable<Uri>(KEY_URI)!!
        val packageName = args?.getString(KEY_PACKAGE)!!

        if (!shouldAllowLaunch(uri)) return null

        buildCustomTabsIntent(destination, packageName).launchUrl(context, uri)

        return null
    }

    private fun buildCustomTabsIntent(
        destination: Destination,
        packageName: String
    ): CustomTabsIntent {
        val builder = CustomTabsIntent.Builder()

        val defaultColors = CustomTabColorSchemeParams.Builder()
        if (destination.toolbarColor != 0) {
            defaultColors.setToolbarColor(ContextCompat.getColor(context, destination.toolbarColor))
        }
        if (destination.secondaryToolbarColor != 0) {
            defaultColors.setNavigationBarColor(
                ContextCompat.getColor(
                    context,
                    destination.secondaryToolbarColor
                )
            )
        }

        builder.setDefaultColorSchemeParams(defaultColors.build())

        val customTabsIntent = builder.build()
        customTabsIntent.intent.apply {
            setPackage(packageName)
        }
//        customTabsIntent.intent.putExtra(
//            Intent.EXTRA_REFERRER,
//            Uri.parse("android-app://" + context.packageName)
//        )
//        customTabsIntent.intent.action = CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION

        return customTabsIntent
    }

    override fun popBackStack(): Boolean = true

    private fun shouldAllowLaunch(uri: Uri): Boolean {
        urisInProgress[uri]?.let { tabStartTime ->
            // Have we launched this URI before recently?
            if (System.currentTimeMillis() - tabStartTime > throttleTimeout) {
                // Since we've exceeded the throttle timeout, continue as normal, launching
                // the destination and updating the time.
                sendLog(
                    "Throttle timeout for $uri exceeded. This means ChromeCustomTabsNavigator "
                            + "failed to accurately determine that the URL finished loading. If you see this error "
                            + "frequently, it could indicate a bug in ChromeCustomTabsNavigator."
                )
            } else {
                // The user has tried to repeatedly open the same URL in rapid succession. Let them chill.
                // The tab probably just hasn't opened yet. Abort opening the tab a second time.
                urisInProgress.remove(uri)
                return false
            }
        }
        urisInProgress[uri] = System.currentTimeMillis()
        return true
    }

    @NavDestination.ClassType(Activity::class)
    class Destination(navigator: Navigator<out NavDestination>) : NavDestination(navigator) {

        @ColorRes
        var toolbarColor: Int = 0

        @ColorRes
        var secondaryToolbarColor: Int = 0

        override fun onInflate(context: Context, attrs: AttributeSet) {
            super.onInflate(context, attrs)

            context.withStyledAttributes(attrs, R.styleable.ChromeCustomTabsNavigator, 0, 0) {
                toolbarColor = getResourceId(R.styleable.ChromeCustomTabsNavigator_toolbarColor, 0)
                secondaryToolbarColor =
                    getResourceId(R.styleable.ChromeCustomTabsNavigator_secondaryToolbarColor, 0)
            }
        }
    }

    companion object {
        private const val KEY_URI = "uri"
        private const val KEY_PACKAGE = "packageName"
    }

}

class MyNavHostFragment : NavHostFragment() {

    override fun onCreateNavController(navController: NavController) {
        super.onCreateNavController(navController)

        context?.let {
            navController.navigatorProvider.addNavigator(ChromeCustomTabsNavigator(it))
        }
    }

    companion object {
        private const val KEY_GRAPH_ID = "android-support-nav:fragment:graphId"
        private const val KEY_START_DESTINATION_ARGS =
            "android-support-nav:fragment:startDestinationArgs"

        @JvmStatic
        fun create(
            @NavigationRes graphResId: Int,
            startDestinationArgs: Bundle? = null
        ): MyNavHostFragment {
            if (graphResId == 0) throw Exception("Navigation graph id cannot be 0")
            val bundle: Bundle = Bundle().apply {
                putInt(KEY_GRAPH_ID, graphResId)
                if (startDestinationArgs != null) {
                    putBundle(KEY_START_DESTINATION_ARGS, startDestinationArgs)
                }
            }
            return MyNavHostFragment().apply {
                arguments = bundle
            }
        }
    }

}