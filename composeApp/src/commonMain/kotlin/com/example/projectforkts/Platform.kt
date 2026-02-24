package com.example.projectforkts

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform