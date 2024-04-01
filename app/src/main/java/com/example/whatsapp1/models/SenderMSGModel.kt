package com.example.whatsapp1.models

import java.sql.Timestamp

class SenderMSGModel {

    var uId :String = ""
    var     msg : String = ""
    var msgId : String = ""
    var timeStamp : Long = 0
    constructor(msg: String, msgId: String) {
        this.msg = msg
        this.uId = msgId
    }
    constructor(msg : String , msgId: String , timestamp : Long){
        this.msg = msg
        this.uId = msgId
        this.timeStamp = timestamp
    }
    constructor()
}
