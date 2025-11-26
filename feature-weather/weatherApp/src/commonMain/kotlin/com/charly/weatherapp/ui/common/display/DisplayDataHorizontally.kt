package com.charly.weatherapp.ui.common.display

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.charly.uitheme.Dimensions
import com.charly.uitheme.TypographySize

@Composable
fun DisplayDataHorizontally(
    title: String,
    data: String
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimensions.medium, vertical = Dimensions.small)
    ) {
        Text(
            text = title,
            fontSize = TypographySize.title
        )
        Text(
            text = data,
            fontSize = TypographySize.body
        )
    }
}
