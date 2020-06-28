package com.andb.apps.aspen.ui.login

import androidx.compose.Composable
import androidx.compose.mutableStateOf
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.core.clip
import androidx.ui.foundation.Image
import androidx.ui.foundation.Text
import androidx.ui.foundation.shape.corner.CircleShape
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.layout.*
import androidx.ui.material.Button
import androidx.ui.material.MaterialTheme
import androidx.ui.material.OutlinedTextField
import androidx.ui.material.Surface
import androidx.ui.res.vectorResource
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import com.andb.apps.aspen.android.BuildConfig
import com.andb.apps.aspen.android.R
import com.andb.apps.aspen.state.UserAction
import com.andb.apps.aspen.util.ActionHandler

@Composable
fun LoginScreen(actionHandler: ActionHandler) {

    val username = mutableStateOf(BuildConfig.TEST_USERNAME)
    val password = mutableStateOf(BuildConfig.TEST_PASSWORD)
    val titleText = mutableStateOf("GradePoint")

    Column(
        horizontalGravity = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Surface(shape = CircleShape, elevation = 4.dp) {
            Image(
                asset = vectorResource(id = R.drawable.ic_fullbleed_gradepoint_icon),
                modifier = Modifier.clip(CircleShape).size(144.dp)
            )
        }
        Text(
            text = titleText.value,
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(top = 32.dp)
        )
        OutlinedTextField(
            value = username.value,
            onValueChange = { username.value = it },
            label = { Text(text = "Username") },
            modifier = Modifier.padding(top = 32.dp)
        )
        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text(text = "Password") },
            modifier = Modifier.padding(top = 16.dp)
        )
        Button(
            onClick = {
                titleText.value = "Clicked (${password.value.length})"
                val sanitizedUsername = username.value.replace("\\p{C}", "")
                val sanitizedPassword = password.value.replace("\\p{C}", "")
                actionHandler.handle(UserAction.Login(sanitizedUsername, sanitizedPassword))
            },
            modifier = Modifier.padding(top = 32.dp),
            shape = RoundedCornerShape(32.dp),
            padding = InnerPadding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 16.dp)

        ) {
            Text(text = "Log in".toUpperCase(), color = MaterialTheme.colors.onPrimary)
        }
    }
}

@Preview
@Composable
private fun PreviewLogin(){
    LoginScreen(actionHandler = ActionHandler { true })
}