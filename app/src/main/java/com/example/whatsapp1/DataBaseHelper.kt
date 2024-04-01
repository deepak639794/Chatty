package com.example.whatsapp1

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.TypeConverters
import com.example.whatsapp1.chats.CallsDatabase

//
//@Database(entities = [CallsDatabase::class], version = 1)
//abstract class AppDatabase : RoomDatabase() {
//    abstract fun userDao(): UserDao
//}

@Database(entities = [CallsDatabase:: class], exportSchema = false, version = 1)
@TypeConverters
abstract class DataBaseHelper :RoomDatabase() {
    abstract fun userDao(): CallsDao

    companion object {

        private var instance: DataBaseHelper? = null

        fun getInstance(context: Context, dbname : String): DataBaseHelper? {
            if (instance == null) {
                synchronized(DataBaseHelper::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        DataBaseHelper::class.java,
                        dbname
                    ).allowMainThreadQueries()
                        .build()
                }
            }
            return instance
        }

        fun destroyInstance() {
            instance = null
        }


//    private final var DB_NAME : String= "CallsDatabase"
//    private lateinit var instance :  DataBaseHelper
//
//
//    fun getDB(context: Context):DataBaseHelper{
//
//        if(instance ==null){
//            instance = Room.databaseBuilder(
//                context,
//                DataBaseHelper::class.java,
//                "MyCalling DataBase"
//            )
//                .fallbackToDestructiveMigrationOnDowngrade()
//                .allowMainThreadQueries()
//                .build()
//        }
////        return instance
////    }
    }
}