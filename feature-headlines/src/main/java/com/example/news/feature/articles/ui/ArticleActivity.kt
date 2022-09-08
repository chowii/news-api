package com.example.news.feature.articles.ui

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.news.feature.articles.ArticleViewModel
import com.example.news.feature.headlines.R
import com.example.news.feature.headlines.databinding.ActivityArticleBinding
import com.example.news.shared.headlines.ArticleArgs
import com.example.news.shared.headlines.ArticleArgs.Companion.ARTICLE_ARGS_KEY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.parcel.Parcelize

@AndroidEntryPoint
class ArticleActivity : AppCompatActivity() {

    private lateinit var activityArticleBinding: ActivityArticleBinding
    private val articleViewModel: ArticleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = getArgs()
        activityArticleBinding = DataBindingUtil.setContentView(this, R.layout.activity_article)
        activityArticleBinding.apply {
            lifecycleOwner = this@ArticleActivity
            viewModel = articleViewModel
            clickListener = articleViewModel
        }
        articleViewModel.fetchArticleSaved(args.title, args.url)
        articleViewModel.viewState.observe(this) { viewState ->
            activityArticleBinding.articleWebView.loadUrl(viewState.url)
            activityArticleBinding.articleSave.isChecked = viewState.isArticleSaved

        }
    }

    private fun getArgs(): ArticleArgs {
        val args = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(ARTICLE_ARGS_KEY)
        } else {
            intent.getParcelableExtra(ARTICLE_ARGS_KEY, ArticleArgs::class.java)
        }
        return args ?: throw Throwable("Article Args not found. Article cannot be launched")
    }
}
