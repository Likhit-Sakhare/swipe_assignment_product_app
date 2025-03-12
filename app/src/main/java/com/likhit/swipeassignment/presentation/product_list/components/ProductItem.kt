package com.likhit.swipeassignment.presentation.product_list.components

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.google.accompanist.flowlayout.FlowRow
import com.likhit.swipeassignment.data.remote.model.Product

@Composable
fun ProductItem(
    modifier: Modifier = Modifier,
    context: Context,
    product: Product
) {
    val placeholderImage = "https://karanzi.websites.co.in/obaju-turquoise/img/product-placeholder.png"
    val isValidImage = product.image.isNotEmpty() && product.image != placeholderImage
    var showImageDialog by remember {
        mutableStateOf(false)
    }
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(15.dp))
            .heightIn(min = 350.dp)
            .wrapContentHeight()
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(15.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.9f)
        )
    ){
        Column(
            modifier = Modifier
                .padding(16.dp)
                .height(300.dp)
        ){
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .clickable {
                        if(isValidImage){
                            showImageDialog = true
                        }
                    },
                contentAlignment = Alignment.Center
            ){
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(
                            if (isValidImage) product.image else placeholderImage
                        )
                        .crossfade(true)
                        .build(),
                    loading = {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ){
                            CircularProgressIndicator()
                        }
                    },
                    onError = { error ->
                        Log.e("ImageError", "Error in loading image: ${error.result}")
                    },
                    contentDescription = product.product_name,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White.copy(0.7f)),
                    contentScale = ContentScale.Crop
                )
            }
        }
        FlowRow(
            mainAxisSpacing = 10.dp,
            crossAxisSpacing = 10.dp,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            ProductTag(label = "Product Type: ", tag = product.product_type)
            ProductTag(label = "Price: ",tag = "${product.price} ₹")
            ProductTag(label = "Product Name: ",tag = product.product_name)
            ProductTag(label = "Tax: ",tag = "${product.tax} %")
        }
        Spacer(modifier = Modifier.heightIn(16.dp))
    }

    if(showImageDialog){
        ProductImageDialog(
            image = product.image,
            onDismiss = {
                showImageDialog = false
            }
        )
    }
}