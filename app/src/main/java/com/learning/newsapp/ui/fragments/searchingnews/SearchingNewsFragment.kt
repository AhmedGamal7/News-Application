package com.learning.newsapp.ui.fragments.searchingnews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.learning.newsapp.R
import com.learning.newsapp.adapters.NewsAdapter
import com.learning.newsapp.databinding.FragmentSearchNewsBinding
import com.learning.newsapp.models.Article
import com.learning.newsapp.utils.Constant.Companion.API_CALL_DELAY
import com.learning.newsapp.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchingNewsFragment : Fragment(), NewsAdapter.OnItemClickListener {
    private lateinit var binding: FragmentSearchNewsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel: SearchingNewsViewModel by viewModel()
        val newsAdapter = NewsAdapter(this)

        binding.apply {

            var job: Job? = null
            editTextSearch.addTextChangedListener { editable ->
                job?.cancel()
                job = MainScope().launch {
                    delay(API_CALL_DELAY)
                    editable?.let {
                        if (editable.toString().isNotEmpty()) {
                            viewModel.getSearchingNews(editable.toString())
                        }
                    }
                }

            }

            recyclerSearchingNews.apply {
                adapter = newsAdapter
                layoutManager = LinearLayoutManager(activity)
            }

            viewModel.searchNews.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Success -> {
                        searchingNewsProgressBar.isVisible = false
                        response.data?.let { newsResponse ->
                            newsAdapter.submitList(newsResponse.articles)
                        }
                    }
                    is Resource.Error -> {
                        searchingNewsProgressBar.isVisible = false
                        searchingNewsTextViewError.isVisible = true
                        response.error?.let { message ->
                            searchingNewsTextViewError.text = message
                        }
                    }
                    is Resource.Loading -> {
                        searchingNewsProgressBar.isVisible = true
                    }
                }
            }
        }
    }

    override fun onItemClick(article: Article) {
        val bundle = Bundle().apply {
            putSerializable("article", article)
        }
        findNavController().navigate(
            R.id.action_searchNewsFragment_to_articleFragment,
            bundle
        )
    }
}