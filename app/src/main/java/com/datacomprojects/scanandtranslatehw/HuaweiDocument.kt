package com.datacomprojects.scanandtranslatehw

import android.graphics.Bitmap
import com.huawei.hmf.tasks.Task
import com.huawei.hms.mlsdk.MLAnalyzerFactory
import com.huawei.hms.mlsdk.common.MLException
import com.huawei.hms.mlsdk.common.MLFrame
import com.huawei.hms.mlsdk.document.MLDocument
import com.huawei.hms.mlsdk.document.MLDocumentSetting
import com.huawei.hms.mlsdk.text.MLRemoteTextSetting
import darthkilersprojects.com.log.L


class HuaweiDocument {

    fun start(bitmap: Bitmap) {
        val languageList: MutableList<String> = ArrayList()
        languageList.add("zh")
        languageList.add("en")
        val setting =
            MLDocumentSetting.Factory() // Specify the languages that can be recognized, which should comply with ISO 639-1.
                .setLanguageList(languageList) // Set the format of the returned text border box.
                // MLRemoteTextSetting.NGON: Return the coordinates of the four corner points of the quadrilateral.
                // MLRemoteTextSetting.ARC: Return the corner points of a polygon border in an arc. The coordinates of up to 72 corner points can be returned.
                .setBorderType(MLRemoteTextSetting.ARC)
                .create()
        val analyzer = MLAnalyzerFactory.getInstance().getRemoteDocumentAnalyzer(setting)
        val frame = MLFrame.fromBitmap(bitmap)


        val task: Task<MLDocument> = analyzer.asyncAnalyseFrame(frame)
        task.addOnSuccessListener {
            L.show(it.stringValue)
            it.blocks.forEach { L.show(it.stringValue); it.sections.forEach { L.show(it); it.lineList.forEach { L.show(it.points) }; it.wordList.forEach { L.show(it.characterList) } } }
        }.addOnFailureListener {
            L.show("error")
            try {
                val mlException = it as MLException
                L.show(it)
                val errorCode = mlException.errCode
                L.show(errorCode)
                val errorMessage = mlException.message
                L.show(errorMessage)
            } catch (error: Exception) {
                L.show(error)
            }
        }

    }
}