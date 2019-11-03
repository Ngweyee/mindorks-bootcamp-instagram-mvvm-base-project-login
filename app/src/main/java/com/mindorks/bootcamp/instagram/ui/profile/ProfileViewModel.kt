package com.mindorks.bootcamp.instagram.ui.profile

import androidx.lifecycle.MutableLiveData
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.data.model.User
import com.mindorks.bootcamp.instagram.data.repository.UserRepository
import com.mindorks.bootcamp.instagram.ui.base.BaseViewModel
import com.mindorks.bootcamp.instagram.utils.common.Event
import com.mindorks.bootcamp.instagram.utils.common.Resource.Companion.error
import com.mindorks.bootcamp.instagram.utils.network.NetworkHelper
import com.mindorks.bootcamp.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class ProfileViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val userRepository: UserRepository
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    val nameField: MutableLiveData<String> = MutableLiveData()

    val emailField: MutableLiveData<String> = MutableLiveData()

    val tagLine: MutableLiveData<String> = MutableLiveData()

    val imageUrl: MutableLiveData<String> = MutableLiveData()

    val launchLogin = MutableLiveData<Event<Map<String, String>>>()

    private val user = userRepository.getCurrentUser()!!

    val uploading: MutableLiveData<Boolean> = MutableLiveData()

    val updating: MutableLiveData<Pair<Boolean,User>> = MutableLiveData()

    fun onBioChange(bio: String) = tagLine.postValue(bio)

    fun onNameChange(name: String) = nameField.postValue(name)

    fun onImageChange(url: String) {
        imageUrl.postValue(url)
        if (!imageUrl.value.isNullOrBlank()) uploading.value = true
    }

    val launchEditProfile = MutableLiveData<Event<Map<String, String>>>()

    init {
        getMyInfo()
    }

    fun getMyInfo() {
        compositeDisposable.add(
            userRepository.getMyInfo(user)
                .subscribeOn(schedulerProvider.io())
                .subscribe({
                    nameField.postValue(it.name)
                    tagLine.postValue(it.tagline)
                    imageUrl.postValue(it.profilePicUrl)
                    emailField.postValue(user.email)
                }, {
                    handleNetworkError(it)
                })
        )
    }

    override fun onCreate() {}

    fun goEditProfile() {
        launchEditProfile.postValue(Event(emptyMap()))
    }

    fun logout() {
        userRepository.removeCurrentUser()
        launchLogin.postValue(Event(emptyMap()))
    }

    fun saveEditedUser() {

        if (nameField.value.isNullOrBlank()) messageStringId.postValue(error(R.string.name_field_empty))

        if (nameField.value != null && checkInternetConnectionWithMessage()) {
            val newUser = User(
                id = user.id,
                name = nameField.value.toString(),
                email = user.email,
                profilePicUrl = imageUrl.value.toString(),
                tagline = tagLine.value.toString(),
                accessToken = user.accessToken
            )
            compositeDisposable.add(
                userRepository.updateMyInfo(
                    newUser
                ).subscribeOn(schedulerProvider.io())
                    .subscribe({
                        updating.postValue(Pair(true,newUser))
                    }, {
                        handleNetworkError(it)
                    })
            )
        }

    }



}