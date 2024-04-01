package com.example.whatsapp1.chats

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whatsapp.models.ChatsDataClass
import com.example.whatsapp.models.User
import com.example.whatsapp1.RecyclerAdaptorForChats
import com.example.whatsapp1.databinding.FragmentChatsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Chats : Fragment() {


    private  var _binding : FragmentChatsBinding? = null
    private  val binding get() = _binding!!
    lateinit var recyclerAdaptor: RecyclerAdaptorForChats
    var chatlist = arrayListOf<ChatsDataClass>()
    lateinit var database : FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.d("mainactivity1", " fragcreated")
        _binding = FragmentChatsBinding.inflate(inflater, container, false)
        return  binding.root
        //return inflater.inflate(R.layout.fragment_chats,container,false)

    }
    fun pushChats(){

        var chatdata = ChatsDataClass("Deepak", "6397946235", "hye")
        chatlist.add(chatdata)
        chatlist.add(chatdata)
        chatlist.add(chatdata)
        chatlist.add(chatdata)
        chatlist.add(chatdata)
        chatlist.add(chatdata)
        chatlist.add(chatdata)
        chatlist.add(chatdata)
        chatlist.add(chatdata)
        chatlist.add(chatdata)
        chatlist.add(chatdata)
        chatlist.add(chatdata)
        chatlist.add(chatdata)
        chatlist.add(chatdata)
        chatlist.add(chatdata)
        chatlist.add(chatdata)
        chatlist.add(chatdata)
        chatlist.add(chatdata)


    }

    override fun onViewCreated(view: View,  savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var userarr = arrayListOf<User>()
        database = FirebaseDatabase.getInstance()
        database.getReference().child("User")
            .addValueEventListener(object  :ValueEventListener{

                override fun onDataChange(snapshot: DataSnapshot) {
                    userarr.clear()
                    if (snapshot.exists()) {
                     //   userarr.add(User("dpk","12345","@gmail.com", "jffjfjfjfjfj"))
                        for (userSnapshot in snapshot.children) {

                            val userModel : User?= userSnapshot.getValue(User::class.java)

                            // Log.d("Chatselse", userSnapshot.key.toString() +"   "+ FirebaseAuth.getInstance().uid)

                            if(userSnapshot.key.toString().equals(FirebaseAuth.getInstance().uid))
                                continue
                            userModel!!.userId =userSnapshot.key.toString()
                            userarr.add(userModel)

                        }
                        var recyclerView = binding.RecyclerView
                        recyclerAdaptor  = RecyclerAdaptorForChats(userarr, view.context)
                        recyclerView.layoutManager = LinearLayoutManager(view.context)
                        recyclerView.adapter = recyclerAdaptor

                        //recyclerAdaptor.notifyDataSetChanged()
                        //Log.d("Chatselse", userarr[0].username)


                    }
                }


                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })



        //Log.d("log", "value")
        //var recyclerView = view.findViewById<RecyclerView>(R.id.RecyclerView)
        //Log.d("mainactivity"," onviewcreated")
       // var recyclerView : RecyclerView = view.findViewById(R.id.RecyclerView)


    }



}