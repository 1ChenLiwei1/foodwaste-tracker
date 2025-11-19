package com.example.foodwaste.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
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
import com.example.foodwaste.ui.InventoryViewModel
import com.example.foodwaste.data.local.FoodItem
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import androidx.compose.foundation.layout.padding

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

    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // MLKit 条形码选项
    val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            com.google.mlkit.vision.barcode.common.Barcode.FORMAT_EAN_13,
            com.google.mlkit.vision.barcode.common.Barcode.FORMAT_QR_CODE
        )
        .build()

    val scanner = BarcodeScanning.getClient(options)
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Scan Barcode") },
                navigationIcon = {
                    IconButton(onClick = onFinish) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->

        if (!hasPermission) {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Camera permission required.")
            }
            return@Scaffold
        }

        Box(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            AndroidView(factory = { ctx ->
                val previewView = PreviewView(ctx)

                val cameraProvider = cameraProviderFuture.get()

                val preview = androidx.camera.core.Preview.Builder()
                    .build()
                    .apply {
                        setSurfaceProvider(previewView.surfaceProvider)
                    }

                val analyzer = ImageAnalysis.Builder()
                    .build()
                    .apply {
                        setAnalyzer(ContextCompat.getMainExecutor(ctx)) { imageProxy ->
                            analyzeBarcode(
                                imageProxy,
                                scanner,
                                vm,
                                scope,
                                onItemAdded,
                                onFinish
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
                    Log.e("CameraX", "Camera binding failed", e)
                }

                previewView
            })
        }
    }
}

@OptIn(ExperimentalGetImage::class)
private fun analyzeBarcode(
    proxy: ImageProxy,
    scanner: com.google.mlkit.vision.barcode.BarcodeScanner,
    vm: InventoryViewModel,
    scope: kotlinx.coroutines.CoroutineScope,
    onItemAdded: (String) -> Unit,
    onFinish: () -> Unit
) {
    val media = proxy.image ?: return proxy.close()
    val image = InputImage.fromMediaImage(media, proxy.imageInfo.rotationDegrees)

    scanner.process(image)
        .addOnSuccessListener { results ->
            if (results.isNotEmpty()) {
                val barcode = results.first()
                val code = barcode.rawValue ?: ""

                Log.d("SCAN", "Found barcode: $code")

                val name = when {
                    code.startsWith("47") -> "Eggs"
                    code.startsWith("88") -> "Milk"
                    else -> "Unknown Item"
                }

                scope.launch {
                    vm.addItem(
                        FoodItem(
                            name = name,
                            expiryDate = LocalDate.now().plusDays(7)
                        )
                    )
                }

                onItemAdded(name)
                onFinish()
            }
        }
        .addOnCompleteListener {
            proxy.close()
        }
}



