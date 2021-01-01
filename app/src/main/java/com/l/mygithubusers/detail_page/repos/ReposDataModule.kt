package com.l.mygithubusers.detail_page.repos

data class ReposDataModule (
    val repoName : String = "",
    val createdAt : String = "",
    val updatedAt : String = "",
    val pushedAt : String = "",
    val fork : Int = 0,
    val language : String = "",
    val watched : Int = 0
)