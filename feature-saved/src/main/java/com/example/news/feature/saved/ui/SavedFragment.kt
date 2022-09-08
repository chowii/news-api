package com.example.news.feature.saved.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyRecyclerView
import com.airbnb.epoxy.EpoxyViewHolder
import com.example.news.feature.saved.ItemSavedBindingModel_
import com.example.news.feature.saved.SavedViewModel
import com.example.news.feature.saved.databinding.FragmentSavedBinding
import com.example.news.shared.headlines.ArticleArgs
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

@AndroidEntryPoint
class SavedFragment : Fragment() {

    private val controller: SavedController by lazy { SavedController(viewModel) }
    private val viewModel: SavedViewModel by viewModels()
    private val disposables = CompositeDisposable()
    private lateinit var savedBinding: FragmentSavedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        savedBinding = FragmentSavedBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            recyclerView.setController(controller)
            setupSwipeToDeleteGesture(recyclerView)
            viewModel = this@SavedFragment.viewModel
            handler = this@SavedFragment.viewModel
        }
        return savedBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.viewState.observe(viewLifecycleOwner) { viewState ->
            viewState.data.let(controller::setData)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getSavedArticles()
        disposables += viewModel.actionsObservable.subscribe { actions ->
            when (actions) {
                is SavedUiContract.Actions.LaunchArticle -> launchArticle(
                    actions.title,
                    actions.url
                )
            }
        }
    }

    override fun onPause() {
        super.onPause()
        disposables.clear()
    }

    private fun launchArticle(title: String, url: String) {
        Intent(Intent.ACTION_VIEW)
            .setClassName(
                requireContext().packageName,
                requireContext().packageName.plus(".feature.articles.ui.ArticleActivity")
            )
            .apply {
                putExtra(ArticleArgs.ARTICLE_ARGS_KEY, ArticleArgs(url, title))
            }
            .let { startActivity(it) }
    }

    private fun setupSwipeToDeleteGesture(recyclerView: EpoxyRecyclerView) {
        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(ItemTouchHelper.ACTION_STATE_IDLE, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val epoxyViewHolder = viewHolder as EpoxyViewHolder
                val title = (epoxyViewHolder.model as ItemSavedBindingModel_).data().title
                viewModel.removeSavedArticle(title)
            }
        }).attachToRecyclerView(recyclerView)
    }

    companion object {
        fun newInstance() = SavedFragment()
    }
}
