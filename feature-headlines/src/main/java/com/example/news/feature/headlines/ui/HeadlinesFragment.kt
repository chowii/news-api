package com.example.news.feature.headlines.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.news.feature.articles.ui.ArticleActivity
import com.example.news.feature.headlines.databinding.FragmentHeadlinesBinding
import com.example.news.feature.headlines.presentation.HeadlineUiContract
import com.example.news.feature.headlines.presentation.HeadlinesViewModel
import com.example.news.shared.headlines.ArticleArgs
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

@AndroidEntryPoint
class HeadlinesFragment : Fragment() {

    private val headlinesViewModel: HeadlinesViewModel by viewModels()
    private val controller: HeadlineController by lazy { HeadlineController(headlinesViewModel) }
    private val disposables = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val headlinesBinding = FragmentHeadlinesBinding.inflate(inflater, container, false).apply {
            recyclerView.setController(controller)
            lifecycleOwner = viewLifecycleOwner
            viewModel = headlinesViewModel
            handler = headlinesViewModel
        }
        return headlinesBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        headlinesViewModel.viewState.observe(viewLifecycleOwner) { viewState ->
            viewState.data?.let {
                controller.setData(it)
            }
        }
    }

    private fun handleActions(actions: HeadlineUiContract.Actions) {
        when (actions) {
            is HeadlineUiContract.Actions.LaunchArticle -> {
                val articleArgs = ArticleArgs(actions.url, actions.title)
                val intent = Intent(requireContext(), ArticleActivity::class.java).apply {
                    putExtra(ArticleArgs.ARTICLE_ARGS_KEY, articleArgs)
                }
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        headlinesViewModel.fetchHeadlines()
        disposables += headlinesViewModel.uiActionsObservable.subscribe(::handleActions)
    }

    override fun onPause() {
        super.onPause()
        disposables.clear()
    }

    companion object {
        @JvmStatic
        fun newInstance() = HeadlinesFragment()
    }
}
