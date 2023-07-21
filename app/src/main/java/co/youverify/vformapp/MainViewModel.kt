package co.youverify.vformapp

import co.youverify.yvos_sdk.modules.documentcapture.DocumentData
import co.youverify.yvos_sdk.modules.livenesscheck.LivenessData

class MainViewmodel{

   companion object{
       var documentData:DocumentData?=null
       var livenessData:LivenessData?=null
   }
}