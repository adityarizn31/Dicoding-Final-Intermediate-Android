package com.dicoding.sub1_appsstory.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.sub1_appsstory.databinding.ItemLoadingBinding

class LoadingStateAdapter(private val retry:() -> Unit) : LoadStateAdapter<LoadingStateAdapter.LoadStateViewHolder>() {

    class LoadStateViewHolder(private val binding: ItemLoadingBinding, retry: () -> Unit):
    RecyclerView.ViewHolder(binding.root){

        init {
            binding.buttonRetry.setOnClickListener { retry.invoke()

            }
        }

        fun bind(loadStateView: LoadState) {
            if (loadStateView is LoadState.Error) {
                binding.errorMsg.text = loadStateView.error.localizedMessage
            }
            binding.progressBar.isVisible = loadStateView is LoadState.Loading
            binding.buttonRetry.isVisible = loadStateView is LoadState.Error
            binding.errorMsg.isVisible = loadStateView is LoadState.Error
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, loadStateView: LoadState): LoadStateViewHolder {
        val binding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadStateViewHolder(binding, retry)
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadStateView: LoadState) {
        holder.bind(loadStateView)
    }

}