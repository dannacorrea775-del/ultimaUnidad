package com.example.proyecto3

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.proyecto3.databinding.ActivityMainBinding
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        executor = ContextCompat.getMainExecutor(this)

        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(
                        applicationContext,
                        "Error de autenticación: $errString",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(
                        applicationContext,
                        "¡Autenticación exitosa!",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intento = Intent(this@MainActivity, MainActivity2::class.java)
                    startActivity(intento)
                    finish()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(
                        applicationContext,
                        "Autenticación fallida",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Iniciar sesión")
            .setSubtitle("Usa tu huella para acceder a la aplicación")
            .setNegativeButtonText("Cancelar")
            .build()

        binding.root.setOnClickListener {
            solicitarBiometria()
        }

        solicitarBiometria()
    }

    private fun solicitarBiometria() {
        if (puedeUsarBiometria()) {
            biometricPrompt.authenticate(promptInfo)
        }
    }

    private fun puedeUsarBiometria(): Boolean {
        val biometricManager = BiometricManager.from(this)
        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)) {
            BiometricManager.BIOMETRIC_SUCCESS -> true

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Toast.makeText(this, "Este teléfono no tiene sensor de huella.", Toast.LENGTH_LONG)
                    .show()
                false
            }

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Toast.makeText(this, "El sensor no está disponible por ahora.", Toast.LENGTH_LONG)
                    .show()
                false
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Toast.makeText(
                    this,
                    "No hay huellas registradas. Agrega una en Ajustes.",
                    Toast.LENGTH_LONG
                ).show()
                false
            }

            else -> {
                Toast.makeText(this, "La biometría no está disponible.", Toast.LENGTH_LONG).show()
                false
            }
        }
    }
}
