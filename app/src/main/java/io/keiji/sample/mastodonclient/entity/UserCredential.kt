package io.keiji.sample.mastodonclient.entity

data class UserCredential (
    val instanceUrl: String,
    val username: String? = null,
    val accessToken: String? = null
)