package com.example.myapplication.presentation.company_listings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.presentation.destinations.CompanyDetailsScreenDestination
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination(start = true)
fun CompanyListingScreen(
    navigator: DestinationsNavigator,
    viewModel: CompanyListingViewModel = hiltViewModel()
) {
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = viewModel.state.isRefreshing)

    val state = viewModel.state

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = {
                viewModel.onEvent(CompanyListingEvent.OnSearchQueryChanged(query = it))
            },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            placeholder = {
                Text(
                    text = "Search Any stock",
                    maxLines = 1,
                )
            },
            singleLine = true
        )

        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                viewModel.onEvent(CompanyListingEvent.RefreshCompanyListings)
            }) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.companyList.size) { item ->
                    CompanyItem(
                        company = state.companyList[item],
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                 navigator.navigate(CompanyDetailsScreenDestination(
                                     symbol = state.companyList[item].symbol)
                                 )
                            }.padding(16.dp))
                    if(item < state.companyList.size) {
                        Divider(
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }

    }

}