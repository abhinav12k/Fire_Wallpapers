package com.apps.firewallpapers

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

/**
 * Created by abhinav on 2/6/20.
 */

//This class is for loading all wallpapers and their thumbnails

class FirebaseRepository{

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    //For implementing pagination
    var lastVisible : DocumentSnapshot? =null
    private val pageSize: Long = 6

    // function to check whether the user is logged in/not
    fun getUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    fun queryWallpapers(): Task<QuerySnapshot> {

        if(lastVisible==null){
            //quering for the first time
            //Load first page
            return firebaseFirestore
                .collection("Wallpapers")
                .orderBy("date", Query.Direction.DESCENDING)
                .limit(9)
                .get()
        }else{
            //Load next page
            return firebaseFirestore
                .collection("Wallpapers")
                .orderBy("date",Query.Direction.DESCENDING)
                .startAfter(lastVisible!!)
                .limit(pageSize).
                get()
        }

    }

}
