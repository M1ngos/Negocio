package com.mobile.negocio.ui.views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mobile.negocio.R
import com.mobile.negocio.ui.navigation.navRegistryItems


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RegisterScreen () {
   Column (modifier = Modifier
       .fillMaxSize()
   ) {
       var selectedTabIndex by remember {
           mutableStateOf(0)
       }

       val pagerState = rememberPagerState {
           navRegistryItems.size
       }

       LaunchedEffect(selectedTabIndex) {
           pagerState.animateScrollToPage(selectedTabIndex)
       }

       LaunchedEffect(pagerState.currentPage) {
           selectedTabIndex = pagerState.currentPage
       }

       TabRow(selectedTabIndex = selectedTabIndex ) {
            navRegistryItems.forEachIndexed { index, navRegistryItem ->
                Tab(
                    selected = index == selectedTabIndex,
                    onClick = {
                        selectedTabIndex = index
                    },
                    text = {
                        Text(text = navRegistryItem.title)
                    }
                )
            }
        }

       HorizontalPager(
           state = pagerState,
           modifier = Modifier
               .fillMaxWidth()
               .weight(1f)
       ) {index ->
          Box(
              modifier = Modifier.fillMaxSize(),
              contentAlignment = Alignment.Center
          ) {
            Text(text = navRegistryItems[index].title)
          }
       }
   }
}