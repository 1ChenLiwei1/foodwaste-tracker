package com.example.foodwaste.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.foodwaste.data.local.FoodItem
import com.example.foodwaste.data.product.ProductLookup
import com.example.foodwaste.ui.InventoryViewModel
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGetImage::class)
@Composable
fun ScanScreen(
    vm: InventoryViewModel,
    onFinish: () -> Unit,
    onItemAdded: (String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = context as LifecycleOwner
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val scope = rememberCoroutineScope()

    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    LaunchedEffect(Unit) {
        if (!hasPermission) {
            Toast.makeText(
                context,
                "Please grant Camera permission in system settings.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Scan Barcode") },
                navigationIcon = {
                    IconButton(onClick = onFinish) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->

        if (!hasPermission) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
            contentAlignment = Alignment.Center
            ) {
                Text("Camera permission not granted.")
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                AndroidView(factory = { ctx ->
                    val previewView = PreviewView(ctx)

                    val cameraProvider = cameraProviderFuture.get()
                    val preview = androidx.camera.core.Preview.Builder()
                        .build()
                        .also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }

                    val analyzer = ImageAnalysis.Builder()
                        .build()
                        .also { analysis ->
                            analysis.setAnalyzer(
                                ContextCompat.getMainExecutor(ctx)
                            ) { imageProxy ->
                                processImageProxy(
                                    imageProxy = imageProxy,
                                    vm = vm,
                                    context = ctx,
                                    scope = scope,
                                    onItemAdded = onItemAdded
                                )
                            }
                        }

                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            CameraSelector.DEFAULT_BACK_CAMERA,
                            preview,
                            analyzer
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    previewView
                })
            }
        }
    }
}

private var lastHandledCode: String? = null

@OptIn(ExperimentalGetImage::class)
private fun processImageProxy(
    imageProxy: ImageProxy,
    vm: InventoryViewModel,
    context: android.content.Context,
    scope: CoroutineScope,
    onItemAdded: (String) -> Unit
) {
    val mediaImage = imageProxy.image ?: run {
        imageProxy.close()
        return
    }

    val image = InputImage.fromMediaImage(
        mediaImage,
        imageProxy.imageInfo.rotationDegrees
    )

    // Barcode recognition only
    val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_EAN_13,
            Barcode.FORMAT_EAN_8,
            Barcode.FORMAT_UPC_A,
            Barcode.FORMAT_UPC_E
        )
        .build()

    val scanner = BarcodeScanning.getClient(options)

    scanner.process(image)
        .addOnSuccessListener { barcodes ->
            for (barcode in barcodes) {
                val raw = barcode.rawValue ?: continue
                if (raw == lastHandledCode) continue
                lastHandledCode = raw

                Log.d("Scan", "Scanned: $raw")

                // Call OFF + local database
                scope.launch {
                    val info = ProductLookup.lookupProduct(raw)

                    if (info == null) {
                        Toast.makeText(
                            context,
                            "Unknown product ($raw)",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val expiry = LocalDate.now().plusDays(info.defaultShelfDays)
                        vm.addItem(
                            FoodItem(
                                name = info.name,
                                expiryDate = expiry,
                                quantity = 1
                            )
                        )
                        onItemAdded(info.name)
                    }
                }
            }
        }
        .addOnFailureListener { e ->
            Log.e("Scan", "Error: ${e.message}", e)
        }
        .addOnCompleteListener {
            imageProxy.close()
        }
}




