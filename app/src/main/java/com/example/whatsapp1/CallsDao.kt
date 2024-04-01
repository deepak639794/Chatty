package com.example.whatsapp1

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.whatsapp1.chats.CallsDatabase

@Dao
interface CallsDao {

    @Query("SELECT * FROM CallsDataBase")
    fun getEntries(): List<CallsDatabase>

    @Insert
    fun addCall(callsDatabase: CallsDatabase)

    @Update
    fun updataDetails(callsDatabase: CallsDatabase)

    @Delete
    fun deletecall(callsDatabase: CallsDatabase)
}
