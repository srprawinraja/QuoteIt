package com.prawin.quoteit.worker

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.prawin.quoteit.R
import com.prawin.quoteit.data.firebase.FireStoreRepository
import com.prawin.quoteit.data.model.Quote
import com.prawin.quoteit.utils.DateHelper
import com.prawin.quoteit.utils.GsonHelper
import com.prawin.quoteit.utils.NotificationHelper
import com.prawin.quoteit.utils.SharedPreferenceHelper
import kotlinx.coroutines.CoroutineScope
import java.time.LocalDate
import java.time.LocalTime

class DailyNotificationWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val sharedPreferenceHelper = SharedPreferenceHelper(applicationContext)
        val gsonHelper = GsonHelper<Quote>()
        val todayDate: String = DateHelper.getDate()
        val yesterdayDate: String =  LocalDate.now().minusDays(1).toString()
        val key = "$todayDate streak"
        val streak = sharedPreferenceHelper.getValue(key = key)
        if(streak==null){
            // need to show quote
            if(/* yesterday date is cached then streak continous */){
                if(/* today quote cache*/){
                    val json = sharedPreferenceHelper.getValue(todayDate)
                    if(json != null){
                        val data:Quote = gsonHelper.getObj(json, Quote::class.java)
                        updateMarked(data.documentId)
                } else {
                    // today quote not available
                }
            } else {
                // new streak start

            }
            val quotes: List<Quote> = FireStoreRepository.getNRandomQuotes(1)
            val json = gsonHelper.getJson(quotes[0])
            sharedPreferenceHelper.save(todayDate, json)
            val notificationHelper = NotificationHelper(applicationContext)
            notificationHelper.showSimpleNotification("A Quote for You ✨", quotes[0].quote, 1)
        }

        return Result.success()
    }
}
