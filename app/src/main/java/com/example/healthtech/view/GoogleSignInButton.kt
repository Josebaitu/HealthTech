package com.example.healthtech.view

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.healthtech.R
import com.example.healthtech.viewmodel.LoginViewModel
import com.example.healthtech.viewmodel.SignUpViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException


@Composable
fun GoogleSignInButton(
    viewModel: LoginViewModel,
    onNavigateToHome: () -> Unit) {
    val context = LocalContext.current

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("242133128346-0ntjhit6vv5h3n9p95r1q2t6jv140t7u.apps.googleusercontent.com")
        .requestEmail()
        .build()

    val googleSignInClient = GoogleSignIn.getClient(context, gso)

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            account?.idToken?.let { token ->
                viewModel.signInWithGoogle(
                    token,
                    onSuccess = onNavigateToHome,
                    onError = { error -> Log.e("GoogleError", error) }
                )
            }
        } catch (e: ApiException) {
            Log.e("GoogleError", "Código de error: ${e.statusCode}")
        }
    }

    OutlinedButton(
        onClick = {
            launcher.launch(googleSignInClient.signInIntent)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Icon(painter = painterResource(
            id = R.drawable.ic_googleicon_logo),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(20.dp))

        Spacer(modifier = Modifier.width(8.dp))

        Text(text = "Continuar con Google")
    }
}