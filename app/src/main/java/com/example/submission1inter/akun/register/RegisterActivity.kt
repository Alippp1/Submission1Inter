package com.example.submission1inter.akun.register

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.submission1inter.akun.login.LoginActivity
import com.example.submission1inter.akun.login.LoginData
import com.example.submission1inter.akun.login.LoginResponse
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
                        Toast.makeText(this@RegisterActivity, "Buat Akun Gagal", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: retrofit2.Call<RegisterResponse>, t: Throwable) {
                    Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
                }
            })
        }

    }
}