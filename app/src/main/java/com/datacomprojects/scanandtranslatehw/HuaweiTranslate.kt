package com.datacomprojects.scanandtranslatehw

import com.huawei.hms.mlsdk.common.MLException
import com.huawei.hms.mlsdk.translate.MLTranslatorFactory
import com.huawei.hms.mlsdk.translate.cloud.MLRemoteTranslateSetting
import com.huawei.hms.mlsdk.translate.cloud.MLRemoteTranslator
import darthkilersprojects.com.log.L

class HuaweiTranslate {

    init {
        implementTranslation(createTextTranslator(), "привет")
    }
    private fun createTextTranslator() : MLRemoteTranslator {
        // Create a text translator using custom parameter settings.

        // Create a text translator using custom parameter settings.
        val setting =
            MLRemoteTranslateSetting.Factory() // Set the source language code. The ISO 639-1 standard is used. This parameter is optional. If this parameter is not set, the system automatically detects the language.
                .setSourceLangCode("ru") // Set the target language code. The ISO 639-1 standard is used.
                .setTargetLangCode("zh-hk")
                .create()
        return MLTranslatorFactory.getInstance().getRemoteTranslator(setting)
    }

    private fun implementTranslation(mlRemoteTranslator: MLRemoteTranslator, sourceText: String) {
        // sourceText: text to be translated, with up to 5000 characters.

        // sourceText: text to be translated, with up to 5000 characters.
        val task = mlRemoteTranslator.asyncTranslate(sourceText)
        task.addOnSuccessListener {
            L.show(it)
        }.addOnFailureListener { e ->
            L.show(e)
            try {
                val mlException = e as MLException
                L.show(e.localizedMessage)
                val errorCode = mlException.errCode
                L.show(errorCode)
                val errorMessage = mlException.message
                L.show(errorMessage)
            } catch (error: Exception) {
                L.show(error)
            }
        }

    }

    private fun releaseTranslator(mlRemoteTranslator: MLRemoteTranslator?) = mlRemoteTranslator?.stop()
}