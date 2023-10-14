package io.keiji.sample.mastodonclient.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope

class LoginViewModel(
    private val instanceUrl: String,
    private val coroutineScope: CoroutineScope,
    application: Application
) : AndroidViewModel(application) {
}