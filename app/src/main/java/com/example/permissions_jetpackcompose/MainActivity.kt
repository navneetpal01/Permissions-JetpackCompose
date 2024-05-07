package com.example.permissions_jetpackcompose

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import com.example.permissions_jetpackcompose.ui.theme.PermissionsJetpackComposeTheme


class MainActivity : ComponentActivity() {
    private val mainViewModel by viewModels<MainViewModel>()

    private val permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.Transparent.toArgb(),
                Color.Transparent.toArgb()
            )
        )
        super.onCreate(savedInstanceState)
        setContent {
            PermissionsJetpackComposeTheme {
                val showDialog = mainViewModel.showDialog.collectAsState().value
                val launchAppSettings = mainViewModel.launchAppSettings.collectAsState().value

                val permissionResultActivityLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions(),
                    onResult = { result ->
                        permissions.forEach { permission ->
                            if (result[permission] == false){

                            }
                        }
                    }
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    Button(
                        onClick = {
                            permissions.forEach { permission ->
                                val isGranted =
                                    checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
                                if (!isGranted) {
                                    //This function tells if I can ask for the permission or not basically it tracks the user's past history
                                    if (shouldShowRequestPermissionRationale(permission)) {
                                        mainViewModel.updateShowDialog(true)
                                    } else {

                                    }
                                }
                            }
                        }
                    ) {
                        Text(text = "Request Permission")
                    }


                }

            }
        }
    }

    @Composable
    fun PermissionDialog(
        onDismiss: () -> Unit,
        onConfirm: () -> Unit,
    ) {

        AlertDialog(
            modifier = Modifier
                .fillMaxWidth(),
            onDismissRequest = onDismiss,
            confirmButton = {
                Button(
                    onClick = onConfirm
                ) {
                    Text(text = "Ok")
                }
            },
            title = {
                Text(
                    text = "Camera and Microphone permissions are needed",
                    fontWeight = FontWeight.SemiBold
                )
            },
            text = {
                Text(text = "This app needs access to your camera and microphone")
            },
        )
    }

}
