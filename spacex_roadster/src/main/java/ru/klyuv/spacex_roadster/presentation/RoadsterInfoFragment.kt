package ru.klyuv.spacex_roadster.presentation

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import ru.klyuv.core.common.data.MainFailureModel
import ru.klyuv.core.common.extensions.*
import ru.klyuv.core.common.ui.BaseFragment
import ru.klyuv.core.model.RoadsterUIModel
import ru.klyuv.core.model.state.RoadsterState
import ru.klyuv.spacex_roadster.R
import ru.klyuv.spacex_roadster.databinding.FragmentRoadsterInfoBinding


class RoadsterInfoFragment : BaseFragment() {

    private val viewModel by androidLazy { getViewModel<RoadsterInfoViewModel>(viewModelFactory) }
    private val viewBinding: FragmentRoadsterInfoBinding by viewBinding(FragmentRoadsterInfoBinding::bind)

    private val roadsterAdapter by androidLazy {
        ListDelegationAdapter<List<RoadsterUIModel>>(
            RoadsterMainItemDelegate.init(::openYoutubeLink, ::openInChromeCustomTab),
            RoadsterCharacterItemDelegate.init()
        )
    }

    override fun getLayoutID(): Int = R.layout.fragment_roadster_info

    override fun setUI(savedInstanceState: Bundle?) {
        with(viewBinding) {

            swipeRoadster.apply {
                setOnRefreshListener { onRefresh() }
                setColorSchemeColors(ContextCompat.getColor(this.context, R.color.colorPrimary))
            }

            rvRoadster.apply {
                layoutManager = LinearLayoutManager(this.context)
                adapter = roadsterAdapter
                setHasFixedSize(true)
            }
        }
        sendLog("setUI")
    }

    override fun observeViewModel() {
        observe(viewModel.roadsterFlow, ::checkRoadsterState)
    }

    private fun checkRoadsterState(state: RoadsterState) {
        sendLog("roadsterState: $state")
        when (state) {
            is RoadsterState.Success -> setSuccessState(state.data)
            is RoadsterState.Loading -> setLoadingState()
            is RoadsterState.Failed -> setFailureState(state.failure)
            is RoadsterState.Nothing -> Unit
        }
    }

    private fun setLoadingState() = with(viewBinding) {
        rvRoadster.toGone()
        swipeRoadster.isRefreshing = true
    }

    private fun setFailureState(data: MainFailureModel) {
        viewBinding.swipeRoadster.isRefreshing = true
        if (data.show) showFailureError(data.failure) { data.show = false }
    }

    private fun setSuccessState(data: List<RoadsterUIModel>) {
        roadsterAdapter.items = data
        roadsterAdapter.notifyDataSetChanged()
        with(viewBinding) {
            swipeRoadster.isRefreshing = false
            rvRoadster.toVisible()
        }
    }

    private fun onRefresh() {
        viewModel.getData()
    }

    private fun openYoutubeLink(youtubeUrl: String) {
        if (requireContext().isYouTubeInstalled()) {
            val youtubeID = youtubeUrl.split("/").lastOrNull()
            if (youtubeID == null) showLongToast("Error")
            else {
                try {
                    val intentApp =
                        Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://$youtubeID"))
                    this.startActivity(intentApp)
                } catch (ex: ActivityNotFoundException) {
                    openInChromeCustomTab(youtubeUrl)
                }
            }
        } else openInChromeCustomTab(youtubeUrl)

    }

    private fun openInChromeCustomTab(url: String) {
        val customTabsPackages = requireContext().getCustomTabsPackages()
        if (customTabsPackages.isNotEmpty()) {
            findNavController().navigate(
                RoadsterInfoFragmentDirections.actionRoadsterInfoFragmentToChromeTest(
                    Uri.parse(url),
                    requireContext().getCustomTabsPackages().first().activityInfo.packageName
                )
            )
        } else {
            val intentBrowser = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            this.startActivity(intentBrowser)
        }

    }

}