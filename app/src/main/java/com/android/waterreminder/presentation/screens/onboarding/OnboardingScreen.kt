package com.android.waterreminder.presentation.screens.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import com.android.waterreminder.domain.model.onboardingPages
import com.android.waterreminder.presentation.screens.userinfo.UserInfoBottomSheet
import com.android.waterreminder.ui.theme.lightGray
import com.android.waterreminder.ui.theme.primary
import com.android.waterreminder.ui.theme.white
import kotlinx.coroutines.launch

class OnboardingScreen : Screen {
    @Composable
    override fun Content() {
        val viewmodel = getViewModel<OnboardingViewModel>()
        OnboardingScreenContent(viewmodel)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun OnboardingScreenContent(
    viewModel: OnboardingViewModel
) {
    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
    val currentPage = pagerState.currentPage
    val scope = rememberCoroutineScope()

    var showUserInfoSheet by remember { mutableStateOf(false) }
    val fullName = remember { mutableStateOf("") }
    val lastName = remember { mutableStateOf("") }

    if (showUserInfoSheet) {
        UserInfoBottomSheet(
            fullName = fullName,
            lastname = lastName,
            onDismiss = { showUserInfoSheet = false },
            onContinue = {
                showUserInfoSheet = false
                viewModel.finishOnboarding(fullName.value, lastName.value)
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onBackground)
    ) {
        /* if (currentPage in 1..onboardingPages.lastIndex) {
             Icon(
                 modifier = Modifier
                     .padding(top = 62.dp, start = 16.dp)
                     .size(20.dp)
                     .clickable{
                         if (currentPage > 0) {
                             scope.launch {
                                 pagerState.animateScrollToPage(currentPage - 1)
                             }
                         }
                     },
                 tint = primary,
                 painter = painterResource(R.drawable.ic_back),
                 contentDescription = null
             )
         }*/
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
        ) { page ->
            val item = onboardingPages[page]

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(top = 120.dp)
                    .fillMaxWidth()
            ) {

                Image(
                    painter = painterResource(id = item.imageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .size(250.dp)
                )
                Spacer(modifier = Modifier.height(25.dp))
                Text(
                    color = MaterialTheme.colorScheme.onPrimary,
                    text = item.title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    ),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    color = MaterialTheme.colorScheme.onPrimary,
                    text = item.description,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(top = 43.dp)
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                repeat(onboardingPages.size) { index ->
                    val isSelected = currentPage == index
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(width = 23.dp, height = 6.dp)
                            .clip(RoundedCornerShape(50.dp))
                            .background(
                                if (isSelected) primary
                                else lightGray,
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.padding(top = 70.dp))
            Button(
                onClick = {
                    if (currentPage < onboardingPages.lastIndex) {
                        scope.launch {
                            pagerState.animateScrollToPage(currentPage + 1)
                        }
                    } else {
                        showUserInfoSheet = true
                    }
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (currentPage == onboardingPages.lastIndex)
                        "Get Started"
                    else
                        "Next",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = white,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}
