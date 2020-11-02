package com.datacomprojects.scanandtranslatehw

import android.content.Context
import android.graphics.Bitmap
import android.util.SparseArray
import android.view.SurfaceView
import android.view.TextureView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.forEach
import androidx.core.util.valueIterator
import com.huawei.hmf.tasks.Task
import com.huawei.hms.mlsdk.MLAnalyzerFactory
import com.huawei.hms.mlsdk.common.LensEngine
import com.huawei.hms.mlsdk.common.MLAnalyzer
import com.huawei.hms.mlsdk.common.MLException
import com.huawei.hms.mlsdk.common.MLFrame
import com.huawei.hms.mlsdk.text.MLLocalTextSetting
import com.huawei.hms.mlsdk.text.MLRemoteTextSetting
import com.huawei.hms.mlsdk.text.MLText
import com.huawei.hms.mlsdk.text.MLTextAnalyzer
import com.otaliastudios.cameraview.CameraView
import darthkilersprojects.com.log.L


class HuaweiOcr {

    private lateinit var analyzer: MLTextAnalyzer


    fun offlineFromBitmap(bitmap: Bitmap) {
        val setting = MLLocalTextSetting.Factory()
            .setOCRMode(MLLocalTextSetting.OCR_DETECT_MODE)
            .setLanguage("йцу")
            .create()
        analyzer = MLAnalyzerFactory.getInstance().getLocalTextAnalyzer(setting)

        val frame = MLFrame.fromBitmap(bitmap)

        val task: Task<MLText> = analyzer.asyncAnalyseFrame(frame)
        task.addOnSuccessListener {
            L.show(it.stringValue)
            stop()
            it.blocks.forEach { L.show(it.language, it.stringValue) }
        }.addOnFailureListener {
            L.show(it)
            stop()
        }
    }

    fun onlineFromBitmap(bitmap: Bitmap, onResult: OnResult) {
        val languageList: MutableList<String> = ArrayList()
        //languageList.add("qwe")
        //languageList.add("ru")
        val setting = MLRemoteTextSetting.Factory()
            .setTextDensityScene(MLRemoteTextSetting.OCR_LOOSE_SCENE)
            .setLanguageList(languageList)
            .setBorderType(MLRemoteTextSetting.ARC)
            .create()
        analyzer = MLAnalyzerFactory.getInstance().getRemoteTextAnalyzer(setting)

        val frame = MLFrame.fromBitmap(bitmap)

        val task = analyzer.asyncAnalyseFrame(frame)
        task.addOnSuccessListener {
            L.show(it.stringValue)
            onResult.onSuccess(it.stringValue)
            L.show("---------------------------")
            stop()
            it.blocks.forEach { L.show(it.language, it.stringValue) }
        }
            .addOnFailureListener {
                stop()
                L.show(it)
                try {
                    val mlException = it as MLException
                    L.show(mlException)
                    val errorCode = mlException.errCode
                    L.show(errorCode)
                    val errorMessage = mlException.message
                    L.show(errorMessage)
                } catch (error: Exception) {
                    L.show(error)
                }
        }
    }

    fun forFrame(context: Context) {
        analyzer = MLTextAnalyzer.Factory(context).create()
        analyzer.setTransactor(object : MLAnalyzer.MLTransactor<MLText.Block> {
            override fun destroy() {
                TODO("Not yet implemented")
            }

            override fun transactResult(p0: MLAnalyzer.Result<MLText.Block>?) {
                val items: SparseArray<MLText.Block>? = p0?.analyseList
                items?.valueIterator()?.forEach { L.show(it.language, it.stringValue) }
            }

        })

        val lensEngine = LensEngine.Creator(context, analyzer)
            .setLensType(LensEngine.BACK_LENS)
            .applyDisplayDimension(1440, 1080)
            .applyFps(30.0f)
            .enableAutomaticFocus(true)
            .create()


        lensEngine.run((context as AppCompatActivity).findViewById<TextureView>(R.id.surface).surfaceTexture)
    }

    private fun stop() {
        analyzer.stop()
    }

    interface OnResult {
        fun onSuccess(text: String)
    }
}