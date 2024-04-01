package com.example.whatsapp1

import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage



class MyFirebaseMessagingService : FirebaseMessagingService() {



    fun generateNotification(){
        val intent = Intent(this,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingintent = PendingIntent.getActivity(this , 0 , intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)


//        var builder : NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, channelId)



    }
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
       Log.d("remotemsg", "$remoteMessage")

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }



}
