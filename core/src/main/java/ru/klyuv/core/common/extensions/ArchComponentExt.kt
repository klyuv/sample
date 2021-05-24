package ru.klyuv.core.common.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun <T> LifecycleOwner.observe(liveData: LiveData<T>, action: (t: T) -> Unit) {
    liveData.observe(this, Observer { it?.let { t -> action(t) } })
}

fun <T> Fragment.observe(liveData: LiveData<T>, action: (t: T) -> Unit) {
    viewLifecycleOwner.observe(liveData, action)
}

inline fun <reified T : ViewModel> Fragment.getViewModel(viewModelFactory: ViewModelProvider.Factory): T =
    ViewModelProvider(this, viewModelFactory)[T::class.java]

inline fun <reified T : ViewModel> FragmentActivity.getViewModel(viewModelFactory: ViewModelProvider.Factory): T =
    ViewModelProvider(this, viewModelFactory)[T::class.java]

inline fun <reified T : ViewModel> Fragment.getParentViewModel(): T =
    ViewModelProvider(requireParentFragment()).get(T::class.java)

inline fun <reified T : ViewModel> Fragment.getParentActivityViewModel(): T =
    ViewModelProvider(requireActivity()).get(T::class.java)

fun <T> LifecycleOwner.observe(flow: Flow<T>, action: suspend (t: T) -> Unit) {
    flow.onEach { it?.let { t -> action(t) } }
        .launchIn(this.lifecycleScope)
}

fun <T> Fragment.observe(flow: Flow<T>, action: suspend (t: T) -> Unit) {
    viewLifecycleOwner.observe(flow, action)
}