package com.example.thechance_firsttask_seasontwo

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import com.example.thechance_firsttask_seasontwo.databinding.ActivityMainBinding
import com.example.thechance_firsttask_seasontwo.model.Weather
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val client = OkHttpClient()

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.btnSearch.setOnClickListener {
            makeRequestUsingOkHttp(binding.searchHomeItem.query.toString())
        }


    }


    private fun makeRequestUsingOkHttp(countryName: String? = "syria") {
        val request = Request.Builder()
            .url("https://api.openweathermap.org/data/2.5/weather?q=$countryName&appid=8597c19a2987ddeedaef98f1a4238194&units=metric")
            .build()

        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                Log.d("TAG:(", e.message.toString())
            }

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let { jsonString ->
                    val res = Gson().fromJson(jsonString, Weather::class.java)
                    runOnUiThread {
                        binding.apply {

                            txtTemperatureDegree.text = res.main.temp
                            txtTemperatureState.text = res.weather[0].description
                            txtDayTemperature.text = res.main.temp_max
                            txtNightTemperature.text = res.main.temp_min
                            txtVisibilityCount.text = (res.visibility.toInt() / 100).toString()
                            txtHumidityCount.text = res.main.humidity + "%"
                            txtCloudCount.text = res.clouds.all + "%"
                            txtWindCount.text = res.wind.speed
                            txtPressureCount.text = res.main.pressure

                            when {
                                res.clouds.all.toInt() in 1..19 -> {
                                    lottieMainImage.setAnimation(R.raw.sun)
                                    lottieMainImage.playAnimation()
                                }
                                res.clouds.all.toInt() in 20..49 -> {
                                    lottieMainImage.setAnimation(R.raw.cloudy)
                                    lottieMainImage.playAnimation()
                                }
                                res.clouds.all.toInt() in 50..79 -> {
                                    lottieMainImage.setAnimation(R.raw.cloud_and_rain)
                                    lottieMainImage.playAnimation()
                                }
                                res.clouds.all.toInt() in 80..100 -> {
                                    lottieMainImage.setAnimation(R.raw.winter)
                                    lottieMainImage.playAnimation()
                                }
                            }
                        }
                    }
                }
            }
        })
    }

    init {
        makeRequestUsingOkHttp()
    }
}