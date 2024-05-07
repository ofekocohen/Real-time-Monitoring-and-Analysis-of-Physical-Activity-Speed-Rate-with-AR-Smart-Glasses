package com.ofekyuval.arworkout.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ofekyuval.arworkout.R
import com.ofekyuval.arworkout.ui.theme.BluePrimary

@Composable
fun MyDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
) {
    AlertDialog(
        containerColor = Color.White,
        title = { Text(text = dialogTitle) },
        text = { Text(text = dialogText) },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onConfirmation) {
                Text(stringResource(R.string.confirm), color = BluePrimary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(R.string.dismiss), color = BluePrimary)
            }
        }
    )
}

@Preview
@Composable
private fun DialogPrev() {
    MyDialog(
        onDismissRequest = { },
        onConfirmation = { },
        dialogTitle = "Title",
        dialogText = "Text"
    )
}