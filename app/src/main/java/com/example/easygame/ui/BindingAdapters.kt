package com.example.easygame.ui

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.easygame.adapter.HomeListAdapter

@BindingAdapter("adapter")
fun homeListAdapterBinding(recyclerView: RecyclerView, homeListAdapter: HomeListAdapter?) {
    homeListAdapter.let {
        recyclerView.adapter = it
    }
}