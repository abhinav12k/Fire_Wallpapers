package com.apps.firewallpapers

import com.google.firebase.Timestamp

/**
 * Created by abhinav on 2/6/20.
 */

data class WallpapersModel(

    val name:String = "",
    val image:String = "",
    val thumbnail:String = "",
    val date:Timestamp? = null

)