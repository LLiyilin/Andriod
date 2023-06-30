package com.example.myapplication2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(){
    val TAG = "MainActivity"

    val data = ArrayList<KPOPsinger>()

    lateinit var adapter: KPOPsingerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val name = intent.getStringExtra("user")
        if(name == null){
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        Log.i(TAG,"login user = $name")

        adapter=KPOPsingerAdapter()

        val recycler=findViewById<RecyclerView>(R.id.recycler)
        val manager=LinearLayoutManager(this)
        recycler.layoutManager=manager
        recycler.adapter=adapter
        //模拟加载数据
        loadData()
    }

    private fun loadData(){
        data.add(KPOPsinger("宋雨琦",23,"2018年5月，随组合发行首张专辑《I am》，并在首尔举行出道Showcase，正式出道",R.mipmap.syq))
        data.add(KPOPsinger("叶舒华",23,"2018年8月，随组合发行首张数位单曲《HANN》。2019年2月，随组合发行第二张迷你专辑《I made》",R.mipmap.ysh ))
        data.add(KPOPsinger("金米妮",26,"Minnie参与专辑收录曲《Blow Your Mind》的词曲创作.2019年4月，随组合为电视剧《她的私生活》演唱OST",R.mipmap.jmn))

        adapter.setData(data)
    }
}