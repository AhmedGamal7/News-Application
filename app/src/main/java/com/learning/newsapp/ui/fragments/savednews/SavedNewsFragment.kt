package com.learning.newsapp.ui.fragments.savednews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.learning.newsapp.R
import com.learning.newsapp.adapters.NewsAdapter
import com.learning.newsapp.databinding.FragmentSavedNewsBinding
import com.learning.newsapp.models.Article
import org.koin.androidx.viewmodel.ext.android.viewModel


class SavedNewsFragment : Fragment(), NewsAdapter.OnItemClickListener {

    private lateinit var binding: FragmentSavedNewsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSavedNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val newsAdapter = NewsAdapter(this)
        val viewModel: SavedNewsViewModel by viewModel()

        val itemSwipe = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val selectedArticle = newsAdapter.currentList[position]
                viewModel.deleteArticle(selectedArticle)
                Snackbar.make(view, "Article removed", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        viewModel.insertArticle(selectedArticle)
                    }
                    show()
                }
            }

        }

        binding.apply {
            recyclerSavedNews.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = newsAdapter
            }

            viewModel.articles.observe(viewLifecycleOwner) { articles ->
                if (articles.isEmpty()) {
                    textViewNoSavedNews.isVisible = true
                } else {
                    textViewNoSavedNews.isVisible = false
                    newsAdapter.submitList(articles)
                }
            }

            ItemTouchHelper(itemSwipe).apply {
                attachToRecyclerView(recyclerSavedNews)
            }

        }
    }

    override fun onItemClick(article: Article) {
        val bundle = Bundle().apply {
            putSerializable("article", article)
        }

        findNavController().navigate(
            R.id.action_savedNewsFragment_to_articleFragment,
            bundle
        )
    }
}