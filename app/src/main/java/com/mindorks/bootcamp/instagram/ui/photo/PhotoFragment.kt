package com.mindorks.bootcamp.instagram.ui.photo

import android.os.Bundle
import android.view.View
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.di.component.FragmentComponent
import com.mindorks.bootcamp.instagram.ui.base.BaseFragment

class PhotoFragment : BaseFragment<PhotoViewModel>() {

    companion object {
        const val TAG = "PhotoFragment"

        fun newInstance(): PhotoFragment {
            var args = Bundle()
            var fragment = PhotoFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun provideLayoutId(): Int = R.layout.fragment_photo

    override fun injectDependencies(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun setupView(view: View) {

    }

    override fun setupObservers() {
        super.setupObservers()
    }
}