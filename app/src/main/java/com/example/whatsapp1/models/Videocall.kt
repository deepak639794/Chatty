package com.example.whatsapp1.models

class Videocall {
    var isvideocall : String = ""
    var name : String = ""
    var callerid : String = ""
    constructor(isvideocall : String, name : String , callerid : String){
        this.name = name
        this. callerid = callerid
        this.isvideocall = isvideocall
    }
    constructor()
}