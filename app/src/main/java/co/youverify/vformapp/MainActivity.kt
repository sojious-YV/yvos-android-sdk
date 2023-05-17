package co.youverify.vformapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.lifecycle.lifecycleScope
import co.youverify.workflow_builder_sdk.modules.vform.VFormOption
import co.youverify.workflow_builder_sdk.YouverifySdk
import co.youverify.workflow_builder_sdk.modules.documentcapture.DocumentOption
import co.youverify.workflow_builder_sdk.modules.livenesscheck.LivenessOption
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

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

        formButton.setOnClickListener{
            progressBar.visibility=View.VISIBLE
            formButton.visibility=  View.INVISIBLE
            livenessButton.visibility=  View.INVISIBLE
            documentButton.visibility=  View.INVISIBLE


            useVFormModule()
            //useLivenessModule()
            //useDocumentCaptureModule()
        }


        livenessButton.setOnClickListener{
            progressBar.visibility=View.VISIBLE
            formButton.visibility=  View.INVISIBLE
            livenessButton.visibility=  View.INVISIBLE
            documentButton.visibility=  View.INVISIBLE

            useLivenessModule()
            //useDocumentCaptureModule()
        }

        documentButton.setOnClickListener{
            progressBar.visibility=View.VISIBLE
            formButton.visibility=  View.INVISIBLE
            livenessButton.visibility=  View.INVISIBLE
            documentButton.visibility=  View.INVISIBLE

            useDocumentCaptureModule()
        }



    }


    private fun useVFormModule(){

       // 6418559951282f74e34472e9 -1
        //644933b8c451436821dac571 -2
        val vFormOption= VFormOption(
            vFormId = "6418559951282f74e34472e9",
            publicMerchantKey = "61d880f1e8e15aaf24558f1a",
            dev = true,
            onClose = {
                progressBar.visibility=View.INVISIBLE
                formButton.visibility=  View.VISIBLE
                livenessButton.visibility=  View.VISIBLE
                documentButton.visibility=  View.VISIBLE
            },
            onSuccess = {
                progressBar.visibility=View.INVISIBLE
                formButton.visibility=  View.VISIBLE
                livenessButton.visibility=  View.VISIBLE
                documentButton.visibility=  View.VISIBLE
            }
        )


        val vFormModule=YouverifySdk.vFormModule(option = vFormOption)
        lifecycleScope.launch {
           /* try {
                vFormModule.start(this@MainActivity)
            }catch (e:Exception){
                progressBar.visibility=View.INVISIBLE
                formButton.visibility=View.VISIBLE
                Toast.makeText(this@MainActivity,e.message,Toast.LENGTH_LONG).show()
            }*/

            vFormModule.start(this@MainActivity)
        }
    }

    override fun onResume() {
        super.onResume()
        if (progressBar.visibility==View.VISIBLE) progressBar.visibility= View.INVISIBLE
        if(formButton.visibility==View.INVISIBLE) formButton.visibility=View.VISIBLE
        if(livenessButton.visibility==View.INVISIBLE) livenessButton.visibility=  View.VISIBLE
        if (documentButton.visibility==View.INVISIBLE) documentButton.visibility=View.VISIBLE
    }

    private fun useLivenessModule(){
        val livenessOption= LivenessOption(
            publicMerchantKey = "61d880f1e8e15aaf24558f1a",
            dev = true,
            onClose = {
                progressBar.visibility=View.INVISIBLE
                formButton.visibility=  View.VISIBLE
                livenessButton.visibility=  View.VISIBLE
                documentButton.visibility=  View.VISIBLE
            },
            onSuccess = {
                progressBar.visibility=View.INVISIBLE
                formButton.visibility=  View.VISIBLE
                livenessButton.visibility=  View.VISIBLE
                documentButton.visibility=  View.VISIBLE
            }
        )

        val livenessModule=YouverifySdk.livenessModule(livenessOption)
        livenessModule.start(this)

    }


    private fun useDocumentCaptureModule(){
        val documentOption= DocumentOption(
            publicMerchantKey = "61d880f1e8e15aaf24558f1a",
            dev = true,
            onClose = {
                progressBar.visibility=View.INVISIBLE
                formButton.visibility=  View.VISIBLE
                livenessButton.visibility=  View.VISIBLE
                documentButton.visibility=  View.VISIBLE
            },
            onSuccess = {
                progressBar.visibility=View.INVISIBLE
                formButton.visibility=  View.VISIBLE
                livenessButton.visibility=  View.VISIBLE
                documentButton.visibility=  View.VISIBLE
            },
            onCancel ={
                progressBar.visibility=View.INVISIBLE
                formButton.visibility=  View.VISIBLE
                livenessButton.visibility=  View.VISIBLE
                documentButton.visibility=  View.VISIBLE
            }
        )

        val documentModule=YouverifySdk.documentCaptureModule(option = documentOption)
        documentModule.start(this)

    }
}