package com.mindorks.bootcamp.instagram.ui.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.di.component.FragmentComponent
import com.mindorks.bootcamp.instagram.ui.base.BaseFragment
import com.mindorks.bootcamp.instagram.ui.home.posts.PostsAdapter
import com.mindorks.bootcamp.instagram.ui.main.MainSharedViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

class HomeFragment : BaseFragment<HomeViewModel>() {

    companion object {
        const val TAG = "HomeFragment"

        fun newInstance(): HomeFragment {
            val args = Bundle()
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun provideLayoutId(): Int = R.layout.fragment_home

    override fun injectDependencies(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    @Inject
    lateinit var mainSharedViewModel: MainSharedViewModel

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager

    @Inject
    lateinit var postsAdapter: PostsAdapter

    override fun setupView(view: View) {

        rvPosts.apply {
            layoutManager = linearLayoutManager
            adapter = postsAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    layoutManager?.run {
                        if (this is LinearLayoutManager &&
                            itemCount > 0 &&
                            itemCount == findLastVisibleItemPosition()
                        )
                            viewModel.onLoadMore()
                    }
                }
            })
        }

    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.loading.observe(this, Observer {
            pb_loading.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.posts.observe(this, Observer {
            it.data?.run {
                postsAdapter.appendData(this)
            }
        })

        mainSharedViewModel.newPost.observe(this, Observer {
            it.getIfNotHandled()?.run {
                viewModel.onNewPost(this)

            }
        })

        viewModel.refreshPosts.observe(this, Observer {
            it.data?.run {
                postsAdapter.updateList(this)
                rvPosts.scrollToPosition(0)
            }
        })
    }


}