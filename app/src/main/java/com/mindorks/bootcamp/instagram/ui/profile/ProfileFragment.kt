package com.mindorks.bootcamp.instagram.ui.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.data.model.User
import com.mindorks.bootcamp.instagram.di.component.FragmentComponent
import com.mindorks.bootcamp.instagram.ui.base.BaseFragment
import com.mindorks.bootcamp.instagram.ui.login.LoginActivity
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : BaseFragment<ProfileViewModel>() {

    companion object {
        const val TAG = "ProfileFragment"
        const val EDIT_PROFILE_REQUEST = 1;
        fun newInstance(): ProfileFragment {
            val args = Bundle()
            val fragment = ProfileFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun provideLayoutId(): Int = R.layout.fragment_profile

    override fun injectDependencies(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun setupView(view: View) {

        bt_edit_profile.setOnClickListener {
            viewModel.goEditProfile()
        }

        tvLogout.setOnClickListener {
            viewModel.logout()
            goBack()
        }


    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.launchEditProfile.observe(this, Observer {
            it.getIfNotHandled()?.run {
                startActivityForResult(
                    Intent(context?.applicationContext, EditProfileActivity::class.java),
                    EDIT_PROFILE_REQUEST
                )
            }
        })

        viewModel.launchLogin.observe(this, Observer {
            it.getIfNotHandled()?.run {
                startActivity(Intent(context?.applicationContext, LoginActivity::class.java))
            }
        })

        viewModel.imageUrl.observe(this, Observer {
            it.run {
                val glideRequest = Glide
                    .with(ivProfile)
                    .load(it)
                    .apply(RequestOptions.centerCropTransform())
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_profile_signup))

                glideRequest.into(ivProfile)
            }
        })

        viewModel.nameField.observe(this, Observer {
            tvProfileName.setText(it)
        })

        viewModel.tagLine.observe(this, Observer {
            tvProfileDetail.setText(it)
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                EDIT_PROFILE_REQUEST -> {
                    if (data != null) {
                        val newUser = data.getParcelableExtra<User>("User")
                        viewModel.onImageChange(newUser.profilePicUrl.toString())
                        viewModel.onNameChange(newUser.name)
                        viewModel.onBioChange(newUser.tagline.toString())
                    }
                }
            }


        }
    }

}