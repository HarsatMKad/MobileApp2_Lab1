package ViewModel

import Data.ApiWeather
import Data.RetrofitSitysApi
import Data.RetrofitWeatherApi
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherViewModel() : ViewModel() {
    var WeatherData : MutableLiveData<ApiWeather> = MutableLiveData()

    fun parsingFunction(sinyName: String) {
        var result: ApiWeather = ApiWeather(null, null, null, null, null)
        val retrofitWeather = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create()).build()

        val productApi = retrofitWeather.create(RetrofitSitysApi::class.java)

        val parsCorutin: Deferred<ApiWeather> = CoroutineScope(Dispatchers.Default).async {
            val sitys = productApi.getSitys(sinyName, "fa74a383f4eccf4d88a8f0397bbab87b")
            val weather = retrofitWeather.create(RetrofitWeatherApi::class.java)
            val weatherResult = weather.getWeather(sitys[0].lat, sitys[0].lon, "fa74a383f4eccf4d88a8f0397bbab87b")

            result = weatherResult
            return@async result
        }

        runBlocking {
            val SecondResult = parsCorutin.await()
            WeatherData.value = SecondResult
            return@runBlocking SecondResult
        }
    }
}