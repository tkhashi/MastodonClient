package io.keiji.sample.mastodonclient.ui.edit

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import io.keiji.sample.mastodonclient.entity.LocalMedia
import io.keiji.sample.mastodonclient.repository.MediaFileRepository
import io.keiji.sample.mastodonclient.repository.TootRepository
import io.keiji.sample.mastodonclient.repository.UserCredentialRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection
import javax.xml.transform.OutputKeys.MEDIA_TYPE
import kotlin.math.log

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
    val mediaFileRepository = MediaFileRepository(application)
    val loginRequired = MutableLiveData<Boolean>()

    val postComplete = MutableLiveData<Boolean>()
    val errorMessasge = MutableLiveData<String>()

    fun postToot(){
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

            try{
                val uploadedMediaIds = mediaAttachments.value?.map {
                    tootRepository.postMedia(it.file, it.mediaType)
                }?.map {
                    it.id
                }

                println(uploadedMediaIds?.first())
                tootRepository.postToot(
                    statusSnapshot,
                    uploadedMediaIds
                )
                postComplete.postValue(true)
            } catch (e: HttpException){
                when (e.code()){
                    HttpURLConnection.HTTP_FORBIDDEN -> {
                        errorMessasge.postValue("必要な権限がありません")
                    }
                }
            } catch (e : IOException){
                errorMessasge.postValue(
                    "サーバーに接続できませんでした。 ${e.message}"
                )
            }
        }
    }

    val mediaAttachments = MutableLiveData<ArrayList<LocalMedia>>()

    fun addMedia(mediaUri: Uri){
        coroutineScope.launch {
            try {
                val bitmap = mediaFileRepository.readBitmap(mediaUri)
                val tempFile = mediaFileRepository.saveBitmap(bitmap)

                val newMediaAttachments = ArrayList<LocalMedia>()
                mediaAttachments.value?.also{
                    newMediaAttachments.addAll(it)
                }
                newMediaAttachments.add(LocalMedia(tempFile, MEDIA_TYPE))
                mediaAttachments.postValue(newMediaAttachments)
            }catch (e: IOException){
                handleMediaException(mediaUri, e)
            }
        }
    }

    private fun handleMediaException(mediaUri: Uri, e: IOException) {
        errorMessasge.postValue("メディアを読み込めません ${e.message} ${mediaUri}")
    }
}