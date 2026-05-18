package com.prawin.quoteit.api

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

object FireStoreInstance {
    val db = Firebase.firestore
}