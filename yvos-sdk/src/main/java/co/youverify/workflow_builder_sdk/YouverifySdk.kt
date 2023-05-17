package co.youverify.workflow_builder_sdk

import co.youverify.workflow_builder_sdk.modules.documentcapture.DocumentCaptureModule
import co.youverify.workflow_builder_sdk.modules.documentcapture.DocumentOption
import co.youverify.workflow_builder_sdk.modules.livenesscheck.LivenessCheckModule
import co.youverify.workflow_builder_sdk.modules.livenesscheck.LivenessOption
import co.youverify.workflow_builder_sdk.modules.vform.VFormModule
import co.youverify.workflow_builder_sdk.modules.vform.VFormOption

object YouverifySdk {

    fun vFormModule(option: VFormOption): VFormModule = VFormModule(option = option)
    fun livenessModule(option: LivenessOption): LivenessCheckModule =LivenessCheckModule(option=option)
    fun documentCaptureModule(option: DocumentOption): DocumentCaptureModule =DocumentCaptureModule(option=option)
}