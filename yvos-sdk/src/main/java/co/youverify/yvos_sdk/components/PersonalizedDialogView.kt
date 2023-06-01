package co.youverify.yvos_sdk.components

import android.content.Context
import android.util.AttributeSet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.AbstractComposeView

class PersonalizedDialogView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
):AbstractComposeView(context,attrs,defStyleAttr) {

    private val _nameText = mutableStateOf("")
    private val _loadingUrl = mutableStateOf(false)
    private val _isVisible = mutableStateOf(false)

    private var _onProceedButtonClicked={}
    private val _onDismissRequest ={_isVisible.value=false}


    //Public getter/setter
    var nameText: String
        get() = _nameText.value
        set(value) { _nameText.value = value }

    var loadingUrl: Boolean
        get() = _loadingUrl.value
        set(value) { _loadingUrl.value = value }

    var isVisible: Boolean
        get() = _isVisible.value
        set(value) { _isVisible.value = value }

    var onProceedButtonClicked: ()->Unit
        get() = _onProceedButtonClicked
        set(value) { _onProceedButtonClicked = value }


    @Composable
    override fun Content() {
        ModalWindow(
            name =_nameText.value ,
            onStartButtonClicked = { /*TODO*/ },
            //loadingUrl =_loadingUrl.value ,
            visible =_isVisible.value
        )
    }
}