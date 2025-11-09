@file:OptIn(androidx.camera.core.ExperimentalGetImage::class)

package com.example.foodwaste.ui.screens

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
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
import androidx.appcompat.app.AlertDialog
import com.example.foodwaste.data.local.FoodItem
import com.example.foodwaste.ui.InventoryViewModel
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.threeten.bp.LocalDate
import java.io.BufferedReader
import java.io.InputStreamReader


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanScreen(
    vm: InventoryViewModel,
    onFinish: () -> Unit,
    onItemAdded: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val lifecycleOwner = context as LifecycleOwner
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val scope = rememberCoroutineScope()

    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    LaunchedEffect(Unit) {
        if (!hasPermission) {
            Toast.makeText(context, "Please grant camera permission manually.", Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Scan Barcode") },
                navigationIcon = {
                    IconButton(onClick = { onFinish() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (hasPermission) {
            Box(Modifier.fillMaxSize().padding(padding)) {
                AndroidView(factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = androidx.camera.core.Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    val analyzer = ImageAnalysis.Builder().build().also {
                        it.setAnalyzer(ContextCompat.getMainExecutor(ctx)) { imageProxy ->
                            processImageProxy(imageProxy, vm, ctx, scope, onItemAdded)
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
        } else {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Camera permission not granted.")
            }
        }
    }
}

private fun loadBarcodeMap(context: android.content.Context): Map<String, String> {
    return try {
        val inputStream = context.assets.open("barcode_map.json")
        val reader = BufferedReader(InputStreamReader(inputStream))
        val jsonString = reader.readText()
        reader.close()
        val jsonObject = JSONObject(jsonString)
        val map = mutableMapOf<String, String>()
        val keys = jsonObject.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            map[key] = jsonObject.getString(key)
        }
        map
    } catch (e: Exception) {
        e.printStackTrace()
        emptyMap()
    }
}

/**
 *  图像分析逻辑：调用 ML Kit 扫描条码
 */
private fun processImageProxy(
    imageProxy: ImageProxy,
    vm: InventoryViewModel,
    context: android.content.Context,
    scope: kotlinx.coroutines.CoroutineScope,
    onItemAdded: (String) -> Unit
) {
    val mediaImage = imageProxy.image ?: run {
        imageProxy.close()
        return
    }
    val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
    val scanner = BarcodeScanning.getClient()
    scanner.process(image)
        .addOnSuccessListener { barcodes ->
            for (barcode in barcodes) {
                handleBarcode(barcode, vm, context, scope, onItemAdded)
            }
        }
        .addOnFailureListener { e -> Log.e("MLKit", "Error: ${e.message}") }
        .addOnCompleteListener { imageProxy.close() }
}

private fun handleBarcode(
    barcode: Barcode,
    vm: InventoryViewModel,
    context: android.content.Context,
    scope: kotlinx.coroutines.CoroutineScope,
    onItemAdded: (String) -> Unit
) {
    val rawValue = barcode.rawValue ?: return
    val numeric = rawValue.filter { it.isDigit() }
    val barcodeMap = loadBarcodeMap(context)
    val mappedName = barcodeMap[numeric] ?: "Unknown Item ($numeric)"

    if (lastScannedCode == numeric) return
    lastScannedCode = numeric

    (context as? ComponentActivity)?.runOnUiThread {
        AlertDialog.Builder(context)
            .setTitle("Add Item")
            .setMessage("Add \"$mappedName\" to inventory?")
            .setPositiveButton("Add") { _: DialogInterface, _: Int ->
                scope.launch {
                    vm.addItem(
                        FoodItem(name = mappedName, expiryDate = LocalDate.now().plusDays(7))
                    )
                    onItemAdded(mappedName)
                }
            }
            .setNegativeButton("Cancel", null)
            .setOnDismissListener { _: DialogInterface -> lastScannedCode = null }
            .show()
    }
}

private var lastScannedCode: String? = null

