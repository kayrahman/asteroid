package com.udacity.asteroidradar.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity) {}
        ViewModelProvider(this,MainViewModel.Factory(activity.application)).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setHasOptionsMenu(true)

        observeViewModel(binding)

        return binding.root
    }

    private fun observeViewModel(binding: FragmentMainBinding) {

        val adapter = AsteroidRecyclerAdapter(AsteroidRecyclerAdapter.OnAsteroidItemClickListener {
            viewModel.displayAsteroidDetail(it)
        })

        binding.asteroidRecycler.adapter = adapter

        viewModel.asteroids.observe(
            viewLifecycleOwner, Observer{
                it.let {
                    adapter.submitAsteroidList(it)
                    Log.d("total_asteroid",it.size.toString())
                }
            }
        )

        viewModel.picOfDay.observe(
                viewLifecycleOwner, Observer {

               if (null != it){
                binding.apod = it
                }

        }
        )

        viewModel.navigateToSelectedAsteroidDetail.observe(viewLifecycleOwner, Observer {
            if(null != it) {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.displayAsteroidDetailComplete()
            }
        })


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
      return when(item.itemId){
            R.id.show_next_week_menu -> {
                updateAsteroidData(FetchType.WEEKLY)
                true
            }

            R.id.show_today_menu -> {
                updateAsteroidData(FetchType.TODAY)
                true
            }

            R.id.show_saved_menu ->{
                updateAsteroidData(FetchType.ALL)
                true
            }

          else -> super.onOptionsItemSelected(item)

        }

        //return true


    }

    enum class FetchType {
        TODAY,WEEKLY,ALL
    }

    private fun updateAsteroidData(type : FetchType) {
        with(viewModel) {
            when (type) {
                FetchType.TODAY -> updateAsteroidDataType(FetchType.TODAY.name)
                FetchType.WEEKLY -> updateAsteroidDataType(FetchType.WEEKLY.name)
                FetchType.ALL -> updateAsteroidDataType(FetchType.ALL.name)
            }
        }
    }
}
