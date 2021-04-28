package com.pava.chatapplication.messages

import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.pava.chatapplication.R
import com.pava.chatapplication.models.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*

class ChatLogActivity : AppCompatActivity() {

    companion object{
        val TAG="chat log"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)
        val username=intent.getStringExtra(NewMessageActivity.USER_KEY)
        supportActionBar?.title=username
        setUpDummyData()
        send_button_chat_log.setOnClickListener{
            Log.d(TAG, "Attempt to send message")
            performSendMessage()
        }

    }
    class ChatMessage(val id: String, val text: String, val fromId: String, val toId: String, val timeStamp: Long)

    private fun performSendMessage() {
        val text=edittext_chat_log.text.toString()
        val fromId=FirebaseAuth.getInstance().uid
        val toId=FirebaseAuth.getInstance().uid
//        val user=intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
//        val toId= user?.uid
        if(fromId==null) return
        val reference=FirebaseDatabase.getInstance().getReference("/messages").push()
        val chatMessage= ChatMessage(reference.key!!, text, fromId!!, toId!!, System.currentTimeMillis()/1000)
        reference.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d(TAG, "Saved our chat message: ${reference.key}")
            }
    }

    private fun setUpDummyData() {
        val adapter=GroupAdapter<ViewHolder>()
        adapter.add(ChatFromItem("From"))
        adapter.add(ChatToItem("To"))
        adapter.add(ChatFromItem("From"))
        adapter.add(ChatToItem("To"))
        recyclerview_chat_log.adapter=adapter
    }
}

class ChatFromItem(val text: String): Item<ViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.chat_from_row

    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_from.text=text
    }

}

class ChatToItem(val text: String): Item<ViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.chat_to_row

    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_to.text=text
    }

}
