package com.example.whatsapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.whatsapp.models.User
import com.example.whatsapp1.SignUp
import com.example.whatsapp1.UserProfile
import com.example.whatsapp1.databinding.FragmentSettingBinding
import com.example.whatsapp1.fragmentInterface
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SettingFragment (var fragInterfacce :fragmentInterface): Fragment() {
    private var _binding : FragmentSettingBinding ?     = null
    private  val binding get() = _binding!!
    lateinit var database : FirebaseDatabase
    lateinit var firebaseauth  :FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSettingBinding.inflate(layoutInflater)
        return binding.root
//        inflater.inflate(R.layout.fragment_setting, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var logout = binding.LogOut

        database = FirebaseDatabase.getInstance()
        firebaseauth = FirebaseAuth.getInstance()
        var uid = firebaseauth.uid
        var model : DatabaseReference = database.getReference()
            .child("User")
            .child(uid!!)
        var username = ""
        model.get().addOnCompleteListener {
            var snapshot = it.getResult()
            var value = snapshot.getValue(User::class.java)
            Log.d("SettingFragment", snapshot.toString())
            username = value!!.username
            Log.d("SettingFragment", username)

           // binding.ProfileName.text = username

        }

        binding.profileCardView.setOnClickListener{

            fragInterfacce.LoadFrag(UserProfile(fragInterfacce),1)

        }

        logout.setOnClickListener {

            var pref = requireActivity().getSharedPreferences("LogInOut",
                AppCompatActivity.MODE_PRIVATE
            )
            var edit = pref.edit()
            edit.putString("LoginDetails","LoggedOut")
            edit.commit()
            startActivity(Intent(view.context, SignUp::class.java))
            requireActivity().finish()




        }


    }

}