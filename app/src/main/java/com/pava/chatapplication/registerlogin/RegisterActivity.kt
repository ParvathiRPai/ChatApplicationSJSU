package com.pava.chatapplication.registerlogin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.pava.chatapplication.R
import com.pava.chatapplication.messages.LatestMessagesActivity
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_button_register.setOnClickListener {
         performRegister()
        }

        already_have_account_text_view.setOnClickListener {
            Log.d("RegisterActivity", "Go to back page")
            // launch the login
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        selectphoto_button_register.setOnClickListener{
            Log.d("Register", "Show my photo")
            val intent=Intent(Intent.ACTION_PICK)
            intent.type="image/*"
            startActivityForResult(intent,0)
        }
    }

    var selectedPhotoUri: Uri?=null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==0 && resultCode==Activity.RESULT_OK && data != null)
        {
            Log.d("RegisterActivity", "Photo was selected")
            selectedPhotoUri=data.data
            val bitmap= MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            selectphoto_imageview_register.setImageBitmap(bitmap)
            selectphoto_button_register.alpha=0f

        }
    }

    private fun performRegister() {
        val email = email_edittext_register.text.toString()
        val password = password_edittext_register.text.toString()

        if(email.isEmpty() || password.isEmpty())
        {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            return
        }


        Log.d("RegisterActivity", "Email is: " + email)
        Log.d("RegisterActivity", "Password: $password")

        // Firebase Authentication to create a user with email and password
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                // else if successful
                Log.d("Register", "Successfully created user with uid: ${it.result?.user?.uid}")
                uploadImageInFireBaseStorage()
            }
            .addOnFailureListener{
                Log.d("Register", "Failed to create user: ${it.message}")
                Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadImageInFireBaseStorage() {
        if(selectedPhotoUri==null)return
        val filename=UUID.randomUUID().toString()
        val ref=FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(selectedPhotoUri!!).addOnSuccessListener {
            Log.d("Register", "Successfully uploaded image : ${it.metadata?.path}")
            ref.downloadUrl.addOnSuccessListener {
                it.toString()
                Log.d("Register", "File Location: $it")
                saveUserToFirebaseDatabase(it.toString())
            }
        }
    }

    private fun saveUserToFirebaseDatabase(profileImageUrl: String) {
        val uid=FirebaseAuth.getInstance().uid ?: ""
        val ref=FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user= User(
            uid,
            username_edittext_register.text.toString(),
            profileImageUrl
        )
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "Finally saved the user to database")
                val intent=Intent(this, LatestMessagesActivity::class.java)
                intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }


    }
}
class User(val uid:String, val username: String, val profileImageUrl: String)
{
    constructor():this("","","")
}

