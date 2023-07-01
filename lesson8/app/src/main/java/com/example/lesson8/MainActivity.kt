package com.example.lesson8

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.VideoView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import java.io.File

class MainActivity : AppCompatActivity() {

    val CH_ID= "private"

    lateinit var manager: NotificationManager

    lateinit var imgUri:Uri

    lateinit var videoView: VideoView

    val player = MediaPlayer()

    val launcher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()){

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        initNotiChannel()

        findViewById<Button>(R.id.btnNotify).setOnClickListener {
            sendNoti()
        }

        // 拍照存入的文件地址

        val outputFile = File(externalCacheDir,"camera.jpg")

        if(outputFile.exists()){
            outputFile.delete()
        }
        outputFile.createNewFile()

        imgUri = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            FileProvider.getUriForFile(this,MyFileProvider.ACTION,outputFile)
        }else{
            Uri.fromFile(outputFile)
        }

        findViewById<Button>(R.id.takePhoto).setOnClickListener {
            takePhoto()
        }
        // 选择相册
        findViewById<Button>(R.id.choosePhoto).setOnClickListener {
            choosePhoto()
        }

        // 音乐播放相关

        initMediaPlayer()

        findViewById<Button>(R.id.mediaPlay).setOnClickListener {
            mediaPlay()
        }
        findViewById<Button>(R.id.mediaPause).setOnClickListener {
            mediaPause()
        }
        findViewById<Button>(R.id.mediaResume).setOnClickListener {
            mediaResume()
        }

        // 视频播放相关

        videoView = findViewById(R.id.videoView)

        videoView.setVideoURI(Uri.parse("android.resource://$packageName/${R.raw.video}"))

        findViewById<Button>(R.id.videoPlay).setOnClickListener {
            videoPlay()
        }
        findViewById<Button>(R.id.videoPause).setOnClickListener {
            videoPause()
        }
        findViewById<Button>(R.id.videoResume).setOnClickListener {
            videoResume()
        }
    }

    private fun videoResume() {
        videoView.resume()
    }

    private fun videoPause() {
        videoView.pause()
    }

    private fun videoPlay() {
        videoView.start()
    }

    private fun initMediaPlayer() {
        player.setDataSource(assets.openFd("music.mp3"))
        player.prepare()
    }

    private fun mediaResume() {
        player.reset()
        initMediaPlayer()
        mediaPlay()
    }

    private fun mediaPause() {
        player.pause()
    }

    private fun mediaPlay() {
        player.start()
//        player.duration
//        player.lis
//        player.seekTo()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.stop()
        player.release()

        videoView.suspend()
    }

    private fun choosePhoto() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"

        startActivityForResult(intent,2048)
    }

    private fun takePhoto() {
        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imgUri)

//        launcher.launch()

        startActivityForResult(intent,1024)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            1024->{
                if(resultCode == RESULT_OK){
                    val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(imgUri))
                    showImg(bitmap)
                }
            }
            2048->{
                if(resultCode == RESULT_OK){
                    data?.data?.let {
                        contentResolver.openFileDescriptor(it,"r")?.use { fd->
                            val bitmap = BitmapFactory.decodeFileDescriptor(fd.fileDescriptor)
                            showImg(bitmap)
                        }
                    }
                }
            }
        }
    }

    private fun showImg(bitmap: Bitmap) {
        findViewById<ImageView>(R.id.img).setImageBitmap(bitmap)
    }

    private fun initNotiChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val ch = NotificationChannel(CH_ID, "私信", NotificationManager.IMPORTANCE_HIGH)
            manager.createNotificationChannel(ch)
        }
    }

    private fun sendNoti() {
        val noti = NotificationCompat.Builder(this,CH_ID)
            .setContentTitle("西南大学通知")
            .setContentText("这个周六要上课，下周五要补课")
            .setSmallIcon(R.drawable.baseline_school_24)
            .setLargeIcon(BitmapFactory.decodeResource(resources,R.mipmap.swu))
            .build()

        manager.notify(1,noti)
    }
}