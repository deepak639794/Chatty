package com.example.whatsapp1


import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whatsapp.models.User
import com.example.whatsapp1.chats.CallsDatabase
import com.example.whatsapp1.chats.ChatAdaptor
import com.example.whatsapp1.databinding.ActivityChattingScreenBinding
import com.example.whatsapp1.models.SenderMSGModel
import com.example.whatsapp1.models.Videocall
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.IOException
import java.util.Date

import okhttp3.Callback;
import okhttp3.Response


class ChattingScreen : AppCompatActivity() {



    private var _binding :ActivityChattingScreenBinding? = null
    private  val binding get() = _binding!!
    lateinit var firebaseauth : FirebaseAuth
    lateinit var database : FirebaseDatabase
    lateinit var otheruser : User
    lateinit var otherusertoken :String

    override fun onCreate(savedInstanceState: Bundle?) {



//        otheruser.username = intent.getStringExtra("username").toString()
//        otheruser.userId = intent.getStringExtra("userid").toString()
//        otheruser.token = intent.getStringExtra("token").toString()





        super.onCreate(savedInstanceState)


        _binding = ActivityChattingScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        otherusertoken = intent.getStringExtra("token").toString()
        Log.d("chattingScreen11", intent.getStringExtra("userid").toString())



        firebaseauth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val senderId = firebaseauth.uid
        val usernameTV = binding.ChattingUserName
        val username = intent.getStringExtra("username")
        val recieverId = intent.getStringExtra("userid")

        usernameTV.text = username





        var back : ImageView = binding.BackArray
        back.setOnClickListener {
            finish()
        }
        val msglist = arrayListOf<SenderMSGModel> ()

        val chatadaptor = ChatAdaptor(msglist, this,recieverId!!)
        val recyclerView = binding.ChattingScreenRecyclerView
        recyclerView.adapter = chatadaptor
        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.scrollToPosition(chatadaptor.arr.size -1)
        var recieverRoom = recieverId+senderId
        var senderRoom = senderId + recieverId


        database.getReference().child("chats")
            .child(senderRoom)
            .addValueEventListener(object :ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot){
                msglist.clear()
                for(snapshot1 in dataSnapshot.children){
                    var model : SenderMSGModel ?= snapshot1.getValue(SenderMSGModel::class.java)

                    //model!!.msgId = snapshot1.key.toString()
                    Log.d("ChattingScreen", snapshot1.key.toString())
                    model!!.msg = snapshot1.child("msg").getValue().toString()

                    msglist.add(model)
                }
                //Log.d("ChattingScreen1", msglist.size.toString())
               chatadaptor.notifyDataSetChanged()
                recyclerView.scrollToPosition(chatadaptor.arr.size -1 )
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("chattingScreen1", "Failed to read value.", error.toException())
            }
        })


//        val db = Room.databaseBuilder(
//            this,
//            DataBaseHelper::class.java,
//            "CallsDatabase"
//        ).build()
//
//        var userdao = db.userDao()
        binding.VideoCall.setOnClickListener {





            var uid = intent.getStringExtra("userid")!!

            Log.d("ChattingScren", uid)

            var calledusername = intent.getStringExtra("username")




            database = FirebaseDatabase.getInstance()
            firebaseauth= FirebaseAuth.getInstance()
            var currentuid = firebaseauth.uid!!
            var username = getNameFromid(currentuid)

            var timestamp = System.currentTimeMillis()
                Log.d("Chatting121", username)
                var value1 = Videocall("True", username, currentuid)
                database.getReference()
                    .child("VideoCall")
                    .child(uid)
                    .setValue(value1)

                var userdao = DataBaseHelper.getInstance(this, "MyLocalDatabase")!!.userDao()
                var list  = userdao.getEntries()
                var long : Long = list.size.toLong()
                var callsDatabase = CallsDatabase(timestamp.toInt(),calledusername!!,"outgoing" )

//            userdao.addCall(callsDatabase)
//

                userdao.addCall(callsDatabase)

                startActivity(Intent(this@ChattingScreen, VideoCall::class.java) )
            }




//            val newData = HashMap<String, Any>()
//            newData["isvidocall"] = "True"



        var send = binding.SendButton
        send.setOnClickListener{
            var edittext : EditText = findViewById(R.id.SendEditText)
            if(edittext.text.toString()!= "") {
                var model = SenderMSGModel(edittext.text.toString(), senderId!!)
                model.timeStamp = Date().time
                val editableText = SpannableStringBuilder("")
                edittext.text = editableText

                database.getReference().child("chats").child(senderRoom)
                    .push()
                    .setValue(model).addOnSuccessListener {


                        database.getReference()
                            .child("chats")
                            .child(recieverRoom)
                            .push()
                            .setValue(model)
                            .addOnSuccessListener {

                                SendNotification(edittext.text.toString());
                                Log.d("ChattingScreen", " databaseSuccessfully entered")

                            }
                    }
            }
        }
       /* var videoCall = binding.VideoCall
        videoCall.setOnClickListener{
            database.getReference().child("Calling")

        }*/


    }



    private fun getNameFromid(uid: String) :String {


        var model : DatabaseReference = database.getReference()
            .child("User")
            .child(uid)
        var username = ""
        model.get().addOnCompleteListener {
            var snapshot = it.getResult()
            var value = snapshot.getValue(User::class.java)
            username = value!!.username
        }
        return username

    }

    fun SendNotification(msg: String){

        //user name , user id , fcmtoken ,

            var uid = FirebaseAuth.getInstance().uid!!
        FirebaseFirestore.getInstance().collection("Userdata").document(uid)
            .get()
            .addOnCompleteListener {

                var currentUser = it.getResult().toObject(User::class.java)
                if(it.isSuccessful){
                    try {
                        var jsonObject  = JSONObject()
                        var notificationObject  = JSONObject()
                        var dataObject  = JSONObject()
                        notificationObject.put("title", currentUser!!.username+"helllo")
                        notificationObject.put( "body", msg)

                        dataObject.put("userId",currentUser.userId)

                        jsonObject.put("notification",notificationObject)
                        jsonObject.put("data", dataObject)
                        jsonObject.put("to",otherusertoken)

                        callApi(jsonObject)


                    }
                    catch (e: Exception){

                        Log.d("exception", "$e")
                    }
                }
            }



    }

        private fun callApi(jsonObject: JSONObject) {



            val JSON = "application/json; charset=utf-8".toMediaType()
            val client = OkHttpClient()
            var url = "https://fcm.googleapis.com/fcm/send"
            var body = RequestBody.create(JSON, jsonObject.toString())
            var request : Request = Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization","Bearer AAAAjpYQCYg:APA91bEsygnjfZdyyhYspg60dSVk5DhZPgAYoyseVhKcVRsB2jdz_fH1y7QFVfVgTyLDJf1enNJ7npqscfyeHK4g9UjP3yv9Yj0AOlTajQCnVC9HNLed-IPnHiq53zIYFt3cA9xmVQQ9")
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    // Handle failure

                    //Log.d("chatting")
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    // Handle response
                    val responseBody = response.body?.string()
                    Log.d("chatting", responseBody!!)
                    // Process responseBody as needed
                }
            })


        }


    fun vdocall(view: View) {
        Log.d("ChattingScreen", "true")

    }

}