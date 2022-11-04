package com.example.submission1inter.akun.login

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Trace.isEnabled
import android.util.Log
import android.widget.*
import androidx.activity.viewModels
import androidx.core.os.TraceCompat.isEnabled
import androidx.lifecycle.ViewModelProvider
import androidx.tracing.Trace.isEnabled
import com.example.submission1inter.MainActivity
import com.example.submission1inter.ValidasiLoginViewModel
import com.example.submission1inter.akun.register.RegisterActivity
import com.example.submission1inter.data.api.ApiConfig
import com.example.submission1inter.databinding.ActivityLoginBinding
import com.example.submission1inter.model.ListStoryActivity
import com.example.submission1inter.model.ViewModelFactory
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var btnLogin : Button

    private fun obtainViewModel(activity: AppCompatActivity) : ValidasiLoginViewModel{
       val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity,factory)[ValidasiLoginViewModel::class.java]
    }

    private val loginViewModel : LoginViewModel by viewModels()
    private lateinit var validasiLoginViewModel: ValidasiLoginViewModel

    private lateinit var etRegister: TextView
    private lateinit var loginData: LoginData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        validasiLoginViewModel = obtainViewModel(this)
        btnLogin = binding.btnLogin
        etRegister = binding.tvRegister

        btnLogin.setOnClickListener{
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            loginData = LoginData(email, password)
            loginViewModel.getLogin(email, password, this)
            loginViewModel.getLoginResult().observe(this){
                validasiLoginViewModel.setLogin(it)
                loginViewModel.getLoginResponse().observe(this){ loginResponse ->
                    loginResponse.loginResult?.token?.let { that ->
                       println("token $that")
                        validasiLoginViewModel.setToken(that)
                    }
                }
                startActivity(Intent(this@LoginActivity, ListStoryActivity::class.java))
                finish()
            }
        }

        etRegister.setOnClickListener{
            Toast.makeText(this@LoginActivity,"Register", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)

        }

    }
}
