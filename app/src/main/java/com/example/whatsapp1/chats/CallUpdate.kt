package com.example.whatsapp1.chats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsapp1.DataBaseHelper
import com.example.whatsapp1.R


class CallUpdate : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calls_update, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {



//        var userdao = DataBaseHelper.getInstance(view.context)?.userDao()!!
//
//        userdao.addCall(CallsDatabase())
//        var dataBaseHelper :DataBaseHelper
//        dataBaseHelper = DataBaseHelper.getInstance
//        val db = Room.databaseBuilder(
//            view.context,
//            DataBaseHelper::class.java,
//            "CallsDatabase"
//        ).build()
//
//        var userdao = db.userDao()
//        var callslist  =userdao.getEntries()
        var userdao = DataBaseHelper.getInstance(view.context, "MyLocalDatabase")!!.userDao()

        var list : List<CallsDatabase> = userdao.getEntries()
        var recyclerview  : RecyclerView= view.findViewById(R.id.RecyclerViewForCalls)
        var adaptor = CallUpdateRecyclerAdaptor(list,view.context)
        recyclerview.layoutManager = LinearLayoutManager(view.context)
        recyclerview.adapter = adaptor
    }

}