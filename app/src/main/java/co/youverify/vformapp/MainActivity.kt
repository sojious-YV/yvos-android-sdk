package co.youverify.vformapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import co.youverify.yvos_sdk.Customization
import co.youverify.yvos_sdk.UserInfo
import co.youverify.yvos_sdk.modules.documentcapture.DocumentCaptureModule
import co.youverify.yvos_sdk.modules.documentcapture.DocumentData
import co.youverify.yvos_sdk.modules.livenesscheck.LivenessCheckModule
import co.youverify.yvos_sdk.modules.workflowBuilder.WorkflowBuilderModule
import com.google.gson.Gson


class MainActivity : AppCompatActivity() {


    private lateinit var inputData: SdkInputData
    private var livenessClicked: Boolean=false
    private var formclicked: Boolean=false
    private var documentClicked: Boolean=false
    private lateinit var showDataButton: Button
    private var mFormData: String?=null
    private var mDocumentData: DocumentData?=null
    private var livenessPhoto: String?=null
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
            //MainViewmodel.livenessData=livenessPhoto
            startActivity(
                Intent(this,LivenessDataActivity::class.java).apply {
                    putExtra(RETURNED_DATA_STRING,livenessPhoto)
                }
            )
            return
        }

        if (documentClicked){

            MainViewmodel.documentData=mDocumentData
            startActivity(Intent(this,DocumentDataActivity::class.java))
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

        val userInfo = if (inputData.firstName.isNotEmpty() && inputData.lastName.isEmpty())
            UserInfo.Builder().firstName(inputData.firstName).build()
            else if (inputData.firstName.isNotEmpty() && inputData.lastName.isNotEmpty())
                UserInfo.Builder().firstName(inputData.firstName).lastName(inputData.lastName).build()
            else null

       /* val vFormOption= VFormOption(
            vFormId = inputData.formId.ifEmpty { "64a6c1501d9315437ae409be" },
            publicMerchantKey = inputData.businessId.ifEmpty { "61d880f1e8e15aaf24558f1a" },
            //publicMerchantKey = "62b2e8b281442b03187f7896",
            dev = true,

            onSuccess = {vformData -> mFormData=vformData},
            onCompleted={vformData ->
               // mFormData=vformData
                        },
            onFailed={},
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
            appearance = Customization(
            greeting = greeting,
            actionText = actionText,
            buttonBackgroundColor = buttonBackgroundColor,
            buttonTextColor = buttonTextColor,
            primaryColor = primaryColor
            )
        )*/


        //val vFormModule=YouverifySdk.vFormModule(option = vFormOption)
        //val vFormModule = YouverifySdk.vFormModule(VFormOption())
        //val livenessCheckModule = YouverifySdk.livenessModule(LivenessOption())
        if (mFormData!=null) mFormData = null

        val workflowBuilderModule = WorkflowBuilderModule.Builder(publicMerchantKey = inputData.businessId.ifEmpty { "61d880f1e8e15aaf24558f1a" }, formId = inputData.formId.ifEmpty { "64a6c1501d9315437ae409be" } )
            .dev(true)
            .userInfo(
                userInfo
            )
            .customization(
                Customization.Builder()
                    .primaryColor(primaryColor)
                    .greetingMessage(greeting)
                    .actionButtonText(actionText)
                    .actionButtonTextColor(buttonTextColor)
                    .actionButtonBackgroundColor(buttonBackgroundColor)
                    .build()
            )
            .onSuccess { formData ->
                //form was submitted successfully. perform some action here
            }
            .onFailed {
                //form submission failed due to some error. perform some action here
            }
            .onCompleted {formData ->
                //form was submitted successfully. This is called immediately after "0nSuccess()".
                // perform some action here
            }
            .metaData(emptyMap())
            .build()



        workflowBuilderModule.start(this@MainActivity)

    }



    private fun useLivenessModule(){

        if (livenessPhoto!=null) livenessPhoto = null

        val greeting=inputData.greeting.ifEmpty { "We will need to carry out a liveness check. It will only take a moment." }
        val actionText=inputData.actionText.ifEmpty { "Start Liveness Test" }
        val buttonBackgroundColor=inputData.buttonBackGroundColor.ifEmpty { "#46B2C8" }
        val buttonTextColor=inputData.buttonTextColor.ifEmpty { "#ffffff" }
        val primaryColor=inputData.primaryColor.ifEmpty { "#46B2C8" }
        val userInfo = if (inputData.firstName.isNotEmpty())
            UserInfo.Builder().firstName(inputData.firstName).build()
        else null

        /*val livenessOption= LivenessOption(
            publicMerchantKey = inputData.businessId.ifEmpty { "61d880f1e8e15aaf24558f1a" },
            //publicMerchantKey = "6222a5ed3e7a41c29c031ecc",
            dev = true,
            onClose = {},
            onSuccess = {livenessData -> mLivenessData=livenessData},
            onFailure = {},
            onCancel = {},
            onRetry = {},
            personalInfo = if (inputData.firstName.isNotEmpty()){
                LivenessPersonalInfo(firstName = inputData.firstName)
            }else{null},

            //Green, White, Blue
            appearance = Customization(
                greeting = greeting,
                actionText = actionText,
                buttonBackgroundColor = buttonBackgroundColor,
                buttonTextColor = buttonTextColor,
                primaryColor = primaryColor
            )
        )*/

        //val livenessModule=YouverifySdk.livenessModule(livenessOption)

        val livenessCheckModule = LivenessCheckModule.Builder(publicMerchantKey = inputData.businessId.ifEmpty { "61d880f1e8e15aaf24558f1a" })
            .dev(true)
            .userInfo(
                userInfo
            )
            .customization(
                Customization.Builder()
                    .primaryColor(primaryColor)
                    .greetingMessage(greeting)
                    .actionButtonText(actionText)
                    .actionButtonTextColor(buttonTextColor)
                    .actionButtonBackgroundColor(buttonBackgroundColor)
                    .build()
            )
            .onSuccess { livenessPhoto ->
                // perform some action here
                this.livenessPhoto = livenessPhoto
            }
            .onFailed {
                // perform some action here
            }
            .onRetry {
                // perform some action here
            }
            .onCancel {
                // perform some action here
            }
            .onClose {
                // perform some action here
            }
            .build()

        livenessCheckModule.start(this)

    }


    private fun useDocumentCaptureModule(){

        if (MainViewmodel.documentData!=null) MainViewmodel.documentData=null

        val greeting=inputData.greeting.ifEmpty { "We will need to carry out a  document capture. It will only take a moment." }
        val actionText=inputData.actionText.ifEmpty { "Start Document Capture" }
        val buttonBackgroundColor=inputData.buttonBackGroundColor.ifEmpty { "#46B2C8" }
        val buttonTextColor=inputData.buttonTextColor.ifEmpty { "#ffffff" }
        val primaryColor=inputData.primaryColor.ifEmpty { "#46B2C8" }
        val userInfo = if (inputData.firstName.isNotEmpty())
            UserInfo.Builder().firstName(inputData.firstName).build()
        else null


        /*val documentOption= DocumentOption(
            publicMerchantKey = inputData.businessId.ifEmpty { "61d880f1e8e15aaf24558f1a" },
            //publicMerchantKey = "6222a5ed3e7a41c29c031ecc",
            //dev = true,
            dev=false,
            onClose = {},
            onSuccess = {documentData ->
                mDocumentData=documentData
                        },
            onCancel ={},
            personalInfo = if (inputData.firstName.isNotEmpty()){
                DocumentPersonalInfo(firstName = inputData.firstName)
            }else{null},
            appearance = Customization(
                greeting = greeting,
                actionText = actionText,
                buttonBackgroundColor = buttonBackgroundColor,
                buttonTextColor =buttonTextColor,
                primaryColor = primaryColor
            )
        )*/

        //val documentModule=YouverifySdk.documentCaptureModule(option = documentOption)
        val documentCaptureModule = DocumentCaptureModule.Builder(publicMerchantKey = inputData.businessId.ifEmpty { "61d880f1e8e15aaf24558f1a" })
            .dev(true)
            .userInfo(
                userInfo
            )
            .customization(
                Customization.Builder()
                    .primaryColor(primaryColor)
                    .greetingMessage(greeting)
                    .actionButtonText(actionText)
                    .actionButtonTextColor(buttonTextColor)
                    .actionButtonBackgroundColor(buttonBackgroundColor)
                    .build()
            )
            .onSuccess {documentData ->
                //perform some action
            }
            .onFailed{
                //perform some action
            }
            .onRetry {
                //perform some action
            }
            .onCancel{
                //perform some action
            }
            .onClose{
                //perform some action
            }
            .build()

        documentCaptureModule.start(this)

    }
}