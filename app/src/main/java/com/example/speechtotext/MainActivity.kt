package com.example.speechtotext

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent.*
import android.speech.SpeechRecognizer
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

private const val PERMISSION_CODE: Int = 1000

class MainActivity : AppCompatActivity() {

    var text = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkVoiceCommandPermission()

        val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        val speechRecognizerIntent = Intent(ACTION_RECOGNIZE_SPEECH)
        speechRecognizerIntent.putExtra(EXTRA_LANGUAGE_MODEL, LANGUAGE_MODEL_FREE_FORM)
        speechRecognizerIntent.putExtra(EXTRA_LANGUAGE_MODEL, Locale.ENGLISH)

        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                //Toast.makeText(this@MainActivity, "Empezamos a grabar", Toast.LENGTH_LONG).show()
            }

            override fun onRmsChanged(rmsdB: Float) {

            }

            override fun onBufferReceived(buffer: ByteArray?) {
                //Toast.makeText(this@MainActivity, "onBufferReceived", Toast.LENGTH_LONG).show()
            }

            override fun onPartialResults(partialResults: Bundle?) {
                //Toast.makeText(this@MainActivity, "onPartialResults", Toast.LENGTH_LONG).show()
            }

            override fun onEvent(eventType: Int, params: Bundle?) {
                //Toast.makeText(this@MainActivity, "onEvent", Toast.LENGTH_LONG).show()
            }

            override fun onBeginningOfSpeech() {
                //Toast.makeText(this@MainActivity, "onBeginningOfSpeech", Toast.LENGTH_LONG).show()
            }

            override fun onEndOfSpeech() {
                //Toast.makeText(this@MainActivity, "onEndOfSpeech", Toast.LENGTH_LONG).show()
            }

            override fun onError(error: Int) {
                //Toast.makeText(this@MainActivity, "Sin permisos", Toast.LENGTH_LONG).show()
            }

            override fun onResults(results: Bundle?) {
                val matchesFound = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                matchesFound?.let {
                    text = it[0]
                    txv_text.text = text
                }
            }
        })


        img_microphone.setOnTouchListener { view, event ->
            view?.performClick()
            Toast.makeText(this@MainActivity, "Event ${event.action}", Toast.LENGTH_LONG).show()
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    speechRecognizer.startListening(speechRecognizerIntent)
                    val params = img_microphone.layoutParams as ViewGroup.LayoutParams
                    params.height = 270
                    params.width = 270
                    img_microphone.layoutParams = params
                }
                MotionEvent.ACTION_UP -> {
                    speechRecognizer.stopListening()
                    val params = img_microphone.layoutParams as ViewGroup.LayoutParams
                    params.height = 300
                    params.width = 300
                    img_microphone.layoutParams = params
                }
                else -> {
                }
            }
            true
        }
    }

    private fun checkVoiceCommandPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nose = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
            if (!nose) {
                requestPermissions(
                    arrayOf(Manifest.permission.RECORD_AUDIO),
                    PERMISSION_CODE
                )
            }
        }
    }

}