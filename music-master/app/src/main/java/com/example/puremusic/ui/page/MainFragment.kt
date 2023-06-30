package com.example.puremusic.ui.page

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.puremusic.bridge.data.bean.TestAlbum
import com.example.architecture.ui.adapter.SimpleBaseBindingAdapter
import com.example.puremusic.R
import com.example.puremusic.bridge.player.PlayerManager
import com.example.puremusic.bridge.request.MusicRequestViewModel
import com.example.puremusic.databinding.FragmentMainBinding
import com.example.puremusic.bridge.state.MainViewModel
import com.example.puremusic.databinding.AdapterPlayItemBinding
import com.example.puremusic.ui.base.BaseFragment

class MainFragment : BaseFragment() {
    var mainBinding: FragmentMainBinding? = null
    var mainViewModel: MainViewModel? = null
    var adapter: SimpleBaseBindingAdapter<TestAlbum.TestMusic?, AdapterPlayItemBinding?>? = null
    private var musicRequestViewModel: MusicRequestViewModel? = null // 音乐资源相关的VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = getFragmentViewModelProvider(this).get(MainViewModel::class.java)
        musicRequestViewModel =
            getFragmentViewModelProvider(this).get(MusicRequestViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        mainBinding = FragmentMainBinding.bind(view)
        mainBinding?.vm = mainViewModel   // 设置VM，就可以实时数据变化
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel!!.initTabAndPage.set(true)

        adapter = object : SimpleBaseBindingAdapter<TestAlbum.TestMusic?
                , AdapterPlayItemBinding?>(
            context, R.layout.adapter_play_item
        ) {
            override fun onSimpleBindItem(
                binding: AdapterPlayItemBinding?,
                item: TestAlbum.TestMusic?,
                holder: RecyclerView.ViewHolder?
            ) {
                binding?.tvTitle?.text = item?.title
                binding?.tvArtist?.text = item?.artist?.name
                Glide.with(binding?.ivCover!!.context).load(item?.coverImg).into(binding.ivCover)

                // 歌曲下标记录
                val currentIndex = PlayerManager.instance.albumIndex // 歌曲下标记录

                binding.rootView.setBackgroundColor(
                    if (currentIndex == holder?.adapterPosition)
                        resources.getColor(R.color.colorPrimaryDark)
                    else Color.TRANSPARENT
                )

                // 点击Item
                binding.root.setOnClickListener { v ->
                    Toast.makeText(mContext, "播放音乐", Toast.LENGTH_SHORT).show()
                    PlayerManager.instance.playAudio(holder!!.adapterPosition)
                }
            }


        }

        mainBinding?.rv?.adapter = adapter



        PlayerManager.instance.changeMusicLiveData.observe(viewLifecycleOwner, {
            adapter?.notifyDataSetChanged()
        })

        if (PlayerManager.instance.album == null) {
            musicRequestViewModel!!.requestFreeMusics()
        }

        musicRequestViewModel!!.freeMusicesLiveData!!.observe(viewLifecycleOwner,
            { musicAlbum: TestAlbum? ->
                if (musicAlbum != null && musicAlbum.musics != null) {
                    // 这里特殊：直接更新UI，越快越好
                    adapter?.list = musicAlbum.musics // 数据加入适配器
                    adapter?.notifyDataSetChanged()

                    // 播放相关的业务需要这个数据
                    if (PlayerManager.instance.album == null ||
                        PlayerManager.instance.album!!.albumId != musicAlbum.albumId
                    ) {
                        PlayerManager.instance.loadAlbum(musicAlbum)
                    }
                }

            })
    }

}