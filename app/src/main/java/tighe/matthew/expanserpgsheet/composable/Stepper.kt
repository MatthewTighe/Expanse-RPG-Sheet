//package tighe.matthew.expanserpgsheet.composable
//
//import androidx.compose.Composable
//import androidx.compose.unaryPlus
//import androidx.ui.core.Text
//import androidx.ui.core.dp
//import androidx.ui.layout.Container
//import androidx.ui.layout.Row
//import androidx.ui.layout.WidthSpacer
//import androidx.ui.material.Button
//import androidx.ui.material.MaterialTheme
//import androidx.ui.material.themeTextStyle
//import androidx.ui.tooling.preview.Preview
//
//@Preview
//@Composable
//fun Stepper() {
//    val count = 0
//    val onRemove = {}
//    val onAdd = {}
//    MaterialTheme {
//        Row {
//            Button(
//                text = "-", onClick = if (count > 0) onRemove else {
//                    {}
//                })
//
//            WidthSpacer(8.dp)
//            Container(width = 24.dp) {
//                Text(text = "$count", style = +themeTextStyle { body1 })
//            }
//            WidthSpacer(8.dp)
//
//            Button(text = "+", onClick = onAdd)
//        }
//    }
//}