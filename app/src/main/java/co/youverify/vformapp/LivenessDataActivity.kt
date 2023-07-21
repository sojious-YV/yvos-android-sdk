package co.youverify.vformapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.bold
import co.youverify.yvos_sdk.modules.livenesscheck.LivenessData
import com.google.gson.Gson

class LivenessDataActivity : AppCompatActivity() {


    private var data: LivenessData?=null
    private lateinit var contentTextView: TextView
    private lateinit var titleTextView: TextView


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_liveness_data)
        titleTextView=findViewById(R.id.textView_title)
        contentTextView=findViewById(R.id.textView_content)
        displayReturnedData()
    }

    @SuppressLint("SetTextI18n")
    private fun displayReturnedData() {
        //val dataString=intent.getStringExtra(RETURNED_DATA_STRING)
        //data=Gson().fromJson(dataString,LivenessData::class.java)
        val livenessData=MainViewmodel.livenessData
        val  sb= SpannableStringBuilder()
            .bold { append("passed: ") }
            .append("${livenessData?.passed?:false}\n\n")
            .bold { append("photo: ") }
            .append("${livenessData?.photo}")


        titleTextView.text="Liveness Data"
        contentTextView.text=sb
    }


}