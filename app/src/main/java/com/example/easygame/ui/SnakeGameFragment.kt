package com.example.easygame.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.easygame.databinding.FragmentSnakeGameBinding
import com.example.easygame.model.Direction

class SnakeGameFragment : Fragment() {
    private var _binding: FragmentSnakeGameBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSnakeGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.snakeGameFragment = this

        (activity as AppCompatActivity).supportActionBar?.title = "貪吃蛇"
    }

    fun changeDirection(direction: Direction) {
        binding.snakeGameView.changeDirection(direction)
    }
}