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


    private var data: DocumentData?=null
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
        val dataString=intent.getStringExtra(RETURNED_DATA_STRING)
         data=Gson().fromJson(dataString,DocumentData::class.java)
       val  sb=SpannableStringBuilder()
           .bold { append("documentNumber: ") }
           .append("${data?.documentNumber}\n")
           .bold { append("firstName: ") }
           .append("${data?.firstName}\n")
           .bold { append("lastNumber: ") }
           .append("${data?.lastName}\n")
           .bold { append("fullName: ") }
           .append("${data?.fullName}\n")
           .bold { append("dateOfBirthNumber: ") }
           .append("${data?.dateOfBirth}\n")
           .bold { append("dateOfExpiry: ") }
           .append("${data?.dateOfExpiry}\n")
           .bold { append("gender: ") }
           .append("${data?.gender}\n")
           .bold { append("fullDocumentFrontImage: ") }
           .append("${data?.fullDocumentFrontImage}\n")
           .bold { append("fullDocumentBackImage: ") }
           .append("${data?.fullDocumentBackImage}\n")
           .bold { append("fullDocumentImage: ") }
           .append("${data?.fullDocumentImage}\n")
           .bold { append("rawMRZString: ") }
           .append("${data?.rawMRZString}\n")

        titleTextView.text="Document Data"
        contentTextView.text=sb
    }


}