package com.mindorks.bootcamp.instagram.ui.home

import androidx.lifecycle.MutableLiveData
import com.mindorks.bootcamp.instagram.data.model.Post
import com.mindorks.bootcamp.instagram.data.model.User
import com.mindorks.bootcamp.instagram.data.repository.PostRepository
import com.mindorks.bootcamp.instagram.data.repository.UserRepository
import com.mindorks.bootcamp.instagram.ui.base.BaseViewModel
import com.mindorks.bootcamp.instagram.utils.common.Resource
import com.mindorks.bootcamp.instagram.utils.network.NetworkHelper
import com.mindorks.bootcamp.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers

class HomeViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val allPostList: ArrayList<Post>,
    private val paginator: PublishProcessor<Pair<String?, String?>>
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    val loading: MutableLiveData<Boolean> = MutableLiveData()

    val posts: MutableLiveData<Resource<List<Post>>> = MutableLiveData()

    val refreshPosts: MutableLiveData<Resource<List<Post>>> = MutableLiveData()

    private val user: User = userRepository.getCurrentUser()!!

    var firstPostId: String? = null

    var lastPostId: String? = null

    init {

        compositeDisposable.add(
            paginator
                .onBackpressureDrop()
                .doOnNext {
                    loading.postValue(true)
                }
                .concatMapSingle { pageIds ->
                    return@concatMapSingle postRepository
                        .fetchHomePostList(pageIds.first, pageIds.second, user)
                        .subscribeOn(Schedulers.io())
                        .doOnError {
                            handleNetworkError(it)
                        }

                }
                .subscribe({


                    allPostList.addAll(it)

                    firstPostId = allPostList.maxBy {
                        it.createdAt.time
                    }?.id

                    lastPostId = allPostList.minBy {
                        it.createdAt.time
                    }?.id
                    loading.postValue(false)
                    posts.postValue(Resource.success(it))
                },
                    {
                        handleNetworkError(it)
                    }
                )
        )
    }

    override fun onCreate() {
        loadMorePosts()
    }

    private fun loadMorePosts() {

        if (checkInternetConnectionWithMessage()) paginator.onNext(Pair(firstPostId, lastPostId))

    }

    fun onLoadMore() {
        if (loading.value !== null && loading.value == false) loadMorePosts()
    }

    fun onNewPost(post: Post) {
        allPostList.add(0, post)
        refreshPosts.postValue(Resource.success(mutableListOf<Post>().apply {
            addAll(allPostList)
        }))
    }
}