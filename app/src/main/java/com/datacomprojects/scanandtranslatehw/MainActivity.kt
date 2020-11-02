package com.datacomprojects.scanandtranslatehw

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.huawei.hms.mlsdk.common.MLApplication
import com.huawei.hms.mlsdk.common.MLException
import com.huawei.hms.mlsdk.translate.MLTranslateLanguage
import com.huawei.hms.mlsdk.translate.MLTranslatorFactory
import com.huawei.hms.mlsdk.translate.cloud.MLRemoteTranslateSetting
import com.huawei.hms.mlsdk.translate.cloud.MLRemoteTranslator
import com.otaliastudios.cameraview.CameraOptions
import darthkilersprojects.com.log.L
import datacomprojects.com.camerafocus.CameraPerformer
import datacomprojects.com.camerafocus.utils.CameraResultCallBack
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var cameraPerformer: CameraPerformer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MLApplication.getInstance().apiKey = "CV7rfgt+qOTXp8VRf1GCg8BN/itIF63ds4m3j1TMCiLB581LaA73II9N5+h10vQi6ghaL4FkkIQo9rYDeDU1kqo+XAyb"

        getOnlineTranslationList()

        HuaweiTranslate()
        //getOfflineTranslateList()

        cameraPerformer = CameraPerformer(this, this, this, null)
            .setCamera(camera)
            .setTakePicture(takePicture)
            .setFlashButton(flash)
            .setBrowseImageView(imp)
            .setTakeSnapshot(true)
            .setCameraResultCallBack(object : CameraResultCallBack() {
                override fun onImageSaved(filePath: String?, success: Boolean) {
                    super.onImageSaved(filePath, success)
                    //HuaweiOcr().offlineFromBitmap(BitmapFactory.decodeFile(filePath))
                    /*HuaweiOcr().onlineFromBitmap(BitmapFactory.decodeFile(filePath), object : HuaweiOcr.OnResult {
                        override fun onSuccess(text: String) {
                            HuaweiLanguageDetection().offlineDetectLanguage(text)
                            HuaweiLanguageDetection().onlineDetectLanguage(text)
                        }

                    })*/
                    //HuaweiDocument().start(BitmapFactory.decodeFile(filePath))

                    //HuaweiObject().start(BitmapFactory.decodeFile(filePath))

                }

                override fun onCameraOpened(options: CameraOptions) {
                    super.onCameraOpened(options)

                    //HuaweiOcr().forFrame(this@MainActivity)
                    //HuaweiObject().realTime(this@MainActivity)
                }
            })
            .build()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        cameraPerformer.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        cameraPerformer.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun getOnlineTranslationList() {

        MLTranslateLanguage.getCloudAllLanguages().addOnSuccessListener {
            L.show(it)
        }.addOnFailureListener {
            L.show(it.localizedMessage)
        }
    }

    private fun getOfflineTranslateList() {
        MLTranslateLanguage.getLocalAllLanguages().addOnSuccessListener {
            L.show(it)
        }
    }


}