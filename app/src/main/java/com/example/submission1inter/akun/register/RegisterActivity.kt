package com.example.submission1inter.akun.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.submission1inter.akun.login.LoginActivity
import com.example.submission1inter.data.api.ApiConfig
import com.example.submission1inter.databinding.ActivityRegisterBinding
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {


    private lateinit var binding: ActivityRegisterBinding
    private lateinit var btnRegister : Button

    private lateinit var RegisterData: RegisterData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btnRegister = binding.btnRegister

        btnRegister.setOnClickListener{
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            RegisterData = RegisterData(name,email,password)
            val client = ApiConfig.getApiService().UserRegister(name,email, password)
            client.enqueue(object : retrofit2.Callback<RegisterResponse> {
                override fun onResponse(
                    call: retrofit2.Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null){
                            Toast.makeText(this@RegisterActivity, responseBody.message.toString(),Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                            startActivity(intent)
                        }
                    } else {
                        Toast.makeText(this@RegisterActivity, "Minimal 6 Karakter", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: retrofit2.Call<RegisterResponse>, t: Throwable) {
                    Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
                }
            })
        }

        playAnimation()
    }

    private  fun playAnimation(){

        val card = ObjectAnimator.ofFloat(binding.card, View.ALPHA, 1f).setDuration(1000)

        AnimatorSet().apply {
            playTogether(card)
            start()
        }
    }
}