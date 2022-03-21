package com.learning.newsapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.learning.newsapp.databinding.FragmentArticleBinding
import com.learning.newsapp.ui.fragments.savednews.SavedNewsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class ArticleFragment : Fragment() {

    private lateinit var binding: FragmentArticleBinding
    private val args: ArticleFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val article = args.article
        val viewModel: SavedNewsViewModel by viewModel()
        binding.apply {

            webView.apply {
                webViewClient = WebViewClient()
                article.url?.let { loadUrl(it) }
            }
            fabSaveArticle.setOnClickListener {
                viewModel.insertArticle(article)
                Snackbar.make(view, "article saved successfully", Snackbar.LENGTH_LONG).show()
            }

        }
    }
}