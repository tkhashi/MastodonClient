package io.keiji.sample.mastodonclient.ui.edit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import io.keiji.sample.mastodonclient.repository.TootRepository
import io.keiji.sample.mastodonclient.repository.UserCredentialRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class TootEditViewModel(
    private val instanceUrl: String,
    private val username: String,
    private val coroutineScope: CoroutineScope,
    application: Application
) : AndroidViewModel(application){

    private val userCredentialRepository = UserCredentialRepository(
        application
    )

    var status = MutableLiveData<String>()
    val loginRequired = MutableLiveData<Boolean>()

    val postComplete = MutableLiveData<Boolean>()
    val errorMessasge = MutableLiveData<String>()

    fun postToot(){
        println("status: ${status}")
        println("status.value: ${status.value}")
        val statusSnapshot = status.value ?: return
        if (statusSnapshot.isBlank()){
            errorMessasge.postValue("投稿内容がありません")
            return
        }

        coroutineScope.launch {
            val credential = userCredentialRepository.find(instanceUrl, username)
            if (credential == null){
                loginRequired.postValue(true)
                return@launch
            }

            val tootRepository = TootRepository(credential)
            tootRepository.postToot(
                statusSnapshot
            )
            postComplete.postValue(true)
        }
    }
}