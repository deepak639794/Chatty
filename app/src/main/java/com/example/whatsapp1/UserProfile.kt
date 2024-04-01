package com.example.whatsapp1

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.net.URI

class UserProfile (var fragmentInterface: fragmentInterface): Fragment() {

    lateinit var  imageuri :Uri
    lateinit var profileimage :ImageView
    lateinit var storage : StorageReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {



        var save :Button = view.findViewById(R.id.SaveButton)
        save.setOnClickListener{
            uploadImage()
        }
        profileimage = view.findViewById(R.id.ProfileImage)

        var imagepicker :ImageView = view.findViewById(R.id.ImagePicker)
        imagepicker.setOnClickListener {
            selectImage()
        }
       var imageView: ImageView =  view.findViewById(R.id.BackButtonInProfile)
        var settingtext : TextView = view.findViewById(R.id.SettingText_InProfile)
        settingtext.setOnClickListener {
            fragmentInterface.LoadFrag(UserProfile(fragmentInterface),-1)
        }
        imageView.setOnClickListener {
//            onBackPressed()
            fragmentInterface.LoadFrag(UserProfile(fragmentInterface),-1)
        }
    }

    private fun uploadImage() {
        storage = FirebaseStorage.getInstance().getReference()
        storage.putFile(imageuri)
            .addOnSuccessListener {

                Toast.makeText(requireView().context, "Image is Successfully Uploaded", Toast.LENGTH_SHORT).show()


                fragmentInterface.LoadFrag(UserProfile(fragmentInterface),-1)


            }
            .addOnFailureListener{
                Toast.makeText(requireView().context, "$it", Toast.LENGTH_SHORT).show()
            }
    }

    private fun selectImage() {
        var intent : Intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(intent, 100)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 100 && data != null && data.data !=null ){

            imageuri = data.data!!
            profileimage.setImageURI(imageuri)


        }
    }

}