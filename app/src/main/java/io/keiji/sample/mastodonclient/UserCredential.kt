package io.keiji.sample.mastodonclient

data class UserCredential (
    val instanceUrl: String,
    val username: String? = null,
    val accessToken: String? = null
)