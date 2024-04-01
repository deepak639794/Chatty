package com.example.whatsapp1.chats

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsapp1.models.SenderMSGModel
import android.content.Context
import android.view.LayoutInflater
//import path.to.AgoraManagerListener
import com.example.whatsapp1.R
import com.google.firebase.auth.FirebaseAuth


class ChatAdaptor(var arr: ArrayList<SenderMSGModel>, var context: Context, var uid : String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//    var mListner : AgoraMan
//    var mListener: AgoraManagerListener? = null // The event handler to notify the UI of agoraEngine events

    var SENDER_VIEW_TYPE = 1
    var RECIEVER_VIEW_TYPE = 2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        if(viewType == SENDER_VIEW_TYPE ){
            val view = LayoutInflater.from(context).inflate(R.layout.fragment_sender_msg, parent, false)
            return  SenderViewHolder(view)

        }

        else
        {
            val  view = LayoutInflater.from(context).inflate(R.layout.fragment_reciver_msg, parent, false)
            return RecieverViewHolder(view)
        }



    }

    override fun getItemViewType(position: Int): Int {
        if(arr[position].uId.equals(FirebaseAuth.getInstance().uid)){
//            Toast.makeText(context, "sender", Toast.LENGTH_SHORT).show()
            return RECIEVER_VIEW_TYPE
        }
        else{
//            Toast.makeText(context, "reciever",Toast.LENGTH_SHORT).show()
            return SENDER_VIEW_TYPE
        }

    }

    override fun getItemCount(): Int {
        return arr.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var massageModel = arr[position]
        if(holder::class.java == SenderViewHolder::class.java){

            (holder as (SenderViewHolder)).sendermsg.text = massageModel.msg
        }
        else
        {
            (holder as (RecieverViewHolder)).recievermsg.text = massageModel.msg
        }
    }

    class RecieverViewHolder( var itemView :View) : RecyclerView.ViewHolder(itemView){
        lateinit var recievermsg : TextView
        lateinit var recieverTime :TextView

        init {
            recievermsg = itemView.findViewById(R.id.RecieverMSG1)
            recieverTime = itemView.findViewById(R.id.ReciverTimeStamp1)
        }
    }

    class  SenderViewHolder( itemView: View) : RecyclerView.ViewHolder(itemView){
        var sendermsg : TextView
        var sendertime: TextView

        init{
            sendermsg = itemView.findViewById(R.id.SenderMSG)
            sendertime = itemView.findViewById(R.id.SenderTimeStamp)
        }
    }



}
