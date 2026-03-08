package com.example.projectforkts.main.domain

import com.example.projectforkts.main.presentation.RepoItem


interface RepoRepository {
    fun getList(): Result<List<RepoItem>>
}