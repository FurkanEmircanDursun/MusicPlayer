package com.furkandursun.musicplayerapp.model

import java.io.Serializable

data class Music(
    var baseCat: Long? = null,
    var title: String? = null,
    var url: String? = null,

    ):Serializable
