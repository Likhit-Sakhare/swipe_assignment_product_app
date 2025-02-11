@file:OptIn(ExperimentalMaterial3Api::class)

package com.likhit.swipeassignment.presentation.product_add

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.rememberAsyncImagePainter
import com.likhit.swipeassignment.R
import com.likhit.swipeassignment.presentation.components.ProductTopAppBar
import com.likhit.swipeassignment.presentation.product_add.components.CustomProductTextField
import com.likhit.swipeassignment.presentation.utils.UIState
import com.likhit.swipeassignment.presentation.utils.getFileFromUri
import org.koin.androidx.compose.koinViewModel
import java.io.File

@Composable
fun AddProductScreenRoot(
    navController: NavHostController,
    viewModel: AddProductViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()

    val name by viewModel.name.collectAsState()
    val price by viewModel.price.collectAsState()
    val tax by viewModel.tax.collectAsState()

    val productTypes = viewModel.productTypes
    val isDropDownExpanded by viewModel.isDropDownExpanded.collectAsState()
    val selectedProductType by viewModel.selectedProductType.collectAsState()
    val setSelectedProductType = viewModel::setSelectedProductType
    val toggleDropDown = viewModel::toggleDropDown

    val image by viewModel.image.collectAsState()
    val setImage = viewModel::setImage

    val uiState by viewModel.uiState.collectAsState()

    AddProductScreen(
        name = name,
        price = price,
        tax = tax,
        setName = {
            viewModel.setName(it)
        },
        setPrice = {
            viewModel.setPrice(it)
        },
        setTax = {
            viewModel.setTax(it)
        },
        productTypes = productTypes,
        isDropDownExpanded = isDropDownExpanded,
        selectedProductType = selectedProductType,
        setSelectedProductType = setSelectedProductType,
        toggleDropDown = toggleDropDown,
        image = image,
        setImage = setImage,
        navController = navController,
        context = context,
        focusManager = focusManager,
        keyboardController = keyboardController,
        scrollState = scrollState,
        uiState = uiState,
        onAddProductClick = {
            viewModel.addProduct {
                navController.navigate("product_list"){
                    popUpTo(0)
                }
            }
        }
    )
}

