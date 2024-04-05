package com.example.whatsapp1

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.whatsapp1.databinding.ActivityVideoCallBinding

import android.content.pm.PackageManager
import android.util.Log
import android.view.SurfaceView
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
//import io.agora.rtc2.video.VideoCanvas
//import io.agora.rtc2.*
import androidx.constraintlayout.widget.ConstraintSet.Constraint
import androidx.constraintlayout.widget.Constraints
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.whatsapp1.models.Videocall
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import io.agora.rtc2.ChannelMediaOptions
import io.agora.rtc2.Constants
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.RtcEngineConfig
import io.agora.rtc2.video.VideoCanvas


class VideoCall : AppCompatActivity() {
    var _binding : ActivityVideoCallBinding ? = null


    val binding get() = _binding!!
        var channelname = "WhatsAppVideoCall"
    var appId = "******API KEY*"
    var isJoined  = false

    var tokenname = "*********Token************************"
    var agoraEngine : RtcEngine? = null
    var localSurfaceView : SurfaceView ? =null
    var remoteSurfaceView : SurfaceView ? = null
    var localUid: Int  =0// UID of the local user
    var remoteUids = HashSet<Int>() // An object to store uids of remote users
    var permissionId = 22
    var permissions = arrayOf(

        android.Manifest.permission.RECORD_AUDIO,
        android.Manifest.permission.CAMERA
    )

    fun showmsg(msg : String){
        runOnUiThread{

            Log.d("VdoCall11", msg)
            Toast.makeText(baseContext,msg, Toast.LENGTH_SHORT).show()
        }
    }
    fun LeaveCall(){

        if(!isJoined){
            showmsg("plz join a channel ")

        }
        else
        {
            agoraEngine!!.leaveChannel()
            showmsg("you left the channel ")
            if(remoteSurfaceView!= null) remoteSurfaceView!!.visibility = GONE
            if(localSurfaceView!=null) localSurfaceView!!.visibility = GONE
            isJoined = false
        }
    }
    fun JoinCall() : Int{

//        if (!checkSelfPermission()) {
//            showmsg("Permissions were not granted")
//            return -1
//        }
//            //this.channelname = channelname
        //if(agoraEngine==null) setUpVideoSdkEngine()

        if(checkSelfPermission()){
            Log.d("VdoCall11","inside the join call")
            val options = ChannelMediaOptions()
            options.channelProfile = Constants.CHANNEL_PROFILE_COMMUNICATION
            options.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER
            SetUpLocalVideo()
            localSurfaceView!!.visibility = VISIBLE
            agoraEngine!!.startPreview()

            agoraEngine!!.joinChannel(tokenname,channelname, localUid,options)
            return 0

        }
        else{
            Log.d("vdocall"," false")
            return 1
        }


    }
    private fun setupVideoSDKEngine() {
        try {
            val config = RtcEngineConfig()
            config.mContext = baseContext
            config.mAppId = appId
            config.mEventHandler = mRtcEventhandler
            agoraEngine = RtcEngine.create(config)
            // By default, the video module is disabled, call enableVideo to enable it.
            agoraEngine!!.enableVideo()
        } catch (e: Exception) {
            showmsg(e.toString())
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityVideoCallBinding.inflate(layoutInflater)

        setContentView(binding.root)



        if(!checkSelfPermission()){
            Log.d("vdocall", "permissions")
            ActivityCompat.requestPermissions(this, permissions,permissionId)
            return

        }
        setupVideoSDKEngine()
        var firebase = FirebaseAuth.getInstance()
        var uid = firebase.uid!!
        var database : FirebaseDatabase
        database = FirebaseDatabase.getInstance()
        binding.CallDecline.setOnClickListener {
//            val newData = HashMap<String, Any>()
//            newData["isvidocall"] = "False"
            val updatesMap = mutableMapOf<String, Any>()
            updatesMap["isvideocall"] = "False"
            updatesMap["name"] = ""
            updatesMap["callerid"] = ""
            database.getReference()
                .child("VideoCall")
                .child(uid)
                .updateChildren(updatesMap)

            LeaveCall()
            finish()
        }
        //var join : Button = findViewById(R.id.Join)
      //  var leave : Button = findViewById(R.id.Leave)


        //  localSurfaceView!!.visibility = VISIBLE

       JoinCall()

    }

    private val mRtcEventhandler: IRtcEngineEventHandler =
        object :IRtcEngineEventHandler(){
            override fun onUserJoined(uid: Int, elapsed: Int) {
                showmsg("remoteuser Joined")
                //remoteUids.add(uid)
                runOnUiThread{
                    SetUpRemoteVideo(uid)



                }

            }

            override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
                isJoined= true
                localUid = uid
                showmsg("Joined Channel $channel")
                // ml
            }

            override fun onUserOffline(uid: Int, reason: Int) {
                runOnUiThread{
                    remoteSurfaceView!!.visibility = GONE


                    var database = FirebaseDatabase.getInstance()
                    var firebase = FirebaseAuth.getInstance()
//                    val newData = HashMap<String, Any>()
                    var uid = firebase.uid!!
//                    newData["isvidocall"] = "False"

//                    var model  = Videocall("False", "","")
                    val updatesMap = mutableMapOf<String, Any>()
                    updatesMap["isvideocall"] = "False"
                    updatesMap["name"] = ""
                    updatesMap["callerid"] = ""
                    database.getReference()
                        .child("VideoCall")
                        .child(uid)
                        .updateChildren(updatesMap)
                    finish()




                    showmsg("user offline")
                }


            }
        }
    override fun onDestroy() {
        super.onDestroy()
        agoraEngine!!.stopPreview()
        agoraEngine!!.leaveChannel()
        Thread{
            RtcEngine.destroy()
            agoraEngine  = null
        }.start()
    }

    fun SetUpLocalVideo (){


        localSurfaceView= SurfaceView(baseContext)
        binding.Reciever.addView(localSurfaceView)

        agoraEngine!!.setupLocalVideo(
            VideoCanvas(
                localSurfaceView,
                VideoCanvas.RENDER_MODE_HIDDEN,
                0
            )
        )
        // localSurfaceView!!.visibility = VISIBLE


    }

    fun SetUpRemoteVideo(uid: Int){

        showmsg("user joined")
        remoteSurfaceView = SurfaceView(baseContext)
        remoteSurfaceView!!.setZOrderMediaOverlay(true)
        binding.Caller.addView(remoteSurfaceView)
        var vdo = VideoCanvas(
            remoteSurfaceView,
            VideoCanvas.RENDER_MODE_FIT,
            uid
        )

        agoraEngine!!.setupRemoteVideo(

            vdo
        )
        //remoteSurfaceView!!.visibility = VISIBLE

    }
    protected fun checkSelfPermission(): Boolean {
        return !(ContextCompat.checkSelfPermission(
            this,
            permissions[0]
        ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    this,
                    permissions[1]
                ) != PackageManager.PERMISSION_GRANTED)
    }



}


