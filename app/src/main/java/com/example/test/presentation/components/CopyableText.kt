package com.example.test.presentation.components

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun CopyableText(text: String, onCopy: (String) -> Unit) {
    Text(
        text = text,
        modifier = Modifier
            .clickable {
                onCopy(text)
            }
            .padding(8.dp)
    )

}