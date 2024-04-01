package com.example.whatsapp.models

import android.util.Log

class User {


    var profilepic = ""
    var username = ""
    var email = ""
    var pass = ""
    var userId = ""
    var lastmsg = ""
    var status = ""
    var token =""



    var isvidocall ="False"
    constructor(isvidocall : String){
        this.isvidocall= isvidocall
    }
    constructor(name: String , pass: String , email: String, userId: String, token:String){
        Log.d("constructor","$name")
        this.username = name
        this.pass = pass
        this.email = email
        this.userId = userId
        this.token = token
        Log.d("constructor", token)
    }

    constructor()
//    constructor(
//        profilepic: String,
//        username: String,
//        email: String,
//        pass: String,
//        userId: String,
//        lastmsg: String,
//        status: String
//    ) {
//        this.profilepic = profilepic
//        this.username = username
//        this.email = email
//        this.pass = pass
//        this.userId = userId
//        this.lastmsg = lastmsg
//        this.status = status
//    }

}