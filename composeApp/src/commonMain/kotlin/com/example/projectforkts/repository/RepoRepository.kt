package com.example.projectforkts.repository

import com.example.projectforkts.presentation.main.RepoItem

class RepoRepository {
    fun getList(): Result<List<RepoItem>> {
        return Result.success(
            List(20) { index ->
                RepoItem(
                    id = index.toLong(),
                    name = "Репозиторий $index",
                    description = "Описание для репозитория $index",
                    stars = index * 10,
                    owner = "Пользователь $index"
                )
            }
        )
    }
}