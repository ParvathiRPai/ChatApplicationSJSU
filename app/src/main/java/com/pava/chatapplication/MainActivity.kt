package com.pava.chatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        login_button_login.setOnClickListener{
            val email=email_edittext_login.text.toString()
            val password=password_edittext_login.text.toString()
            Log.d("MainActivity", "Eamil"+email)
            Log.d("MainActivity", "password"+password)
        }
        back_to_register_textview.setOnClickListener{
            Log.d("MainActivity", "Try to show login activity")
            //Launch
            val intent=Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}