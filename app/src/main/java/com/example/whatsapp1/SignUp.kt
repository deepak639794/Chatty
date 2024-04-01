package com.example.whatsapp1

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.whatsapp1.databinding.ActivitySignUpBinding
import com.example.whatsapp.models.User
import com.example.whatsapp1.models.Videocall
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import java.util.zip.Inflater



class SignUp : AppCompatActivity() {

    lateinit var emailEditText :EditText
    lateinit var passEditText: EditText
    lateinit var confirmEditText: EditText
    lateinit var nameEditText: EditText
    lateinit var firebaseApp: FirebaseApp
    lateinit var databse : FirebaseDatabase
    lateinit var mauth : FirebaseAuth
    var name = ""
    var email = ""
    var pass = ""
    var confirm = ""
    var uId = ""
    var token = ""
    private  var _binding : ActivitySignUpBinding ?= null
    private  val binding get() = _binding!!
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        _binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)


        emailEditText   = binding.EmailSignUP
        confirmEditText = binding.ConfirmPassSignUP
        passEditText    = binding.PasswordSignUp1111
        nameEditText = binding.UserName




        var signin =binding.SignInInSignup
        signin.setOnClickListener {
            startActivity(Intent(this , SignIn::class.java))
            finish()
        }

        val progressDialogue = ProgressDialog(this)
        progressDialogue.setTitle("Creating Account")
        progressDialogue.setMessage("We'are Creating yout account..")
        progressDialogue.setCancelable(false)
        gettoken()
        var login = binding.LoginSignUp
        Log.d("SignUp11", token)
        login.setOnClickListener {
            pass = passEditText.text.toString()

            email = emailEditText.text.toString()
            confirm = confirmEditText.text.toString()
            name = nameEditText.text.toString()
            mauth = FirebaseAuth.getInstance()

            databse = FirebaseDatabase.getInstance()
            println(checkIfEmailExists(email))
            // if(checkIfEmailExists(email)){


          //  if(checkcredentials()){
                progressDialogue.show()
                mauth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener {
                    progressDialogue.dismiss()
                    if(it.isSuccessful){

                        Log.d("SignUp11", token)

                        uId = it.getResult().user!!.uid

                       // var user = User(name , pass, email, uId)
                        //Log.d("Chats" , " ${user.username} , ${user.pass}, ${user.email}, ${user.userId}")

                        Log.d("signup11", token)
                        databse.getReference()
                            .child("User")
                            .child(uId)
                            .setValue( User(name , pass, email, uId, token))

//                        val newData = HashMap<String, Any>()
//                        newData["isvidocall"] = "False"

                        val updatesMap = mutableMapOf<String, Any>()
                        updatesMap["isvideocall"] = "False"
                        updatesMap["name"] = ""
                        updatesMap["callerid"] = ""
                        databse.getReference()
                            .child("VideoCall")
                            .child(uId)
                            .updateChildren(updatesMap)


                        var uid = FirebaseAuth.getInstance().uid!!
                        FirebaseFirestore.getInstance().collection("Userdata").document(uid)
                            .set(User(name,pass,email,uId,token))


                        var pref = getSharedPreferences("LogInOut", MODE_PRIVATE)
                        var edit = pref.edit()
                        edit.putString("LoginDetails", "LoggedIn")
                        edit.commit()


                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                    else{

                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }

                }
           // }
            //  }
//            else {
//                Toast.makeText(this, "Email1 is already registered", Toast.LENGTH_SHORT).show()
//            }

        }







    }

    fun checkIfEmailExists(email: String)  : Boolean{
        var check = false
        mauth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result = task.result
                    if (result != null && result.signInMethods != null && result.signInMethods!!.isNotEmpty()) {
                        // Email is already registered

                    } else {
                        check = true
                        // Email is not registered
                        println("Email11 is not registered")
                    }
                } else {
                    // Error occurred
                    val exception = task.exception
                    if (exception is FirebaseAuthUserCollisionException) {
                        // Email is already registered
                        println("Email11 is already registered")
                    } else {
                        // Other errors
                        println("Error: ${exception?.message}")
                    }
                }
            }
        return  check
    }
    private fun checkcredentials() : Boolean{

        if(email.length==0 || !email.contains("@gmail.com")){
            showerror(emailEditText,"invalid Email!")
            return false
        }
        if(pass.length<10){
            showerror(passEditText,"length must be more than 10")
            return false
        }
        if(pass.contains("@")){
            showerror(passEditText,"must Contain @")
            return false
        }
        if(!pass.equals(confirm)){
            showerror(passEditText,"password must be same ")
            return false
        }
        else  return true
    }
    private fun showerror(input: EditText, s: String) {
        Log.d("check","showerror")
        input.error = s
        input.requestFocus()
    }



    fun gettoken(){

        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if(it.isSuccessful){
                token = it.getResult().toString()
                Log.d("SignUp", token)

            }
        }

    }
}