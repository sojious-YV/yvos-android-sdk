package co.youverify.yvos_sdk.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.youverify.yvos_sdk.theme.SdkTheme
import co.youverify.yvos_sdk.theme.primaryColor

@Composable
fun ModalWindow(
    modifier: Modifier = Modifier,
    name: String,
    greeting:String,
    onActionButtonClicked: () -> Unit,
    buttonText:String,
    buttonTextColorString:String,
    buttonBackGroundColorString:String,
    visible:Boolean
) {

    val buttonTextColor=Color(android.graphics.Color.parseColor(buttonTextColorString))
    val buttonBackGroundColor=Color(android.graphics.Color.parseColor(buttonBackGroundColorString))
    val namePrefixedWithComma=", $name"


    AnimatedVisibility(
                visible = visible,
                modifier = modifier.fillMaxSize(),

                content = {

                    Box(
                        modifier=Modifier.fillMaxSize()
                            .background(Color.White),
                        contentAlignment = Alignment.Center,
                    ){

                        Column(
                            modifier = Modifier
                                //.padding(1.dp)
                                //.fillMaxSize()
                                .width(250.dp)
                                .height(250.dp)
                                .background(color = Color.White, shape = RoundedCornerShape(4.dp)),
                                //.border(1.dp, color = yvColor1, shape = RoundedCornerShape(10.dp)),
                            //verticalArrangement = Arrangement.spacedBy(8.dp),
                            content = {
                                Text(
                                    text = "Hey$namePrefixedWithComma",
                                    modifier = modifier.padding(start = 8.dp,top=10.dp),
                                    fontSize = 20.sp,
                                    color =Color(0XFF666666) ,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = greeting,
                                    modifier = modifier.padding(start = 8.dp, top = 20.dp,end=32.dp),
                                    fontSize = 14.sp,
                                    color =Color(0XFF666666),
                                    fontWeight = FontWeight.Medium,
                                )



                                //Spacer(modifier = Modifier.weight(1f))

                                Button(
                                    onClick = {onActionButtonClicked()},
                                    modifier= Modifier
                                        //.height(50.dp)
                                        //.width(50.dp)
                                        //.align(Alignment.End,)
                                        .fillMaxWidth()
                                        .padding( top = 60.dp,end=16.dp),

                                    shape = RoundedCornerShape(4.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor =buttonBackGroundColor, contentColor =buttonTextColor),
                                    content = {
                                        Text(
                                            text = buttonText,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    },
                                )


                            }

                        )
                    }


                }
            )

}

@Preview
@Composable
fun ModalWindowPreview(){
    SdkTheme {
       Surface {
           ModalWindow(
               name = "Adesoji",
               onActionButtonClicked = {},
               //loadingUrl = false,
               visible = true,
               buttonBackGroundColorString = "#46B2C8",
               buttonTextColorString = "#ffffff",
               greeting = "We will need to verify your identity. It will only take a moment.",
               buttonText = "Verify Identity"
           )
       }
    }
}