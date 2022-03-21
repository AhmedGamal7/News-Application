package com.learning.newsapp.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.learning.newsapp.databinding.RecyclerRowBinding
import com.learning.newsapp.models.Article

class NewsAdapter(private val listener: OnItemClickListener) :
    ListAdapter<Article, NewsAdapter.ViewHolder>(NewsComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = getItem(position)
        if (article != null) {
            holder.bind(article)
        }
    }

    inner class ViewHolder(private val binding: RecyclerRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.onItemClick(item)
                    }
                }
            }
        }

        fun bind(article: Article) {
            binding.apply {
                Glide.with(itemView).load(article.urlToImage).into(imageViewArticleImage)
                textViewArticleTitle.text = article.title
                textViewArticleSource.text = article.source.name
                textViewArticleDate.text = article.publishedAt
                textViewArticleDescription.text = article.description
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(article: Article)
    }

    class NewsComparator : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article) =
            oldItem.url == newItem.url

        override fun areContentsTheSame(oldItem: Article, newItem: Article) =
            oldItem == newItem
    }

}