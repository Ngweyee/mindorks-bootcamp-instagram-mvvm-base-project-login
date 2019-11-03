package com.mindorks.bootcamp.instagram.ui.main

import androidx.lifecycle.MutableLiveData
import com.mindorks.bootcamp.instagram.ui.base.BaseViewModel
import com.mindorks.bootcamp.instagram.utils.common.Event
import com.mindorks.bootcamp.instagram.utils.network.NetworkHelper
import com.mindorks.bootcamp.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class MainViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {


    val homeNavigation = MutableLiveData<Event<Boolean>>()
    val photoNavigation = MutableLiveData<Event<Boolean>>()
    val profileNavigation = MutableLiveData<Event<Boolean>>()

    override fun onCreate() {

        homeNavigation.postValue(Event(true))
    }

    fun homeSelected() {
        homeNavigation.postValue(Event(true))
    }

    fun addPhotoSelected() {
        photoNavigation.postValue(Event(true))
    }

    fun profileSelected() {
        profileNavigation.postValue(Event(true))
    }


}