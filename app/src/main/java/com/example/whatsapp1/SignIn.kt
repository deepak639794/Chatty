package com.example.whatsapp1

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import android.util.Log
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.whatsapp.models.User
import com.example.whatsapp1.MainActivity
import com.example.whatsapp1.R
import com.example.whatsapp1.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.values
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging

class SignIn : AppCompatActivity() {

    lateinit var mauth : FirebaseAuth
    lateinit var database : FirebaseDatabase
    lateinit var _binding : ActivitySignInBinding
    val  binding get() = _binding!!
    var token =""
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_sign_in)


        var emailET = binding.EmailSignIn
        var passET = binding.PasswordSignIn



        var progressBar = ProgressDialog(this)
        progressBar.setTitle("Loging")
        progressBar.setMessage("Loging you Please wait")
        progressBar.setCancelable(true)

        mauth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()



        var signUp = binding.SignUPINSignin

        signUp.setOnClickListener {
            startActivity(Intent(this , SignUp::class.java))
            finish()
        }

        var login: TextView = findViewById(R.id.MyLoginSignIN)
        login.setOnClickListener {
            var email = emailET.text.toString()
            var pass  =passET.text.toString()
            gettoken()
            if (!email.isEmpty() && !pass.isEmpty()) {
                progressBar.show()
                mauth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener {

                        progressBar.dismiss()
                        if (it.isSuccessful)
                        {
                            var uid = FirebaseAuth.getInstance().uid!!
                            FirebaseFirestore.getInstance().collection("Userdata").document(uid)
                                .update("token", token)

                            val pref = getSharedPreferences("LogInOut", MODE_PRIVATE)
                            val edit = pref.edit()
                            edit.putString("LoginDetails", "LoggedIn")
                            edit.commit()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()

                        } else {
                            emailET.error = "Wrong Credentials"
                            emailET.requestFocus()
                            Toast.makeText(this, "${it.exception}", Toast.LENGTH_SHORT).show()
                        }
                    }

            }
            else {
                Toast.makeText(this, "$pass ,   ### $email" , Toast.LENGTH_SHORT).show()
            }
        }




    }
    fun gettoken(){

        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if(it.isSuccessful){
                token = it.getResult().toString()
                Log.d("SignUp", token)

            }
        }

    }
    private fun showerror(input: EditText, s: String) {
        Log.d("check","showerror")
        input.error = s
        input.requestFocus()
    }

}