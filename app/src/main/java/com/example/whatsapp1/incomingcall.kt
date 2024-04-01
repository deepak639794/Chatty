package com.example.whatsapp1

import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.whatsapp.models.User
import com.example.whatsapp1.models.Videocall
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import okhttp3.internal.notify
import org.w3c.dom.Text


class incomingcall : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {


        var database : FirebaseDatabase
        var firebase : FirebaseAuth
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_incomingcall)
        var join  : ImageView= findViewById(R.id.Join)
        var decline : ImageView = findViewById(R.id.Reject)
        database = FirebaseDatabase.getInstance()
        firebase = FirebaseAuth.getInstance()
        var uid = firebase.uid!!




        var model : DatabaseReference = database.getReference()
            .child("VideoCall")
            .child(uid)
            .child("name")
        var username = ""
        model.get().addOnCompleteListener {
            var snapshot = it.getResult()
            username = snapshot.value.toString()
            Log.d("MainActivity11111", snapshot.value.toString())
            var callername :TextView= findViewById(R.id.CallerName)


            callername.text = username
        }

       //

//        var username = ""
//        model.get().addOnCompleteListener {
//            var snapshot = it.getResult()
//
//            var value = snapshot.getValue(User::class.java)
//            username = value!!.username
//            Log.d("Chatting1211", value.toString())
//            Log.d("Chatting121", value!!.username)
//        }

        decline.setOnClickListener {

            val updatesMap = mutableMapOf<String, Any>()
            updatesMap["isvideocall"] = "False"
            updatesMap["name"] = ""
            updatesMap["callerid"] = ""
//            var model  = Videocall("False", "", "")



            database.getReference()
                .child("VideoCall")
                .child(uid)
                .updateChildren(updatesMap)


            finish()

        }

        join.setOnClickListener {
            startActivity(Intent(this , VideoCall::class.java))
            finish()
        }




    }
}