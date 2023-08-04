package co.youverify.yvos_sdk.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ProgressIndicator(colorString: String, visible:Boolean){
    if (visible)
        CircularProgressIndicator(
            modifier=Modifier.size(30.dp),
            color= Color(android.graphics.Color.parseColor(colorString)),
            strokeWidth = 2.dp
    )
}