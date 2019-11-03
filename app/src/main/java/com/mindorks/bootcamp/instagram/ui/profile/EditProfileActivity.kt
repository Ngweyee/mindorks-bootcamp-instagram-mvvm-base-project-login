package com.mindorks.bootcamp.instagram.ui.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mindorks.bootcamp.instagram.di.component.ActivityComponent
import com.mindorks.bootcamp.instagram.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_edit_profile.*


class EditProfileActivity : BaseActivity<ProfileViewModel>() {

    companion object {
        const val PICK_IMAGE = 1
    }

    override fun provideLayoutId(): Int =
        com.mindorks.bootcamp.instagram.R.layout.activity_edit_profile

    override fun injectDependencies(activityComponent: ActivityComponent) =
        activityComponent.inject(this)

    override fun setupView(savedInstanceState: Bundle?) {

        etEditName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.onNameChange(s.toString())
            }
        })

        etEditBio.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.onBioChange(s.toString())
            }
        })

        tvChangePhoto.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)

        }

        ivSave.setOnClickListener {
            loadingProgressBar.show(this, "Saving Profile Please Wait...")
            viewModel.saveEditedUser()

        }

        ivClose.setOnClickListener {
            finish()
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE) {
                // Get the url from data
                val selectedImage = data!!.data
                if (selectedImage != null) {
                    loadingProgressBar.show(this, "Uploading Image Please")
                    viewModel.onImageChange(selectedImage.toString())
                }
            }
        }
    }


    override fun setupObservers() {
        super.setupObservers()

        viewModel.nameField.observe(this, Observer {
            if (etEditName.text.toString() != it) etEditName.setText(it)
        })

        viewModel.tagLine.observe(this, Observer {
            if (etEditBio.text.toString() != it) etEditBio.setText(it)
        })

        viewModel.emailField.observe(this, Observer {
            tvEditEmail.setText(it)
        })

        viewModel.imageUrl.observe(this, Observer {
            it.run {
                val glideRequest = Glide
                    .with(ivAddProfile)
                    .load(it)
                    .apply(RequestOptions.centerCropTransform())
                    .apply(RequestOptions.placeholderOf(com.mindorks.bootcamp.instagram.R.drawable.ic_profile_add_pic))

                glideRequest.into(ivAddProfile)

            }

        })

        viewModel.updating.observe(this, Observer {
            if (it.first) loadingProgressBar.hide()
            val newIntent = Intent()
            newIntent.putExtra("User",it.second)
            setResult(Activity.RESULT_OK,newIntent)
            viewModel.onNameChange(etEditName.text.toString())
            viewModel.onBioChange(etEditBio.text.toString())
            finish()
        })

        viewModel.uploading.observe(this, Observer {
            if (it) loadingProgressBar.hide()
        })
    }


}


