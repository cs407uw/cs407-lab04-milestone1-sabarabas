package com.cs407.lab4_milestone1

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    private val TAG = "MyActivity"
    private var job : Job? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.start).setOnClickListener() {
            startDownload(findViewById<View>(R.id.main))
        }

        findViewById<Button>(R.id.stop).setOnClickListener() {
            stopDownload(findViewById<View>(R.id.main))
        }
    }

    @SuppressLint("SetTextI18n")
    private suspend fun mockFileDownloader() {
        withContext(Dispatchers.Main) {
            findViewById<Button>(R.id.start).text = getString(R.string.download)
        }
        for(downloadProgress in 0..100 step 10) {
            Log.d(TAG, "Download Progress $downloadProgress%")
            withContext(Dispatchers.Main) {
                findViewById<TextView>(R.id.progressText).text = getString(R.string.download_progress) + " $downloadProgress%"
            }
            delay(1000)
        }
        withContext(Dispatchers.Main) {
            findViewById<Button>(R.id.start).text = getString(R.string.start_button)
        }
    }

    private fun startDownload(view: View) {
        job = CoroutineScope(Dispatchers.Default).launch {
            mockFileDownloader()
        }
    }

    private fun stopDownload(view: View) {
        job?.cancel()
        findViewById<Button>(R.id.start).text = getString(R.string.start_button)
        findViewById<TextView>(R.id.progressText).text = getString(R.string.download_cancelled)
    }
}