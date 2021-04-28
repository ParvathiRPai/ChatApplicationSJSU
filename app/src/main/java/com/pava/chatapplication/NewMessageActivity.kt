package com.pava.chatapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*

class NewMessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)
        supportActionBar?.title="Select User"
        val adapter =GroupAdapter<ViewHolder>()
        recyclerview_newmessage.adapter=adapter
        fetchUsers()
    }

    private fun fetchUsers() {
        val ref=FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                val adapter =GroupAdapter<ViewHolder>()
                p0.children.forEach {
                    Log.d("New Message", it.toString())
                    val user=it.getValue(User::class.java)
                    if(user!=null)
                    {
                        adapter.add(UserItem(user))
                    }
                }
                recyclerview_newmessage.adapter=adapter
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}

class UserItem(val user:User): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {

    }
    override fun getLayout(): Int {
        return R.layout.user_row_new_message
    }

}