package com.example.whatsapp1.chats

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsapp1.R


class CallUpdateRecyclerAdaptor(var list: List<CallsDatabase>, var context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        //var img :ImageView =item.findViewById(R.id.ImageId)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.calling_rows,parent,false))

    }

    override fun getItemCount(): Int {
        Log.d("Chatssize", list.size.toString())
        return list.size;
//        return 0;

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


        var callername :TextView = holder.itemView.findViewById(R.id.CallerNameCallUpdate)
        var callingmode :TextView  = holder.itemView.findViewById(R.id.CallingMode)

        callername.text = list[position].name
        callingmode.text = list[position].mode


//        var usernameTV : TextView = holder.itemView.findViewById(R.id.ChatsUserName)
//        usernameTV.text = chatlist[position].username



//        holder.itemView.setOnClickListener{
//
//            var intent =  Intent(context, ChattingScreen::class.java)
//            intent.putExtra("username", chatlist[position].username)
//            intent.putExtra("userid", chatlist[position].userId)
//            intent.putExtra("token", chatlist[position].token)
//
//
//
//            context.startActivity(intent)
//        }
    }
}