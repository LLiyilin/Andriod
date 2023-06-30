package com.example.puremusic.ui.view

import android.animation.ArgbEvaluator
import android.animation.FloatEvaluator
import android.animation.IntEvaluator
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.Transformation
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState
import com.example.architecture.utils.DisplayUtils
import com.example.architecture.utils.ScreenUtils
import com.example.puremusic.R
import com.example.puremusic.databinding.FragmentPlayerBinding

/**
 * SlidingUpPanelLayout：可以上下滑动的菜单布
 * 可以认为是对：SlidingUpPanelLayout 进行了封装
 */
class PlayerSlideListener(
    private val mBinding: FragmentPlayerBinding,
    private val mSlidingUpPanelLayout: SlidingUpPanelLayout
) : SlidingUpPanelLayout.PanelSlideListener {
    private val screenWidth: Int
    private val screenHeight: Int
    private val intEvaluator = IntEvaluator()
    private val floatEvaluator = FloatEvaluator()
    private val colorEvaluator = ArgbEvaluator()
    private val nowPlayingCardColor: Int
    private val playPauseDrawableColor: Int
    private var albumImageDrawable: Drawable? = null
    private var titleEndTranslationX = 0
    private var artistEndTranslationX = 0
    private var artistNormalEndTranslationY = 0
    private var artistFullEndTranslationY = 0
    private var contentNormalEndTranslationY = 0
    private var contentFullEndTranslationY = 0


    private var previousStartX = 0
    private var playPauseStartX = 0
    private var nextStartX = 0
    private var playPauseEndX = 0
    private var previousEndX = 0
    private var markEndX = 0
    private var nextEndX = 0
    private var playqueueEndX = 0
    private var iconContainerStartY = 0
    private var iconContainerEndY = 0
    private var mStatus = Status.COLLAPSED

    override fun onPanelSlide(panel: View, slideOffset: Float) {
        val params = mBinding.albumArt.layoutParams as CoordinatorLayout.LayoutParams

        //animate albumImage
        val tempImgSize = intEvaluator.evaluate(slideOffset, DisplayUtils.dp2px(55f), screenWidth)
        params.width = tempImgSize
        params.height = tempImgSize
        mBinding.albumArt.layoutParams = params

        //animate title and artist
        mBinding.title.translationX = floatEvaluator.evaluate(slideOffset, 0, titleEndTranslationX)
        mBinding.artist.translationX =
            floatEvaluator.evaluate(slideOffset, 0, artistEndTranslationX)
        mBinding.artist.translationY =
            floatEvaluator.evaluate(slideOffset, 0, artistNormalEndTranslationY)
        mBinding.summary.translationY =
            floatEvaluator.evaluate(slideOffset, 0, contentNormalEndTranslationY)

        //aniamte icons
        mBinding.playPause.x =
            intEvaluator.evaluate(slideOffset, playPauseStartX, playPauseEndX).toFloat()
        mBinding.playPause.setCircleAlpah(intEvaluator.evaluate(slideOffset, 0, 255))
        mBinding.playPause.drawableColor =
            colorEvaluator.evaluate(slideOffset, playPauseDrawableColor, nowPlayingCardColor) as Int
        mBinding.previous.x =
            intEvaluator.evaluate(slideOffset, previousStartX, previousEndX).toFloat()

        mBinding.next.x = intEvaluator.evaluate(slideOffset, nextStartX, nextEndX).toFloat()

        mBinding.previous.alpha = floatEvaluator.evaluate(slideOffset, 0, 1)
        mBinding.iconContainer.y =
            intEvaluator.evaluate(slideOffset, iconContainerStartY, iconContainerEndY).toFloat()
        val params1 = mBinding.summary.layoutParams as CoordinatorLayout.LayoutParams
        params1.height =
            intEvaluator.evaluate(slideOffset, DisplayUtils.dp2px(55f), DisplayUtils.dp2px(60f))
        mBinding.summary.layoutParams = params1
    }

    override fun onPanelStateChanged(
        panel: View, previousState: PanelState,
        newState: PanelState
    ) {
        if (previousState == PanelState.COLLAPSED) {
            if (mBinding.songProgressNormal.visibility != View.INVISIBLE) {
                mBinding.songProgressNormal.visibility = View.INVISIBLE
            }

            if (mBinding.previous.visibility != View.VISIBLE) {
                mBinding.previous.visibility = View.VISIBLE
            }
        } else if (previousState == PanelState.EXPANDED) {
            if (mStatus == Status.FULLSCREEN) {
                animateToNormal()
            }
        }
        if (newState == PanelState.EXPANDED) {
            mStatus = Status.EXPANDED
            toolbarSlideIn(panel.context)

            mBinding.previous.isClickable = true
            mBinding.topContainer.setOnClickListener { v: View? ->
                if (mStatus == Status.EXPANDED) {
                    animateToFullscreen()
                } else if (mStatus == Status.FULLSCREEN) {
                    animateToNormal()
                } else {
                    mSlidingUpPanelLayout.panelState = PanelState.COLLAPSED
                }
            }
        } else if (newState == PanelState.COLLAPSED) {
            mStatus = Status.COLLAPSED
            if (mBinding.songProgressNormal.visibility != View.VISIBLE) {
                mBinding.songProgressNormal.visibility = View.VISIBLE
            }

            if (mBinding.previous.visibility != View.GONE) {
                mBinding.previous.visibility = View.GONE
            }
            mBinding.topContainer.setOnClickListener { v: View? ->
                if (mSlidingUpPanelLayout.isTouchEnabled) {
                    mSlidingUpPanelLayout.panelState = PanelState.EXPANDED
                }
            }
        } else if (newState == PanelState.DRAGGING) {
            if (mBinding.customToolbar.visibility != View.INVISIBLE) {
                mBinding.customToolbar.visibility = View.INVISIBLE
            }

        }
    }

    private fun caculateTitleAndArtist() {
        val titleBounds = Rect()
        mBinding.title.paint.getTextBounds(
            mBinding.title.text.toString(), 0,
            mBinding.title.text.length, titleBounds
        )
        val titleWidth = titleBounds.width()
        val artistBounds = Rect()
        mBinding.artist.paint.getTextBounds(
            mBinding.artist.text.toString(), 0,
            mBinding.artist.text.length, artistBounds
        )
        val artistWidth = artistBounds.width()
        titleEndTranslationX = screenWidth / 2 - titleWidth / 2 - DisplayUtils.dp2px(67f)
        artistEndTranslationX = screenWidth / 2 - artistWidth / 2 - DisplayUtils.dp2px(67f)
        artistNormalEndTranslationY = DisplayUtils.dp2px(12f)
        artistFullEndTranslationY = 0
        contentNormalEndTranslationY = screenWidth + DisplayUtils.dp2px(32f)
        contentFullEndTranslationY = DisplayUtils.dp2px(32f)
        if (mStatus == Status.COLLAPSED) {
            mBinding.title.translationX = 0f
            mBinding.artist.translationX = 0f
        } else {
            mBinding.title.translationX = titleEndTranslationX.toFloat()
            mBinding.artist.translationX = artistEndTranslationX.toFloat()
        }
    }

    private fun caculateIcons() {
        previousStartX = mBinding.previous.left
        playPauseStartX = mBinding.playPause.left
        nextStartX = mBinding.next.left

        val size = DisplayUtils.dp2px(36f)
        val gap = (screenWidth - 5 * size) / 6
        playPauseEndX = screenWidth / 2 - size / 2
        previousEndX = playPauseEndX - gap - size
        markEndX = previousEndX - gap - size
        nextEndX = playPauseEndX + gap + size
        playqueueEndX = nextEndX + gap + size
        iconContainerStartY = mBinding.iconContainer.top
        iconContainerEndY =
            screenHeight - 3 * mBinding.iconContainer.height - mBinding.seekBottom.height
    }


    private fun toolbarSlideIn(context: Context) {
        mBinding.customToolbar.post {
            val animation = AnimationUtils.loadAnimation(context, R.anim.toolbar_slide_in)
            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {
                    mBinding.customToolbar.visibility = View.VISIBLE
                }

                override fun onAnimationEnd(animation: Animation) {}
                override fun onAnimationRepeat(animation: Animation) {}
            })
            mBinding.customToolbar.startAnimation(animation)
        }
    }

    private fun animateToFullscreen() {
        //album art fullscreen
        albumImageDrawable = mBinding.albumArt.drawable

        //animate title and artist
        val contentAnimation: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                mBinding.summary.translationY =
                    contentNormalEndTranslationY - (contentNormalEndTranslationY - contentFullEndTranslationY) * interpolatedTime
                mBinding.artist.translationY =
                    artistNormalEndTranslationY - (artistNormalEndTranslationY - artistFullEndTranslationY) * interpolatedTime
            }
        }
        contentAnimation.duration = 150
        mBinding.artist.startAnimation(contentAnimation)

        mStatus = Status.FULLSCREEN
    }

    private fun animateToNormal() {
        //album art
        val imageLayout = mBinding.albumArt.layoutParams as CoordinatorLayout.LayoutParams
        imageLayout.height = screenWidth
        imageLayout.width = screenWidth
        mBinding.albumArt.layoutParams = imageLayout

        //animate title and artist
        val contentAnimation: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                mBinding.summary.translationY =
                    contentFullEndTranslationY + (contentNormalEndTranslationY - contentFullEndTranslationY) * interpolatedTime
                mBinding.artist.translationY =
                    artistFullEndTranslationY + (artistNormalEndTranslationY - artistFullEndTranslationY) * interpolatedTime
            }
        }
        contentAnimation.duration = 300
        mBinding.artist.startAnimation(contentAnimation)

        mStatus = Status.EXPANDED
    }

    enum class Status {
        EXPANDED, COLLAPSED, FULLSCREEN
    }

    init {
        screenWidth = ScreenUtils.screenWidth
        screenHeight = ScreenUtils.screenHeight
        playPauseDrawableColor = Color.BLACK
        nowPlayingCardColor = Color.WHITE
        caculateTitleAndArtist()
        caculateIcons()
        mBinding.playPause.drawableColor = playPauseDrawableColor
    }
}