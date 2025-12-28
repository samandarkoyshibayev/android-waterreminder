package com.android.waterreminder.presentation.screens.userinfo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.waterreminder.ui.theme.extendedColors
import com.android.waterreminder.ui.theme.gray


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfoBottomSheet(
    fullName: MutableState<String>,
    lastname: MutableState<String>,
    onDismiss: () -> Unit = {},
    onContinue: () -> Unit = {}
) {
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            Text(
                text = "Enter your details",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            )

            Text(
                text = "Personal Information",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = gray
                ),
                modifier = Modifier.padding(top = 5.dp)
            )

            TextField(
                value = fullName.value,
                onValueChange = { fullName.value = it },
                modifier = Modifier
                    .padding(top = 30.dp)
                    .height(56.dp)
                    .fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Enter your firstname",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 14.sp,
                            color = gray
                        )
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = MaterialTheme.extendedColors.cursorColor
                ),
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimary
                ),
            )

            TextField(
                value = lastname.value,
                onValueChange = { lastname.value = it },
                modifier = Modifier
                    .padding(top = 30.dp)
                    .height(56.dp)
                    .fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Enter your lastname",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 14.sp,
                            color = gray
                        )
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = MaterialTheme.extendedColors.cursorColor
                ),
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimary
                ),
            )

            Button(
                onClick = {
                    onDismiss()
                    onContinue()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                )
            ) {
                Text(
                    text = "Continue",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.extendedColors.white
                    )
                )
            }
        }
    }
}

