package com.example.submission1inter.akun.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.submission1inter.ValidasiLoginViewModel
import com.example.submission1inter.akun.register.RegisterActivity
import com.example.submission1inter.databinding.ActivityLoginBinding
import com.example.submission1inter.model.ListStoryActivity
import com.example.submission1inter.model.ViewModelFactory

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

        playAnimation()
    }

    private  fun playAnimation(){

        val card = ObjectAnimator.ofFloat(binding.card, View.ALPHA, 1f).setDuration(1500)

        AnimatorSet().apply {
            playTogether(card)
            start()
        }
    }
}
