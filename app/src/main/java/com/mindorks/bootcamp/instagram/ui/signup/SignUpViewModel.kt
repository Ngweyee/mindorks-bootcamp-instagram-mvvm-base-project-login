package com.mindorks.bootcamp.instagram.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.mindorks.bootcamp.instagram.data.repository.UserRepository
import com.mindorks.bootcamp.instagram.ui.base.BaseViewModel
import com.mindorks.bootcamp.instagram.utils.common.*
import com.mindorks.bootcamp.instagram.utils.network.NetworkHelper
import com.mindorks.bootcamp.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class SignUpViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val userRepository: UserRepository
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    val launchLogin: MutableLiveData<Event<Map<String, String>>> = MutableLiveData()
    val launchDummy: MutableLiveData<Event<Map<String, String>>> = MutableLiveData()

    private val validationsList: MutableLiveData<List<Validation>> = MutableLiveData()

    val emailField: MutableLiveData<String> = MutableLiveData()
    val passwordField: MutableLiveData<String> = MutableLiveData()
    val nameField: MutableLiveData<String> = MutableLiveData()

    val emailValidation: LiveData<Resource<Int>> = filterValidation(Validation.Field.EMAIL)
    val passwordValidation: LiveData<Resource<Int>> = filterValidation(Validation.Field.PASSWORD)

    val isSigningUp: MutableLiveData<Boolean> = MutableLiveData()


    override fun onCreate() {}

    fun onEmailChange(email: String) = emailField.postValue(email)

    fun onNameChange(name: String) = nameField.postValue(name)

    fun onPasswordChange(email: String) = passwordField.postValue(email)

    private fun filterValidation(field: Validation.Field) =
        Transformations.map(validationsList) {
            it.find { validation -> validation.field == field }
                ?.run { return@run this.resource }
                ?: Resource.unknown()
        }

    fun onSignUp() {

        val name = nameField.value
        val email = emailField.value
        val password = passwordField.value

        val validations = Validator.validateSignUpFields(email, password)
        validationsList.postValue(validations)

        if (validations.isNotEmpty() && email != null && password != null && name != null) {
            val successValidation = validations.filter { it.resource.status == Status.SUCCESS }
            if (successValidation.size == validations.size && checkInternetConnectionWithMessage()) {
                isSigningUp.postValue(true)
                compositeDisposable.addAll(
                    userRepository.doUserSignup(name, email, password)
                        .subscribeOn(schedulerProvider.io())
                        .subscribe(
                            {
                                userRepository.saveCurrentUser(it)
                                isSigningUp.postValue(false)
                                launchDummy.postValue(Event(emptyMap()))
                            }, {
                                handleNetworkError(it)
                                isSigningUp.postValue(false)
                            }
                        )
                )
            }
        }

    }

    fun onLogin() {
        launchLogin.postValue(Event(emptyMap()))
    }

}
