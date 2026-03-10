package com.example.projectforkts.login.domain

interface LoginRepository{
    fun login(username: String, password: String): Result<Unit>
}