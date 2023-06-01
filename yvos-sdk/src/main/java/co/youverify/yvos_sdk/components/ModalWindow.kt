package co.youverify.yvos_sdk.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.youverify.yvos_sdk.theme.Pink40
import co.youverify.yvos_sdk.theme.SdkTheme
import co.youverify.yvos_sdk.theme.inputDeepTextColor
import co.youverify.yvos_sdk.theme.primaryColor
import co.youverify.yvos_sdk.theme.yvColor
import co.youverify.yvos_sdk.theme.yvColor1

@Composable
fun ModalWindow(
    modifier: Modifier = Modifier,
    name: String,
    onStartButtonClicked: () -> Unit,
    //loadingUrl: Boolean,
    visible:Boolean
) {

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
                                    text = "Hey $name",
                                    modifier = modifier.padding(start = 8.dp,top=10.dp),
                                    fontSize = 20.sp,
                                    color =Color(0XFF666666) ,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = "We would like to perform an identity check",
                                    modifier = modifier.padding(start = 8.dp, top = 20.dp,end=32.dp),
                                    fontSize = 12.sp,
                                    color =Color(0XFF666666),
                                    fontWeight = FontWeight.Medium,
                                )



                                //Spacer(modifier = Modifier.weight(1f))

                                Button(
                                    onClick = {onStartButtonClicked()},
                                    modifier= Modifier
                                        //.height(50.dp)
                                        //.width(50.dp)
                                        //.align(Alignment.End,)
                                        .fillMaxWidth()
                                        .padding( top = 60.dp,end=16.dp,start=16.dp),

                                    shape = RoundedCornerShape(4.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor, contentColor = Color.White),
                                    content = {
                                        Text(
                                            text = "Start",
                                            fontSize = 11.sp,
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
               onStartButtonClicked = {},
               //loadingUrl = false,
               visible = true
           )
       }
    }
}