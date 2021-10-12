package com.example.messenger

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.messenger.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbarLogin
        setSupportActionBar(toolbar)

        supportActionBar!!.title = "Login"
        toolbar.setTitleTextColor(Color.WHITE)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            val intent = Intent(
                this@LoginActivity,
                WelcomeActivity::class.java
            )
            startActivity(intent)
            finish()
        }

        mAuth = FirebaseAuth.getInstance()

        binding.loginButton.setOnClickListener {
            loginUser()
        }


    }

    private fun loginUser()
    {
        val username: String = binding.usernameLogin.text.toString()
        val password: String = binding.passwordLogin.text.toString()

        if (username == "")
        {
            Toast.makeText(this@LoginActivity, "Please Enter Username.", Toast.LENGTH_LONG).show()
        }
        else if (password == "")
        {
            Toast.makeText(this@LoginActivity, "Please Enter Password.", Toast.LENGTH_LONG).show()
        }
        else
        {
            mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful)
                    {
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    }
                    else
                    {
                        Toast.makeText(
                            this@LoginActivity,
                            "Error Message: " + task.exception!!.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }
        }
    }
}
