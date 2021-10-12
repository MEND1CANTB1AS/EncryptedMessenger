package com.example.messenger

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.messenger.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference

class RegisterActivity : AppCompatActivity()
{

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var refUsers: DatabaseReference
    private var firebaseUserID: String = ""


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbarRegister
        setSupportActionBar(toolbar)

        supportActionBar!!.title = "Register"
        toolbar.setTitleTextColor(Color.WHITE)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            val intent = Intent(this@RegisterActivity, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        mAuth = FirebaseAuth.getInstance()

        binding.registerButton.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser()
    {
        val username: String = binding.usernameRegister.text.toString()
        val email: String = binding.emailRegister.text.toString()
        val password: String = binding.passwordRegister.text.toString()

        if (username == "")
        {
            Toast.makeText(this@RegisterActivity, "please enter username.", Toast.LENGTH_LONG)
                .show()
        }
        else if (email == "")
        {
            Toast.makeText(this@RegisterActivity, "please enter email.", Toast.LENGTH_LONG).show()
        }
        else if (password == "")
        {
            Toast.makeText(this@RegisterActivity, "please enter password.", Toast.LENGTH_LONG)
                .show()
        }
        else
        {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    firebaseUserID = mAuth.currentUser!!.uid
                    refUsers = FirebaseDatabase.getInstance().reference.child("Users")
                        .child(firebaseUserID)

                    val userHashMap = HashMap<String, Any>()
                    userHashMap["uid"] = firebaseUserID
                    userHashMap["username"] = username
                    userHashMap["profile"] =
                        "https://firebasestorage.googleapis.com/v0/b/messengerapp-97e68.appspot.com/o/empty_pofile_image.png?alt=media&token=90b67474-c112-4c01-b1ee-4283c3166c90"
                    userHashMap["cover"] =
                        "https://firebasestorage.googleapis.com/v0/b/messengerapp-97e68.appspot.com/o/OIP.jfif?alt=media&token=763c46f6-9de6-4dda-b12e-47748f5260ab"
                    userHashMap["status"] = "offline"
                    userHashMap["search"] = username.lowercase()
                    userHashMap["facebook"] = "https://m.facebook.com"
                    userHashMap["instagram"] = "https://m.instagram.com"
                    userHashMap["website"] = "https://www.google.com"

                    refUsers.updateChildren(userHashMap)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                finish()
                            }
                        }
                    }
                else
                {
                    Toast.makeText(this@RegisterActivity,
                                    "Error Message." + task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}