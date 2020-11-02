package com.datacomprojects.scanandtranslatehw

import com.huawei.hmf.tasks.Task
import com.huawei.hms.mlsdk.common.MLException
import com.huawei.hms.mlsdk.langdetect.MLDetectedLang
import com.huawei.hms.mlsdk.langdetect.MLLangDetectorFactory
import com.huawei.hms.mlsdk.langdetect.cloud.MLRemoteLangDetectorSetting
import com.huawei.hms.mlsdk.langdetect.local.MLLocalLangDetector
import com.huawei.hms.mlsdk.langdetect.local.MLLocalLangDetectorSetting
import darthkilersprojects.com.log.L


class HuaweiLanguageDetection {
    fun offlineDetectLanguage(text: String) {
        // Create a local language detector.

        // Create a local language detector.
        val factory: MLLangDetectorFactory = MLLangDetectorFactory.getInstance()
        val setting: MLLocalLangDetectorSetting =
            MLLocalLangDetectorSetting.Factory() // Set the minimum confidence threshold for language detection.
                .setTrustedThreshold(0.01f)
                .create()
        val mlLocalLangDetector: MLLocalLangDetector = factory.getLocalLangDetector(setting)

        val probabilityDetectTask: Task<List<MLDetectedLang>> =
            mlLocalLangDetector.probabilityDetect(text) // sourceText: input text string.

        probabilityDetectTask.addOnSuccessListener {
            L.show(it)
        }.addOnFailureListener {
            L.show(it)
        }
        val firstBestDetectTask: Task<String> =
            mlLocalLangDetector.firstBestDetect(text) // sourceText: input text string.

        firstBestDetectTask.addOnSuccessListener {
            L.show(it)
        }.addOnFailureListener {
            L.show(it)
        }
    }

    fun onlineDetectLanguage(text: String) {
        val setting =
            MLRemoteLangDetectorSetting.Factory() // Set the minimum confidence threshold for language detection.
                .setTrustedThreshold(0.01f)
                .create()
        val mlRemoteLangDetector = MLLangDetectorFactory.getInstance()
            .getRemoteLangDetector(setting)
        val probabilityDetectTask = mlRemoteLangDetector.probabilityDetect(text)
        probabilityDetectTask.addOnSuccessListener {
            L.show(it)
        }.addOnFailureListener {
            // Processing logic for detection failure.
            // Recognition failure.
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
        val firstBestDetectTask = mlRemoteLangDetector.firstBestDetect(text)
        firstBestDetectTask.addOnSuccessListener {
            L.show(it)
        }.addOnFailureListener {
            // Processing logic for detection failure.
            // Recognition failure.
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
}