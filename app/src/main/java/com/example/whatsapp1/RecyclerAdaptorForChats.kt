package com.example.whatsapp1

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsapp.models.ChatsDataClass
import com.example.whatsapp.models.User
import com.example.whatsapp1.ChattingScreen
import com.example.whatsapp1.R
import java.util.zip.Inflater

class RecyclerAdaptorForChats(var chatlist: ArrayList<User>,var context:Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        //var img :ImageView =item.findViewById(R.id.ImageId)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_chat_row,parent,false))

    }

    override fun getItemCount(): Int {
        Log.d("Chatssize", chatlist.size.toString())
        return chatlist.size

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var usernameTV :TextView= holder.itemView.findViewById(R.id.ChatsUserName)
        usernameTV.text = chatlist[position].username


        holder.itemView.setOnClickListener{

            var intent =  Intent(context, ChattingScreen::class.java)
            intent.putExtra("username", chatlist[position].username)
            intent.putExtra("userid", chatlist[position].userId)
            intent.putExtra("token", chatlist[position].token)



            context.startActivity(intent)
        }
    }
}