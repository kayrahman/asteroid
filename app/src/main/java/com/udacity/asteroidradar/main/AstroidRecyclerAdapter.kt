package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.databinding.ListItemAsteoridBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AstroidRecyclerAdapter : ListAdapter<Asteroid, AstroidRecyclerAdapter.AsteroidViewHolder>(AsteroidDiffCallback()) {


    private val adapterScope = CoroutineScope(Dispatchers.Default)


    fun submitAsteroidList(list: List<Asteroid>?) {

        adapterScope.launch {
            /*val items = when (list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map { DataItem.SleepNightItem(it) }
            }*/
            withContext(Dispatchers.Main) {
                submitList(list)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        return AsteroidViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val asteroid = getItem(position) as Asteroid
        holder.bind(asteroid)
    }


    class AsteroidDiffCallback : DiffUtil.ItemCallback<Asteroid>(){
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
           return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }

    }


    class AsteroidViewHolder private constructor(val binding: ListItemAsteoridBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : Asteroid){
            binding.asteroid = item
            binding.executePendingBindings()
        }


        companion object {
            fun from (parent: ViewGroup): AsteroidViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemAsteoridBinding.inflate(layoutInflater,parent,false)
                return AsteroidViewHolder(binding)
            }

        }

    }



}