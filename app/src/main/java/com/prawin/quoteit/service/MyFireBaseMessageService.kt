package com.prawin.quoteit.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val TAG = "firebase service"

    override fun onRegistered(installationId: String) {
        Log.d(TAG, "Registered installation ID: $installationId")

        // Send the Firebase Installation ID to your app server.
        //sendRegistrationToServer(installationId)
    }

}