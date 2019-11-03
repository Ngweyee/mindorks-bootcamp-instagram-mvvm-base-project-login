package com.mindorks.bootcamp.instagram.data.repository

import com.mindorks.bootcamp.instagram.data.local.db.DatabaseService
import com.mindorks.bootcamp.instagram.data.local.prefs.UserPreferences
import com.mindorks.bootcamp.instagram.data.model.User
import com.mindorks.bootcamp.instagram.data.remote.NetworkService
import com.mindorks.bootcamp.instagram.data.remote.request.LoginRequest
import com.mindorks.bootcamp.instagram.data.remote.request.ProfileRequest
import com.mindorks.bootcamp.instagram.data.remote.request.SignUpRequest
import com.mindorks.bootcamp.instagram.data.remote.response.GeneralResponse
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val networkService: NetworkService,
    private val databaseService: DatabaseService,
    private val userPreferences: UserPreferences
) {

    fun saveCurrentUser(user: User) {
        userPreferences.setUserId(user.id)
        userPreferences.setUserName(user.name)
        userPreferences.setUserEmail(user.email)
        userPreferences.setAccessToken(user.accessToken)
        user.tagline?.let { userPreferences.setUserBio(it) }
        user.profilePicUrl?.let { userPreferences.setUserProfile(it) }

    }

    fun removeCurrentUser() {
        userPreferences.removeUserId()
        userPreferences.removeUserName()
        userPreferences.removeUserEmail()
        userPreferences.removeAccessToken()
        userPreferences.removeUserBio()
        userPreferences.removeUserProfile()
    }

    fun getCurrentUser(): User? {

        val userId = userPreferences.getUserId()
        val userName = userPreferences.getUserName()
        val userEmail = userPreferences.getUserEmail()
        val accessToken = userPreferences.getAccessToken()
        val userProfile = userPreferences.getUserProfile()
        val userBio = userPreferences.getUserBio()

        return if (userId !== null && userName != null && userEmail != null && accessToken != null)
            User(userId, userName, userEmail, accessToken,userProfile,userBio)
        else
            null
    }

    fun doUserLogin(email: String, password: String): Single<User> =
        networkService.doUserLogin(LoginRequest(email, password))
            .map {
                User(
                    it.userId,
                    it.userName,
                    it.userEmail,
                    it.accessToken,
                    it.profilePicUrl
                )
            }

    fun doUserSignup(name: String, email: String, password: String): Single<User> =
        networkService.doUserSignup(SignUpRequest(name, email, password))
            .map {
                User(
                    it.userId,
                    it.userName,
                    it.userEmail,
                    it.accessToken

                )
            }


    fun getMyInfo(user: User): Single<User> =
        networkService.fetMyInfoCall(user.id, user.accessToken)
            .map {
                it.data
            }

    fun updateMyInfo(user: User): Single<GeneralResponse> =
        networkService.updateMyInfoCall(
            ProfileRequest(
                user.name, user.profilePicUrl, user.tagline
            )
            , user.id,
            user.accessToken
        )


}