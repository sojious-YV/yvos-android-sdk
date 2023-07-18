package co.youverify.vformapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import co.youverify.yvos_sdk.Appearance
import co.youverify.yvos_sdk.YouverifySdk
import co.youverify.yvos_sdk.modules.documentcapture.DocumentOption
import co.youverify.yvos_sdk.modules.documentcapture.DocumentPersonalInfo
import co.youverify.yvos_sdk.modules.livenesscheck.LivenessData
import co.youverify.yvos_sdk.modules.livenesscheck.LivenessOption
import co.youverify.yvos_sdk.modules.livenesscheck.LivenessPersonalInfo
import co.youverify.yvos_sdk.modules.vform.VFormEntryData
import co.youverify.yvos_sdk.modules.vform.VFormOption
import co.youverify.yvos_sdk.modules.vform.VFormPersonalInfo
import com.google.gson.Gson
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {


    private lateinit var inputData: SdkInputData
    private var livenessClicked: Boolean=false
    private var formclicked: Boolean=false
    private var documentClicked: Boolean=false
    private lateinit var showDataButton: Button
    private var mFormData: VFormEntryData?=null
    private var mDocumentData: String=""
    private var mLivenessData: LivenessData?=null
    lateinit var progressBar:ProgressBar
    lateinit var formButton: Button
    lateinit var livenessButton: Button
    lateinit var documentButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val data=intent.getStringExtra(INPUT_DATA)
        inputData=Gson().fromJson(data,SdkInputData::class.java)

        //mainViewmodel= MainViewmodel()

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

            //val data=Gson().toJson(mDocumentData)

            MainViewmodel.documentData=mDocumentData
            startActivity(Intent(this,DocumentDataActivity::class.java))
           // return
            //textView_dt.text=mDocumentData
            //layout2.visibility= View.VISIBLE
            //Toast.makeText(this,mDocumentData,Toast.LENGTH_LONG).show()
        }

    }


    private fun useVFormModule(){

       // 6418559951282f74e34472e9 -1
        //644933b8c451436821dac571 -2
        // New public merchant key - 62b2e8b281442b03187f7896
        //64a6c1501d9315437ae409be

        val greeting=inputData.greeting.ifEmpty { "We will need to verify your identity. It will only take a moment." }
        val actionText=inputData.actionText.ifEmpty { "Verify Identity" }
        val buttonBackgroundColor=inputData.buttonBackGroundColor.ifEmpty { "#46B2C8" }
        val buttonTextColor=inputData.buttonTextColor.ifEmpty { "#ffffff" }
        val primaryColor=inputData.primaryColor.ifEmpty { "#46B2C8" }

        val vFormOption= VFormOption(
            vFormId = inputData.formId.ifEmpty { "64a6c1501d9315437ae409be" },
            publicMerchantKey = inputData.businessId.ifEmpty { "61d880f1e8e15aaf24558f1a" },
            //publicMerchantKey = "62b2e8b281442b03187f7896",
            dev = true,

            onSuccess = {vformData -> mFormData=vformData},
            onCompleted={vformData ->
               // mFormData=vformData
                        },
            onFailed={vformData -> mFormData=vformData},
            personalInfo = if (inputData.firstName.isNotEmpty() && inputData.lastName.isEmpty()){
                VFormPersonalInfo(
                    firstName = inputData.firstName,
                    //lastName = "Olowa",
                    //middleName = "Yekeen",
                    //email = "abc@gmail.com",
                    //mobile = "07012345678",
                    //gender = GenderType.MALE
                )
            }else if (inputData.firstName.isNotEmpty() && inputData.lastName.isNotEmpty()){
                VFormPersonalInfo(
                    firstName = inputData.firstName,
                    lastName = inputData.lastName,
                    //middleName = "Yekeen",
                    //email = "abc@gmail.com",
                    //mobile = "07012345678",
                    //gender = GenderType.MALE
                )
            }else{
                null
            },
            appearance = Appearance(
            greeting = greeting,
            actionText = actionText,
            buttonBackgroundColor = buttonBackgroundColor,
            buttonTextColor = buttonTextColor,
            primaryColor = primaryColor
            )
        )


        val vFormModule=YouverifySdk.vFormModule(option = vFormOption)

        lifecycleScope.launch {
            vFormModule.start(this@MainActivity)
        }
    }



    private fun useLivenessModule(){

        val greeting=inputData.greeting.ifEmpty { "We will need to carry out a liveness check. It will only take a moment." }
        val actionText=inputData.actionText.ifEmpty { "Start Liveness Test" }
        val buttonBackgroundColor=inputData.buttonBackGroundColor.ifEmpty { "#46B2C8" }
        val buttonTextColor=inputData.buttonTextColor.ifEmpty { "#ffffff" }
        val primaryColor=inputData.primaryColor.ifEmpty { "#46B2C8" }

        val livenessOption= LivenessOption(
            publicMerchantKey = inputData.businessId.ifEmpty { "61d880f1e8e15aaf24558f1a" },
            //publicMerchantKey = "6222a5ed3e7a41c29c031ecc",
            dev = true,
            onClose = { livenessData -> mLivenessData=livenessData },
            onSuccess = {livenessData -> mLivenessData=livenessData },
            onFailure = {livenessData -> mLivenessData=livenessData },
            onCancel = {livenessData ->mLivenessData=livenessData },
            onRetry = {livenessData -> mLivenessData=livenessData },
            personalInfo = if (inputData.firstName.isNotEmpty()){
                LivenessPersonalInfo(firstName = inputData.firstName)
            }else{null},

            //Green, White, Blue
            appearance = Appearance(
                greeting = greeting,
                actionText = actionText,
                buttonBackgroundColor = buttonBackgroundColor,
                buttonTextColor = buttonTextColor,
                primaryColor = primaryColor
            )
        )

        val livenessModule=YouverifySdk.livenessModule(livenessOption)
        livenessModule.start(this)

    }


    private fun useDocumentCaptureModule(){

        val greeting=inputData.greeting.ifEmpty { "We will need to carry out a  document capture. It will only take a moment." }
        val actionText=inputData.actionText.ifEmpty { "Start Document Capture" }
        val buttonBackgroundColor=inputData.buttonBackGroundColor.ifEmpty { "#46B2C8" }
        val buttonTextColor=inputData.buttonTextColor.ifEmpty { "#ffffff" }
        val primaryColor=inputData.primaryColor.ifEmpty { "#46B2C8" }


        val documentOption= DocumentOption(
            publicMerchantKey = inputData.businessId.ifEmpty { "61d880f1e8e15aaf24558f1a" },
            //publicMerchantKey = "6222a5ed3e7a41c29c031ecc",
            //dev = true,
            dev=false,
            onClose = {documentData -> mDocumentData=documentData },
            onSuccess = {documentData ->
                mDocumentData=documentData
                        },
            onCancel ={documentData ->mDocumentData=documentData},
            personalInfo = if (inputData.firstName.isNotEmpty()){
                DocumentPersonalInfo(firstName = inputData.firstName)
            }else{null},
            appearance = Appearance(
                greeting = greeting,
                actionText = actionText,
                buttonBackgroundColor = buttonBackgroundColor,
                buttonTextColor =buttonTextColor,
                primaryColor = primaryColor
            )
        )

        val documentModule=YouverifySdk.documentCaptureModule(option = documentOption)
        documentModule.start(this)

    }
}