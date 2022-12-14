package com.example.myweather.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.myweather.R
import com.example.myweather.databinding.OneHourBinding
import com.example.myweather.network.model.maininfo.forecastday.day.hour.OneHour
import kotlin.math.roundToInt

class WeatherAdapter: ListAdapter<OneHour, WeatherAdapter.WeatherHourHolder>(DiffCallback) {

    class WeatherHourHolder(view: View): RecyclerView.ViewHolder(view) {

        private val binding = OneHourBinding.bind(view)

        fun init(oneHour: OneHour) {
            binding.hourImage.load(oneHour.condition.hourWeatherIcon.toUri().buildUpon().scheme("https").build())
            binding.hourDegree.text = "${oneHour.temp.roundToInt()}°C"
            binding.hourTime.text = oneHour.time.substringAfterLast(' ')
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherHourHolder {
        return WeatherHourHolder(LayoutInflater.from(parent.context).inflate(R.layout.one_hour,parent,false))
    }

    override fun onBindViewHolder(holder: WeatherHourHolder, position: Int) {

        val oneHour = getItem(position)

        holder.init(oneHour)
    }

    object DiffCallback: DiffUtil.ItemCallback<OneHour>() {

        override fun areItemsTheSame(oldItem: OneHour, newItem: OneHour): Boolean {
            return oldItem.time == newItem.time
        }

        override fun areContentsTheSame(oldItem: OneHour, newItem: OneHour): Boolean {
            return oldItem.temp == newItem.temp
        }

    }

}