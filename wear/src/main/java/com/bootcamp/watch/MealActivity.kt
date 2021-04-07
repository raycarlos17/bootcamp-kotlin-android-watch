package com.bootcamp.watch

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.wearable.activity.ConfirmationActivity
import android.util.Log
import com.example.shared.Meal
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.wearable.PutDataRequest
import com.google.android.gms.wearable.Wearable
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_meal.*


class MealActivity : Activity(), GoogleApiClient.ConnectionCallbacks {

    private lateinit var client: GoogleApiClient
    private var currentMeal: Meal? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal)

        client = GoogleApiClient.Builder(this).addConnectionCallbacks(this).addApi(Wearable.API).build()
        client.connect()

        star.setOnClickListener {
            sendLike()
        }
    }

    override fun onConnected(p0: Bundle?) {
        Wearable.MessageApi.addListener(client) { messageEvent ->
            currentMeal = Gson().fromJson(String(messageEvent.data), Meal::class.java)
            updateView()
        }
    }

    override fun onConnectionSuspended(p0: Int) {
        Log.w("Wear", "Google api Client connection suspended!")
    }

    private fun updateView() {
        currentMeal?.let {
            mealTitle.text = it.title
            calories.text = getString(R.string.calories, it.calories)
            ingredients.text = it.ingredients.joinToString(separator = ", ")
        }
    }

    private fun sendLike() {
        currentMeal?.let {
            val bytes = Gson().toJson(it.copy(favorited = true)).toByteArray()
            Wearable.DataApi.putDataItem(client, PutDataRequest.create("/liked").setData(bytes).setUrgent()).setResultCallback {
                showConfirmationScreen()
            }
        }
    }

    private fun showConfirmationScreen() {
        val intent = Intent(this, ConfirmationActivity::class.java)
        intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, ConfirmationActivity.SUCCESS_ANIMATION)
        intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE, getString(R.string.starred_meal))
        startActivity(intent)
    }
}
