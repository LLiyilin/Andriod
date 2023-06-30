package com.example.puremusic.ui.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.example.puremusic.R
import com.example.puremusic.bridge.player.PlayerManager
import com.example.puremusic.bridge.state.PlayerViewModel
import com.example.puremusic.databinding.FragmentPlayerBinding
import com.example.puremusic.ui.base.BaseFragment
import com.example.puremusic.ui.view.PlayerSlideListener

class PlayerFragment : BaseFragment() {
    private var binding: FragmentPlayerBinding? = null
    private var playerViewModel: PlayerViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playerViewModel = getFragmentViewModelProvider(this).get(PlayerViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_player, container, false)
        binding = FragmentPlayerBinding.bind(view)
        binding?.vm = playerViewModel
        binding?.click = ClickProxy()
        binding?.event = EventHandler()

        return view
    }

    // 观察变化 很多双眼睛
    // 观察到数据的变化，我就变化
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 观察：此字段只要发生了改变，就会 添加监听（可以弹上来的监听）
        sharedViewModel.timeToAddSlideListener.observe(viewLifecycleOwner, {
            if (view.parent.parent is SlidingUpPanelLayout) {
                val sliding = view.parent.parent as SlidingUpPanelLayout
                // 添加监听（可以弹上来的监听）
                sliding.addPanelSlideListener(PlayerSlideListener(binding!!, sliding))
            }
        })
        // 我是播放条，我要去变化，我成为观察者 ---> 播放相关的类 PlayerManager
        PlayerManager.instance.changeMusicLiveData.observe(viewLifecycleOwner, { changeMusic ->
            // 例如 ：理解 切歌的时候， 音乐的标题，作者，封面 状态等 改变
            playerViewModel!!.title.set(changeMusic.title)
            playerViewModel!!.artist.set(changeMusic.summary)
            playerViewModel!!.coverImg.set(changeMusic.img)
        })


        // 我是播放条，我要去变化，我成为观察者 -----> 播放相关的类PlayerManager
        PlayerManager.instance.playingMusicLiveData.observe(viewLifecycleOwner, { playingMusic ->

            // 例如 ：理解 切歌的时候，  播放进度的改变  按钮图标的改变
            playerViewModel!!.maxSeekDuration.set(playingMusic.duration) // 总时长
            playerViewModel!!.currentSeekPosition.set(playingMusic.playerPosition) // 拖动条
        })
// 播放/暂停是一个控件  图标的true和false
        PlayerManager.instance.pauseLiveData.observe(viewLifecycleOwner, { aBoolean ->
            playerViewModel!!.isPlaying.set(!aBoolean!!) // 播放时显示暂停，暂停时显示播放
        })

        // 可以控制 播放详情 点击/back 掉下来
        // 例如：场景  back  要不要做什么事情
        sharedViewModel.closeSlidePanelIfExpanded.observe(viewLifecycleOwner, {
            if (view.parent.parent is SlidingUpPanelLayout) {
                val sliding = view.parent.parent as SlidingUpPanelLayout

                // 如果是扩大，也就是，详情页面展示出来的
                if (sliding.panelState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    sliding.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED // 缩小了（掉下来了）
                    sharedViewModel.activityCanBeClosedDirectly.setValue(true) // 活动关闭的一些记录（播放条 缩小一条 与 扩大展开）
                } else {
                    sharedViewModel.activityCanBeClosedDirectly.setValue(false) // 活动关闭的一些记录（播放条 缩小一条 与 扩大展开）
                }
            } else {
                sharedViewModel.activityCanBeClosedDirectly.setValue(false) // 活动关闭的一些记录（播放条 缩小一条 与 扩大展开）
            }
        })
    }


    // 内部类的好处，方便获取 当前Fragment的 环境
    /*// 点击事件
    {
        sharedViewModel.closeSlidePanelIfExpanded = true
    }*/
    /**
     * 当我们点击的时候，我们要触发
     */
    inner class ClickProxy {
        fun previous() = PlayerManager.instance.playPrevious() // 上一首

        operator fun next() = PlayerManager.instance.playNext() // 下一首

        // 左手边的滑落，点击缩小的，可以控制 播放详情 点击/back 掉下来
        fun slideDown() = sharedViewModel.closeSlidePanelIfExpanded.setValue(true)

        fun togglePlay() = PlayerManager.instance.togglePlay()

        fun playMode() = PlayerManager.instance.changeMode() // 播放

    }

    inner class EventHandler : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
        }

        override fun onStopTrackingTouch(seekBar: SeekBar) {
            PlayerManager.instance.setSeek(seekBar.progress)
        }

    }

}