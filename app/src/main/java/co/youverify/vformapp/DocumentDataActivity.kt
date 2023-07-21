package co.youverify.vformapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.bold
import co.youverify.yvos_sdk.modules.documentcapture.DocumentData
import com.google.gson.Gson

class DocumentDataActivity : AppCompatActivity() {


    private lateinit var contentTextView: TextView
    private lateinit var titleTextView: TextView


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document_data)
        titleTextView=findViewById(R.id.textView_title)
        contentTextView=findViewById(R.id.textView_content)
        displayReturnedData()
    }

    @SuppressLint("SetTextI18n")
    private fun displayReturnedData() {
        //val dataString=intent.getStringExtra(RETURNED_DATA_STRING)
         //data=Gson().fromJson(dataString,DocumentData::class.java)
         val documentData=MainViewmodel.documentData
       val  sb=SpannableStringBuilder()
           .bold { append("documentNumber: ") }
           .append("${documentData?.documentNumber}\n")
           .bold { append("firstName: ") }
           .append("${documentData?.firstName}\n")
           .bold { append("lastNumber: ") }
           .append("${documentData?.lastName}\n")
           .bold { append("fullName: ") }
           .append("${documentData?.fullName}\n")
           .bold { append("dateOfBirthNumber: ") }
           .append("${documentData?.dateOfBirth}\n")
           .bold { append("dateOfExpiry: ") }
           .append("${documentData?.dateOfExpiry}\n")
           .bold { append("gender: ") }
           .append("${documentData?.gender}\n")
           //.bold { append("fullDocumentFrontImage: ") }
           //.append("${documentData?.fullDocumentFrontImage}\n")
           //.bold { append("fullDocumentBackImage: ") }
           //.append("${documentData?.fullDocumentBackImage}\n")
           //.bold { append("fullDocumentImage: ") }
           //.append("${documentData?.fullDocumentImage}\n")
           //.bold { append("rawMRZString: ") }
           //.append("${documentData?.rawMRZString}\n")

        titleTextView.text="Document Data"
        contentTextView.text=sb
    }


}