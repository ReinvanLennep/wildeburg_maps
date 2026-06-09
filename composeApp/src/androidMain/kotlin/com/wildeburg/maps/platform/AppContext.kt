package com.wildeburg.maps.platform

import android.content.Context

object AppContext {
    private lateinit var ctx: Context
    fun init(context: Context) { ctx = context.applicationContext }
    fun get(): Context = ctx
}
