package com.example.myweather.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myweather.R
import com.example.myweather.databinding.FragmentMainBinding
import com.example.myweather.databinding.OneHourBinding
import com.example.myweather.network.models.maininfo.forecastday.day.OneDay

class WeatherAdapter: ListAdapter<OneDay, WeatherAdapter.WeatherDayHolder>(DiffCallback) {

    class WeatherDayHolder(private var binding: OneHourBinding): RecyclerView.ViewHolder(binding.root) {

        fun init(oneDay: OneDay) {
            //TODO to inflate the layout with the object
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherDayHolder {
        return WeatherDayHolder(OneHourBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: WeatherDayHolder, position: Int) {

        val oneDay = getItem(position)

        holder.init(oneDay)
    }

    object DiffCallback: DiffUtil.ItemCallback<OneDay>() {

        override fun areItemsTheSame(oldItem: OneDay, newItem: OneDay): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: OneDay, newItem: OneDay): Boolean {
            return oldItem.dayWeather.avgTemp == newItem.dayWeather.avgTemp
        }

    }

}