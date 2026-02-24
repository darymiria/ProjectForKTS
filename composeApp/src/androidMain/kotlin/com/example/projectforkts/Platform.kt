package com.example.projectforkts


import android.os.Build

actual fun getPlatformName(): String = "Android ${Build.VERSION.SDK_INT}"