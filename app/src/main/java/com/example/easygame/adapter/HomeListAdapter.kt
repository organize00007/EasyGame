package com.example.easygame.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.easygame.R
import com.example.easygame.databinding.HomeListItemBinding
import com.example.easygame.ui.HomeFragment

class HomeListAdapter(private val homeFragment: HomeFragment) : RecyclerView.Adapter<HomeListAdapter.HomeListViewHolder>() {

    private val dataList = arrayListOf("貪吃蛇", "俄羅斯方塊")

    class HomeListViewHolder(private val binding: HomeListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(title: String, homeFragment: HomeFragment) {
            binding.funBtn.text = title
            binding.funBtn.setOnClickListener {
                when (title) {
                    "貪吃蛇" -> {
                        homeFragment.findNavController()
                            .navigate(R.id.action_homeFragment_to_snakeGameFragment)
                    }
                    "俄羅斯方塊" -> {
                        homeFragment.findNavController()
                            .navigate(R.id.action_homeFragment_to_tertrisFragment)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeListViewHolder {
        val binding = HomeListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeListViewHolder, position: Int) {
        holder.bind(dataList[position], homeFragment)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}
