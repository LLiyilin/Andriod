package com.example.lesson7

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class ContactActivity : AppCompatActivity() {

    lateinit var adapter: ContactAdapter

    var contactLauncher = registerForActivityResult(RequestPermission()) {
        if (it) {
            readContacts()
        } else {
            Toast.makeText(this, "请同意读取联系人列表", Toast.LENGTH_LONG).show()
        }
    }
    var callLauncher = registerForActivityResult(RequestPermission()) {
        if (it) {
            clickedItem?.apply {
                call(this.number)
            }
        } else {
            Toast.makeText(this, "请同意使用拨打电话权限", Toast.LENGTH_LONG).show()
        }
    }

    var clickedItem: Contact? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
        val recycler = findViewById<RecyclerView>(R.id.recycler)

        adapter = ContactAdapter()

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        val per = "android.permission.READ_CONTACTS"
        when {
            ContextCompat.checkSelfPermission(this, per) == PackageManager.PERMISSION_GRANTED -> {
                readContacts()
            }
            else -> {
                contactLauncher.launch(per)
            }
        }


    }

    fun makeCall(item: Contact) {
        val per = "android.permission.CALL_PHONE";
        clickedItem = item
        when {
            ActivityCompat.checkSelfPermission(this, per) == PackageManager.PERMISSION_GRANTED -> {
                call(item.number)
            }
            else -> {
                callLauncher.launch(per)
            }
        }
    }

    fun call(number: String) {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$number")
        startActivity(intent)
    }

    private fun readContacts() {
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        cursor?.use {
            val list = arrayListOf<Contact>()
            while (it.moveToNext()) {
                val name = it.getString(
                    Math.max(
                        0,
                        it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                    )
                )
                val number = it.getString(
                    Math.max(
                        0,
                        it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    )
                )
                list.add(Contact(name, number))
            }
            adapter.setData(list)
        }
    }

    inner class ContactAdapter : RecyclerView.Adapter<ContactViewHolder>() {

        val data = arrayListOf<Contact>()

        override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
            val item = data.get(position)
            holder.name.text = item.name
            holder.number.text = item.number

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)

            val holder = ContactViewHolder(view)

            holder.itemView.setOnClickListener {
                val pos = holder.adapterPosition
                val item = data.get(pos)
                makeCall(item)
            }

            return holder
        }

        override fun getItemCount() = data.size

        fun setData(list: ArrayList<Contact>) {
            data.clear()
            data.addAll(list)
            notifyItemRangeChanged(0, list.size)
        }

    }

    inner class ContactViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val name = v.findViewById<TextView>(R.id.name)
        val number = v.findViewById<TextView>(R.id.number)


    }

    data class Contact(val name: String, val number: String)
}