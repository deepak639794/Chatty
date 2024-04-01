package com.example.whatsapp1

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.whatsapp.SettingFragment
import com.example.whatsapp.models.User
import com.example.whatsapp1.chats.CallUpdate
import com.example.whatsapp1.chats.CallsDatabase
import com.example.whatsapp1.chats.Chats
import com.example.whatsapp1.databinding.ActivityMainBinding
import com.example.whatsapp1.models.Videocall
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity(), fragmentInterface {

    private var  _binding : ActivityMainBinding? = null
    private val binding get() = _binding!!
    lateinit var ft :FragmentTransaction
    val tag = "mainactivity1"
    lateinit var bnv : BottomNavigationView

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            askNotificationPermission()
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED

            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {

            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
    override
    fun onCreate(savedInstanceState: Bundle?) {




        super.onCreate(savedInstanceState)



        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        askNotificationPermission()
//

        var pref = getSharedPreferences("LogInOut", MODE_PRIVATE)

        var logindetails = pref.getString("LoginDetails", "null")

        Log.d("preference ", "$logindetails")
        if(logindetails != "LoggedIn" && logindetails!!.equals("null")){
            startActivity(Intent(this ,  SignUp::class.java))
            finish()

        }



        if(intent.extras!=null){
            var username = intent.extras!!.getString("user")
            Log.d("MainActivity111" , username.toString())



//            var intent = Intent(this, ChattingScreen::class.java)
//            startActivity(intent)
        }


        LoadFrag(Chats(),1)




        var database  :FirebaseDatabase
        var firebase = FirebaseAuth.getInstance()
        var uid = firebase.uid


            database = FirebaseDatabase.getInstance()
            Log.d("MainActivity1", uid.toString())
            database.getReference().child("VideoCall")
                .addValueEventListener(object  : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (userSnapshot in snapshot.children) {
                                val value = userSnapshot.value


                                Log.d("MainActivity1111", "${userSnapshot.key}")
                                var model : Videocall?= userSnapshot.getValue(Videocall::class.java)

                                Log.d("MainActivity111", "${model}" + "    "+"${model!!.name}")

//                                if (value is Map<*, *>) {
//                                    val userData = value as Map<*, *>
//                                    val isVidocallObject = userData["isvidocall"]
//


                                    if (model.isvideocall.equals("True") && userSnapshot.key.equals(uid)) {
                                        Log.d(
                                            "Mainactivity121",
                                            "${model.isvideocall}" + "   " + uid
                                        )

                                        Toast.makeText(
                                            baseContext,
                                            "some one is calling you ",
                                            Toast.LENGTH_SHORT
                                        ).show()


                                        var database: FirebaseDatabase
                                        database = FirebaseDatabase.getInstance()



                                        var firebaseAuth = FirebaseAuth.getInstance()
                                        var currentuid = firebaseAuth.uid!!

                                        var model: DatabaseReference = database.getReference()
                                            .child("User")
                                            .child(currentuid)

                                        model.get().addOnCompleteListener {
                                            var snapshot = it.getResult()
                                            var value = snapshot.getValue(User::class.java)




                                            var userdao = DataBaseHelper.getInstance(this@MainActivity, "MyLocalDatabase")!!.userDao()
                                            var list  = userdao.getEntries()
                                            var long : Long = list.size.toLong()
                                            var callsDatabase = CallsDatabase(long.toInt(), value!!.username,"incoming" )



                                            userdao.addCall(callsDatabase)


                                            var intent1 = Intent(
                                                this@MainActivity,
                                                incomingcall::class.java
                                            )
                                            Log.d("Mainactivity222", value!!.username)
                                            intent1.putExtra("username", value!!.username)
                                            startActivity(
                                               intent1
                                            )

                                        }
                                    }


                                    // if(isVidocallObject!!.equals("False") && )
//                                }


                            }

                        }
                        else {
                            Log.d("MainActivity", "nothing")
                        }
                    }


                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                        Log.d("MainActivity", error.toString())
                    }

                })





        bnv = binding!!.BNV
        bnv.setOnNavigationItemSelectedListener {
            Log.d("$tag", "tag")
            when (it.itemId){

                R.id.status->
                {
                    Log.d("$tag", "status")
                    true
                }
                R.id.calls ->{

                    LoadFrag(CallUpdate(),1)
                    Log.d("$tag"," calls")
                    true
                }
                R.id.Camera ->{
                    Log.d("$tag", "Camera")
                    true
                }
                R.id.Chats->{
                    Log.d("$tag", "chats")
                    LoadFrag(Chats(),1)
                    true
                }
                R.id.Settings->{
                    Log.d("$tag","Settings")

                    LoadFrag(SettingFragment(this),1)
                    true
                }
                else -> false
            }
        }



    }

    override fun LoadFrag(frag:Fragment, flag:Int){

         ft = supportFragmentManager.beginTransaction()
        if(flag ==0){
            Log.d("$tag", " container")
            ft.add(R.id.container, frag)
                .addToBackStack(null)
        }

//        else if(flag ==-1){
//            ft.remove(frag )
//                .addToBackStack(null)
//        }

        if(flag ==-1){
            supportFragmentManager.popBackStack()
        }
        else {
            Log.d("$tag", " container")
            ft.replace(R.id.container, frag)
                .addToBackStack(null)


        }

        ft.commit()

    }
}


