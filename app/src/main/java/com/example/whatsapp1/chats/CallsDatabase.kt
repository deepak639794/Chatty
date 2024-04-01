package com.example.whatsapp1.chats
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("CallsDataBase")
class CallsDatabase (
    @PrimaryKey(autoGenerate = true)
    var serialnumber:Int,

    @ColumnInfo(name = "CallerName")
    var name : String,

    @ColumnInfo(name ="Mode")
    var mode :String

)