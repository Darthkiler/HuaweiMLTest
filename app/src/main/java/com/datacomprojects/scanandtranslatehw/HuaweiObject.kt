package com.datacomprojects.scanandtranslatehw

import android.content.Context
import android.graphics.Bitmap
import android.view.TextureView
import androidx.appcompat.app.AppCompatActivity
import com.huawei.hmf.tasks.Task
import com.huawei.hms.mlsdk.MLAnalyzerFactory
import com.huawei.hms.mlsdk.common.LensEngine
import com.huawei.hms.mlsdk.common.MLAnalyzer
import com.huawei.hms.mlsdk.common.MLFrame
import com.huawei.hms.mlsdk.objects.MLObject
import com.huawei.hms.mlsdk.objects.MLObjectAnalyzerSetting
import darthkilersprojects.com.log.L


class HuaweiObject {

    fun start(bitmap: Bitmap) {
        val setting = MLObjectAnalyzerSetting.Factory()
            .setAnalyzerType(MLObjectAnalyzerSetting.TYPE_PICTURE)
            .allowMultiResults()
            .allowClassification()
            .create()
        val analyzer = MLAnalyzerFactory.getInstance().getLocalObjectAnalyzer(setting)

        val frame = MLFrame.fromBitmap(bitmap)
        val task: Task<List<MLObject>> = analyzer.asyncAnalyseFrame(frame)
        task.addOnSuccessListener {
            it.forEach { L.show(it); L.show(it.border); L.show(it.tracingIdentity); L.show(it.typeIdentity); }
        }.addOnFailureListener {
            L.show(it)
        }

    }

    fun realTime(context: AppCompatActivity) {
        val analyzer = MLAnalyzerFactory.getInstance().localObjectAnalyzer

        analyzer.setTransactor(object : MLAnalyzer.MLTransactor<MLObject> {
            override fun destroy() {

            }

            override fun transactResult(p0: MLAnalyzer.Result<MLObject>?) {
                L.show("online list", p0)
            }

        })

        val lensEngine = LensEngine.Creator(context, analyzer)
            .setLensType(LensEngine.BACK_LENS)
            .applyDisplayDimension(1440, 1080)
            .applyFps(30.0f)
            .enableAutomaticFocus(true)
            .create()

        lensEngine.run((context).findViewById<TextureView>(R.id.surface).surfaceTexture)
    }
}