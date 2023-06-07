package co.youverify.vformapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import co.youverify.yvos_sdk.Appearance
import co.youverify.yvos_sdk.modules.vform.VFormOption
import co.youverify.yvos_sdk.YouverifySdk
import co.youverify.yvos_sdk.modules.documentcapture.DocumentData
import co.youverify.yvos_sdk.modules.documentcapture.DocumentOption
import co.youverify.yvos_sdk.modules.documentcapture.DocumentPersonalInfo
import co.youverify.yvos_sdk.modules.livenesscheck.LivenessData
import co.youverify.yvos_sdk.modules.livenesscheck.LivenessOption
import co.youverify.yvos_sdk.modules.livenesscheck.LivenessPersonalInfo
import co.youverify.yvos_sdk.modules.vform.GenderType
import co.youverify.yvos_sdk.modules.vform.VFormEntryData
import co.youverify.yvos_sdk.modules.vform.VFormPersonalInfo
import com.google.gson.Gson
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var livenessClicked: Boolean=false
    private var formclicked: Boolean=false
    private var documentClicked: Boolean=false
    private lateinit var showDataButton: Button
    private var mFormData: VFormEntryData?=null
    private var mDocumentData: DocumentData?=null
    private var mLivenessData: LivenessData?=null
    lateinit var progressBar:ProgressBar
    lateinit var formButton: Button
    lateinit var livenessButton: Button
    lateinit var documentButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar=findViewById(R.id.progressBar)
        formButton=findViewById(R.id.form_button)
        livenessButton=findViewById(R.id.liveness_button)
        documentButton=findViewById(R.id.document_button)
        showDataButton=findViewById(R.id.button_returned_data)


        formButton.setOnClickListener{
            formclicked=true
            livenessClicked=false
            documentClicked=false
            useVFormModule()
        }
        livenessButton.setOnClickListener{
            livenessClicked=true
            formclicked=false
            documentClicked=false
            useLivenessModule()
        }
        documentButton.setOnClickListener{
            documentClicked=true
            formclicked=false
            livenessClicked=false
            useDocumentCaptureModule()
        }
        showDataButton.setOnClickListener{showProcessData()}





    }

    private fun showProcessData() {
        if (formclicked) {

            val data=Gson().toJson(mFormData)
            startActivity(
            Intent(this,FormDataActivity::class.java).apply { putExtra(RETURNED_DATA_STRING,data) }
            )
            return
        }


        if (livenessClicked){
            val data=Gson().toJson(mLivenessData)
            startActivity(
                Intent(this,LivenessDataActivity::class.java).apply { putExtra(RETURNED_DATA_STRING,data) }
            )
            return
        }

        if (documentClicked){
            val data=Gson().toJson(mDocumentData)
            startActivity(
                Intent(this,DocumentDataActivity::class.java).apply { putExtra(RETURNED_DATA_STRING,data) }
            )
            return
        }

    }


    private fun useVFormModule(){

       // 6418559951282f74e34472e9 -1
        //644933b8c451436821dac571 -2
        // New public merchant key - 62b2e8b281442b03187f7896
        val vFormOption= VFormOption(
            vFormId = "6418559951282f74e34472e9",
            publicMerchantKey = "61d880f1e8e15aaf24558f1a",
            //publicMerchantKey = "62b2e8b281442b03187f7896",
            dev = true,

            onSuccess = {vformData -> mFormData=vformData},
            onCompleted={vformData ->
               // mFormData=vformData
                        },
            onFailed={vformData -> mFormData=vformData},
            personalInfo = VFormPersonalInfo(
                firstName = "Adesoji",
                lastName = "Olowa",
                //middleName = "Yekeen",
                //email = "abc@gmail.com",
                //mobile = "07012345678",
                //gender = GenderType.MALE
            ),
        appearance = Appearance(
            greeting = "We really really really need to verify your identity. It will only take a moment.",
            actionText = "Start Jare",
            buttonBackgroundColor = "#46B2C8",
            buttonTextColor = "#ffffff",
            primaryColor = "#bf00ff"
        )
        )


        val vFormModule=YouverifySdk.vFormModule(option = vFormOption)

        lifecycleScope.launch {
            vFormModule.start(this@MainActivity)
        }
    }



    private fun useLivenessModule(){
        val livenessOption= LivenessOption(
            publicMerchantKey = "61d880f1e8e15aaf24558f1a",
            //publicMerchantKey = "6222a5ed3e7a41c29c031ecc",
            dev = true,
            onClose = { livenessData -> mLivenessData=livenessData },
            onSuccess = {livenessData -> mLivenessData=livenessData },
            onFailure = {livenessData -> mLivenessData=livenessData },
            onCancel = {livenessData ->mLivenessData=livenessData },
            onRetry = {livenessData -> mLivenessData=livenessData },
            personalInfo = LivenessPersonalInfo(firstName = "Adesoji"),
            /*appearance = Appearance(
                greeting = "We will need to verify your identity. It will only take a moment.",
                actionText = "Verify Identity",
                buttonBackgroundColor = "#46B2C8",
                buttonTextColor = "#ffffff",
                primaryColor = "#46B2C8"
            )*/
        )

        val livenessModule=YouverifySdk.livenessModule(livenessOption)
        livenessModule.start(this)

    }


    private fun useDocumentCaptureModule(){
        val documentOption= DocumentOption(
            publicMerchantKey = "61d880f1e8e15aaf24558f1a",
            //publicMerchantKey = "6222a5ed3e7a41c29c031ecc",
            //dev = true,
            dev=true,
            onClose = {documentData -> mDocumentData=documentData },
            onSuccess = {documentData ->mDocumentData=documentData},
            onCancel ={documentData ->mDocumentData=documentData},
            personalInfo = DocumentPersonalInfo(firstName = "Adesoji"),
            appearance = Appearance(
                greeting = "We will need to verify your identity. It will only take a moment.",
                actionText = "Verify Identity",
                buttonBackgroundColor = "#46B2C8",
                buttonTextColor = "#ffffff",
                primaryColor = "#ffff00"
            )
        )

        val documentModule=YouverifySdk.documentCaptureModule(option = documentOption)
        documentModule.start(this)

    }
}