package co.youverify.yvos_sdk.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import co.youverify.yvos_sdk.theme.yvColor

@Composable
fun LoadingDialog( visible:Boolean, modifier: Modifier = Modifier, message:String="Please wait..."){

    if (visible)
        Dialog(onDismissRequest = {},) {
        Row(
            modifier=modifier.padding(horizontal = 32.dp).fillMaxWidth().background(color = Color(0xFF595959), shape = RoundedCornerShape(4.dp)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ){

            Box(modifier = Modifier.padding(top = 16.dp, bottom = 16.dp, start = 8.dp).size(48.dp)){

                /*Image(
                    painter = painterResource(id = R.drawable.baseline_close_24),
                    contentDescription =null ,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(37.dp,21.dp).align(Alignment.Center)

                )*/

                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    color= yvColor,
                    strokeWidth = 2.dp
                )


            }


            Text(
                text = message,
                modifier=modifier.padding(start = 8.dp),
                fontSize = 10.sp,
                color = Color.White
            )

        }
    }
}

@Preview
@Composable
fun LoadingDialogPreview(){
    LoadingDialog(visible = true)
}
