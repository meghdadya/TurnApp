package com.sample.turnapp.feature.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.sample.turnapp.core.ui.theme.TurnAppTheme
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel() ,
    onNavigateToPeople: () -> Unit = {},
    onNavigateToAppointments: () -> Unit = {}
) {

    val state = viewModel.currentState

    val colors = TurnAppTheme.colors
    val dimens = TurnAppTheme.dimens
    val typography = TurnAppTheme.typography

    // -------------------- EFFECTS --------------------
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {

                HomeContract.HomeUiEffect.NavigateToPeople ->
                    onNavigateToPeople()

                HomeContract.HomeUiEffect.NavigateToAppointments ->
                    onNavigateToAppointments()

                HomeContract.HomeUiEffect.ShowError -> {
                    // optional snackbar later
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(TurnAppTheme.dimens.paddingMedium)
            .padding(dimens.paddingSemiSmall)
    ) {

        // -------------------- HEADER --------------------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(dimens.buttonRadiusLarge))
                .background(colors.backgroundBlueContainer)
                .padding(vertical = dimens.paddingLarge),
            contentAlignment = Alignment.Center
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Text(
                    text = "سامانه نوبت‌دهی",
                    style = typography.title.bold,
                    color = colors.textPrimary
                )

                Spacer(modifier = Modifier.height(dimens.paddingXSmall))

                Text(
                    text = "خوش آمدید",
                    style = typography.body1.medium,
                    color = colors.textSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(dimens.paddingMedium))

        // -------------------- MANAGEMENT CARD --------------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(dimens.insideFrameRadius))
                .background(colors.backgroundElevatedItems)
                .padding(dimens.paddingMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(dimens.iconWidthLarge)
                    .clip(RoundedCornerShape(dimens.buttonRadiusMedium))
                    .background(colors.backgroundButtonPrimary),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = colors.textColoredButton,
                    modifier = Modifier.size(dimens.iconWidthMedium)
                )
            }

            Spacer(modifier = Modifier.width(dimens.paddingSemiSmall))

            Column {

                Text(
                    text = "مدیریت افراد و نوبت‌ها",
                    style = typography.title.medium,
                    color = colors.textPrimary
                )

                Spacer(modifier = Modifier.height(dimens.paddingXSmall))

                Text(
                    text = "سیستم نوبت‌دهی هوشمند",
                    style = typography.subtitle.medium,
                    color = colors.textSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(dimens.paddingMedium))

        // -------------------- DASHBOARD CARDS --------------------
        Row(horizontalArrangement = Arrangement.spacedBy(dimens.paddingSemiSmall)) {

            DashboardCard(
                modifier = Modifier.weight(1f),
                title = "لیست نوبت‌ها",
                subtitle = "${state.appointments} نوبت",
                icon = Icons.Default.Star,
                iconBackground = colors.backgroundYellowContainer,
                iconTint = colors.textYellow,
                onClick = {
                    viewModel.setEvent(HomeContract.HomeUiEvent.OnAppointmentsClick)
                }
            )

            DashboardCard(
                modifier = Modifier.weight(1f),
                title = "لیست افراد",
                subtitle = "${state.peopleCount} نفر",
                icon = Icons.Default.Star,
                iconBackground = colors.backgroundBlueContainer,
                iconTint = colors.textBlue,
                onClick = {
                    viewModel.setEvent(HomeContract.HomeUiEvent.OnPeopleClick)
                }
            )
        }

        Spacer(modifier = Modifier.height(dimens.paddingMedium))

        // -------------------- STATS --------------------
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(dimens.insideFrameRadius))
                .background(colors.backgroundElevatedItems)
                .padding(dimens.paddingMedium)
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = colors.textPrimary
                )

                Spacer(modifier = Modifier.width(dimens.paddingSmall))

                Text(
                    text = "آمار سریع",
                    style = typography.subtitle.medium,
                    color = colors.textPrimary
                )
            }

            Spacer(modifier = Modifier.height(dimens.paddingSemiLarge))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                StatItem(
                    number = state.deletedCount.toString(),
                    title = "افراد حذف شده",
                    color = colors.primaryAction
                )

                StatItem(
                    number = state.activePeopleCount.toString(),
                    title = "افراد فعال",
                    color = colors.active
                )
            }
        }
    }
}

@Composable
private fun DashboardCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconBackground: Color,
    iconTint: Color,
    onClick: () -> Unit
) {
    val colors = TurnAppTheme.colors
    val typography = TurnAppTheme.typography
    val dimens = TurnAppTheme.dimens

    Column(
        modifier = modifier
            .clip(TurnAppTheme.shapes.roundedLarge)
            .background(colors.backgroundElevatedItems)
            .clickable { onClick.invoke() }
            .padding(dimens.paddingMedium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(TurnAppTheme.shapes.roundedMedium)
                .background(iconBackground),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint
            )
        }

        Spacer(modifier = Modifier.height(dimens.paddingSmall))

        Text(
            text = title,
            style = typography.subtitle.medium,
            color = colors.textPrimary
        )

        Spacer(modifier = Modifier.height(dimens.paddingXSmall))

        Text(
            text = subtitle,
            style = typography.subtitle.medium,
            color = colors.textSecondary
        )
    }
}

@Composable
private fun StatItem(
    number: String,
    title: String,
    color: Color
) {

    val typography = TurnAppTheme.typography
    val colors = TurnAppTheme.colors
    val dimens = TurnAppTheme.dimens

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = number,
            style = typography.subtitle.medium,
            color = color
        )

        Spacer(modifier = Modifier.height(dimens.paddingXSmall))

        Text(
            text = title,
            style = typography.body2.medium,
            color = colors.textSecondary
        )
    }
}



