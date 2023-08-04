package co.youverify.vformapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.google.gson.Gson

class DataInputActivity : AppCompatActivity() {


    private lateinit var proceedButton: Button
    private lateinit var businessIdEditText: EditText
    private lateinit var formIdEditText: EditText
    private lateinit var devEditText: EditText
    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var buttonBackgroundColorEditText: EditText
    private lateinit var buttonTextColorEditText: EditText
    private lateinit var primaryColorEditText: EditText
    private lateinit var greetingEditText: EditText
    private lateinit var actionTextEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_input)
        initializeViews()
    }

    private fun initializeViews() {
        businessIdEditText=findViewById(R.id.editTextText_biz_id)
        formIdEditText=findViewById(R.id.editText_template_id)
        devEditText=findViewById(R.id.editText_dev)
        firstNameEditText=findViewById(R.id.editText_first_name)
        lastNameEditText=findViewById(R.id.editText_last_name)
        buttonBackgroundColorEditText=findViewById(R.id.editText_button_bg_color)
        buttonTextColorEditText=findViewById(R.id.editText_button_text_color)
        primaryColorEditText=findViewById(R.id.editText_primary_color)
        greetingEditText=findViewById(R.id.editText_greeting)
        proceedButton=findViewById(R.id.button_proceed)
        actionTextEditText=findViewById(R.id.editText_action_text)

        proceedButton.setOnClickListener{
            val input=SdkInputData(
                businessId = businessIdEditText.text.toString(),
                formId = formIdEditText.text.toString(),
                dev= true,
                firstName = firstNameEditText.text.toString(),
                lastName = lastNameEditText.text.toString(),
                buttonBackGroundColor = buttonBackgroundColorEditText.text.toString(),
                buttonTextColor = buttonTextColorEditText.text.toString(),
                primaryColor = primaryColorEditText.text.toString(),
                greeting=greetingEditText.text.toString(),
                actionText=actionTextEditText.text.toString()
            )
            val inputData= Gson().toJson(input)
            startActivity(Intent(this,MainActivity::class.java).apply {
                putExtra(INPUT_DATA,inputData)
            })
        }

    }



}