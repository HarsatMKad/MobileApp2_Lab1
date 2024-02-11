package com.example.myapplication

import Data.ApiWeather
import Data.SityData
import Data.sitys
import ViewModel.WeatherViewModel
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.squareup.picasso.Picasso
import java.math.RoundingMode

//sity api: http://api.openweathermap.org/geo/1.0/direct?q=London&limit=5&appid=fa74a383f4eccf4d88a8f0397bbab87b
//weather api: https://api.openweathermap.org/data/2.5/forecast?lat=54.2021736&lon=30.2964015&appid=fa74a383f4eccf4d88a8f0397bbab87b

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        val WeatherModel = ViewModelProvider(this)[WeatherViewModel::class.java]

        val inputText = findViewById<EditText>(R.id.editTextText)
        val button = findViewById<Button>(R.id.button)
        val sityText = findViewById<TextView>(R.id.textView3)
        val weatherText = findViewById<TextView>(R.id.textView4)
        val descripText = findViewById<TextView>(R.id.textView5)
        val weatherIcon = findViewById<ImageView>(R.id.imageView)

        button.setOnClickListener{
            WeatherModel.parsingFunction(inputText.text.toString())
        }

        WeatherModel.WeatherData.observe(this, Observer<ApiWeather>{
                value: ApiWeather ->
            sityText.text = "Город  " + value.city?.name
            weatherText.text = "Погода  " + value.list?.get(0)?.main?.temp?.minus(272.15)
                ?.toBigDecimal()?.setScale(2,RoundingMode.UP).toString()
            descripText.text = "Описание  " + (value.list?.get(0)?.weather?.get(0)?.description)
            val icnURL = "https://openweathermap.org/img/w/" + (value.list?.get(0)?.weather?.get(0)?.icon) + ".png"
            Picasso.get().load(icnURL).into(weatherIcon)
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val sityText = findViewById<EditText>(R.id.editTextText)
        outState.putString("sity", sityText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val sityText = findViewById<EditText>(R.id.editTextText)
        val sityname: String = savedInstanceState.getString("sity").toString()
        sityText.setText(sityname)
    }
}
