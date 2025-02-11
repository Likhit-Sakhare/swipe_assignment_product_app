package com.likhit.swipeassignment

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.likhit.swipeassignment.presentation.product_add.AddProductScreenRoot
import com.likhit.swipeassignment.presentation.product_list.ProductListScreenRoot

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "product_list"
    ) {
        composable(
            route = "product_list"
        ){
            ProductListScreenRoot(navController)
        }
        composable(
            route = "add_product"
        ){
            AddProductScreenRoot(navController)
        }
    }
}