@Composable
fun AddProductScreen(
    modifier: Modifier = Modifier,
    name: String,
    price: String,
    tax: String,
    setName: (String) -> Unit,
    setPrice: (String) -> Unit,
    setTax: (String) -> Unit,
    productTypes: List<String>,
    isDropDownExpanded: Boolean,
    selectedProductType: String,
    toggleDropDown: () -> Unit,
    setSelectedProductType: (String) -> Unit,
    image: File?,
    setImage: (File?) -> Unit,
    navController: NavHostController,
    context: Context,
    focusManager: FocusManager,
    keyboardController: SoftwareKeyboardController?,
    scrollState: ScrollState,
    uiState: UIState,
    onAddProductClick: () -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if(uri != null){
                val file = getFileFromUri(context.contentResolver, uri)
                setImage(file)
            }
        }
    )

    Scaffold(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                })
            }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.ime)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                ProductTopAppBar(
                    painter = painterResource(R.drawable.add_product),
                    title = "Add Product",
                    isBackButtonVisible = true,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .width(220.dp)
                        .height(220.dp)
                        .border(
                            width = 1.dp,
                            color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                            shape = RoundedCornerShape(15.dp)
                        )
                        .clickable {
                            launcher.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        },
                    contentAlignment = Alignment.Center
                ){
                    if(image != null){
                        Image(
                            painter = rememberAsyncImagePainter(model = image),
                            contentDescription = "Product's image",
                            modifier = Modifier
                                .fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    else{
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = null
                            )
                            Text(
                                text = "Tap here to add an image"
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 32.dp),
                ) {
                    Text(
                        text = "General",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .border(
                            width = 0.5.dp,
                            color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                            shape = RoundedCornerShape(15.dp)
                        )
                ){
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Name",
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(8.dp)
                        )
                        CustomProductTextField(
                            text = name,
                            hint = "Add product's name",
                            onValueChange = setName
                        )
                    }
                    HorizontalDivider(
                        thickness = 0.3.dp,
                        color = if(isSystemInDarkTheme()) Color.White else Color.Black
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text = "Type",
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(8.dp)
                                .padding(start = 8.dp)
                        )

                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ){
                            ExposedDropdownMenuBox(
                                expanded = isDropDownExpanded,
                                onExpandedChange = { toggleDropDown() },
                                modifier = Modifier.align(Alignment.CenterEnd)
                            ) {
                                OutlinedTextField(
                                    value = selectedProductType,
                                    onValueChange = {},
                                    readOnly = true,
                                    modifier = Modifier
                                        .menuAnchor()
                                        .padding(horizontal = 8.dp)
                                        .padding(bottom = 8.dp)
                                        .width(180.dp)
                                        .height(60.dp),
                                    label = {
                                        Text(text = "")
                                    },
                                    textStyle = TextStyle(fontSize = 14.sp),
                                    maxLines = 1,
                                    trailingIcon = {
                                        Icon(
                                            imageVector = if(isDropDownExpanded){
                                                Icons.Default.ArrowDropUp
                                            }else{
                                                Icons.Default.ArrowDropDown
                                            },
                                            contentDescription = null
                                        )
                                    }
                                )
                            }

                            DropdownMenu(
                                expanded = isDropDownExpanded,
                                onDismissRequest = { toggleDropDown() },
                                modifier = Modifier
                                    .width(150.dp)
                                    .height(250.dp)
                                    .align(Alignment.BottomCenter)
                            ) {
                                productTypes.forEach { type ->
                                    DropdownMenuItem(
                                        text = { Text(text = type, overflow = TextOverflow.Ellipsis) },
                                        onClick = {
                                            setSelectedProductType(type)
                                            toggleDropDown()
                                        }
                                    )
                                    HorizontalDivider()
                                }
                            }
                        }
                    }
                }

                HorizontalDivider(
                    thickness = 0.7.dp,
                    color = if(isSystemInDarkTheme()) Color.White else Color.Black,
                    modifier = Modifier
                        .padding(vertical = 24.dp, horizontal = 8.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 32.dp),
                ) {
                    Text(
                        text = "Price and Tax",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .border(
                            width = 0.5.dp,
                            color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                            shape = RoundedCornerShape(15.dp)
                        )
                ){
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Product's price",
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(8.dp)
                        )
                        CustomProductTextField(
                            text = price,
                            hint = "in Rupees",
                            onValueChange = setPrice,
                            keyboardType = KeyboardType.Number
                        )
                    }
                    HorizontalDivider(
                        thickness = 0.3.dp,
                        color = if(isSystemInDarkTheme()) Color.White else Color.Black
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Tax",
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(8.dp)
                        )
                        CustomProductTextField(
                            text = tax,
                            hint = "% tax on product",
                            onValueChange = setTax,
                            keyboardType = KeyboardType.Number
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if(uiState == UIState.LOADING){
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ){
                        CircularProgressIndicator()
                    }
                }else{
                    val enabled = name != "" && selectedProductType != "Select"
                            && price != "" && tax != ""
                    Button(
                        onClick = {
                            onAddProductClick()
                        },
                        enabled = enabled,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            disabledContentColor = Color.Gray
                        ),
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = if(enabled){
                                    Color.Transparent
                                }else{
                                    Color.Gray
                                },
                                shape = RoundedCornerShape(50.dp)
                            )
                            .background(
                                brush = if(enabled) {
                                    Brush.linearGradient(
                                        listOf(
                                            Color(0xFFD0E8FF), // Light Blue
                                            Color(0xFFFFF3E0)
                                        )
                                    )
                                }else {
                                    SolidColor(Color.Transparent)
                                },
                                shape = RoundedCornerShape(50.dp)
                            )
                    ) {
                        Text(text = "Add Product")
                    }
                }
            }
        }
    }
}