package com.apps.firewallpapers

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Created by abhinav on 2/6/20.
 */

class WallpapersViewModel: ViewModel(){

    private val TAG = "View_Model_Log"
    private val firebaseRepository = FirebaseRepository()
    private val wallpapersList: MutableLiveData<List<WallpapersModel>> by lazy {
        MutableLiveData<List<WallpapersModel>>().also {
            loadWallpapersData()
        }
    }

    fun getWallpaperList(): LiveData<List<WallpapersModel>>{
        return wallpapersList
    }


    fun loadWallpapersData(){

        //Query Data from repository

        firebaseRepository.queryWallpapers().addOnCompleteListener {
            if(it.isSuccessful){

                val result = it.result

                if(result!!.isEmpty){
                    //No data left to load
                }else{
                    //Results are ready to load
                    wallpapersList.value = result.toObjects(WallpapersModel::class.java)
                }

            }else{
                Log.d(TAG,"Error ${it.exception!!.message}")
            }
        }

    }

}