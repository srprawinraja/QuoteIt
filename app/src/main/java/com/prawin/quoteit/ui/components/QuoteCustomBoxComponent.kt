package com.prawin.quoteit.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prawin.quoteit.db.saved.SavedQuoteEntity
import com.prawin.quoteit.ui.theme.themeColors

@Composable
fun QuoteComponentBox(savedQuoteEntity: SavedQuoteEntity, onClick: ()-> Unit){
    Column (
        modifier = Modifier.width(200.dp).height(200.dp)
            .border(
                border = BorderStroke(1.dp, themeColors().text),
                shape = RoundedCornerShape(5.dp)
            ).padding(20.dp).clickable(onClick = {
                onClick()
            }),
        verticalArrangement = Arrangement.SpaceBetween
    ){
        var quote = savedQuoteEntity.savedQuote
        if(quote.length>50){
            quote = quote.substring(0,50) + "..."
        }
        Text(
            text = quote,
            fontSize = 20.sp,
            color = themeColors().text,
            fontFamily = FontFamily.Serif,
            lineHeight = 25.sp,
            modifier = Modifier.fillMaxWidth()
        )
        Text(modifier = Modifier.wrapContentSize(), text=savedQuoteEntity.savedAuthor, color=themeColors().lightText, fontSize = 15.sp)
    }
}