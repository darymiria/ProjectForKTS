package com.example.projectforkts.main.data

import com.example.projectforkts.main.domain.RepoRepository
import com.example.projectforkts.main.presentation.RepoItem

class RepoRepositoryImpl: RepoRepository {
    override fun getList(): Result<List<RepoItem>> {
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