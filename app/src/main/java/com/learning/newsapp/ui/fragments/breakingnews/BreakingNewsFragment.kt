package com.learning.newsapp.ui.fragments.breakingnews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.learning.newsapp.R
import com.learning.newsapp.adapters.NewsAdapter
import com.learning.newsapp.databinding.FragmentBreakingNewsBinding
import com.learning.newsapp.models.Article
import com.learning.newsapp.utils.Resource
import org.koin.androidx.viewmodel.ext.android.viewModel


class BreakingNewsFragment : Fragment(), NewsAdapter.OnItemClickListener {
    private lateinit var binding: FragmentBreakingNewsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBreakingNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel: BreakingNewsViewModel by viewModel()

        val newsAdapter = NewsAdapter(this)
        binding.apply {

            recyclerBreakingNews.apply {
                adapter = newsAdapter
                layoutManager = LinearLayoutManager(activity)
            }

            viewModel.breakingNews.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Success -> {
                        breakingNewsProgressBar.isVisible = false
                        response.data?.let { newsResponse ->
                            newsAdapter.submitList(newsResponse.articles)
                        }
                    }
                    is Resource.Error -> {
                        breakingNewsProgressBar.isVisible = false
                        textViewError.isVisible = true
                        response.error?.let { message ->
                            textViewError.text = message
                        }
                    }
                    is Resource.Loading -> {
                        breakingNewsProgressBar.isVisible = true
                    }
                }
                breakingNewsSwipeToRefreshLayout.isRefreshing = false
            }

//            buttonLoadMore.setOnClickListener {
//                viewModel.loadMore()
//            }

            breakingNewsSwipeToRefreshLayout.setOnRefreshListener {
                viewModel.getBreakingNews()
            }
        }
    }

    override fun onItemClick(article: Article) {
        val bundle = Bundle().apply {
            putSerializable("article", article)
        }

        findNavController().navigate(
            R.id.action_breakingNewsFragment_to_articleFragment,
            bundle
        )
    }
}