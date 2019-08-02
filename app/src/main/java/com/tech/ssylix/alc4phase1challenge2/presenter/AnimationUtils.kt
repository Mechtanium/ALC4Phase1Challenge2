package com.tech.ssylix.alc4phase1challenge2.presenter

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator

fun View.animateClicks(dur: Long = 20, function: (() -> Unit)? = null) {
    val bouncerXF1 = ObjectAnimator.ofFloat(this, "scaleX", 1f, 0.7f)
    val bouncerYF1 = ObjectAnimator.ofFloat(this, "scaleY", 1f, 0.7f)
    val bouncerXR1 = ObjectAnimator.ofFloat(this, "scaleX", 0.7f, 1f)
    val bouncerYR1 = ObjectAnimator.ofFloat(this, "scaleY", 0.7f, 1f)
    val bouncerXF2 = ObjectAnimator.ofFloat(this, "scaleX", 1f, 0.85f)
    val bouncerYF2 = ObjectAnimator.ofFloat(this, "scaleY", 1f, 0.85f)
    val bouncerXR2 = ObjectAnimator.ofFloat(this, "scaleX", 0.85f, 1f)
    val bouncerYR2 = ObjectAnimator.ofFloat(this, "scaleY", 0.85f, 1f)

    bouncerXF1.interpolator = DecelerateInterpolator()
    bouncerYF1.interpolator = DecelerateInterpolator()

    bouncerXR1.interpolator = AccelerateInterpolator()
    bouncerYR1.interpolator = AccelerateInterpolator()

    bouncerXF2.interpolator = DecelerateInterpolator()
    bouncerYF2.interpolator = DecelerateInterpolator()

    bouncerXR2.interpolator = AccelerateInterpolator()
    bouncerYR2.interpolator = AccelerateInterpolator()

    /*arrayListOf(bouncerXF1, bouncerXF2, bouncerYF1, bouncerYF2, bouncerXR1, bouncerXR2, bouncerYR1, bouncerYR2).forEach {

    }*/

    AnimatorSet().apply {
        playSequentially(
            AnimatorSet().apply {
                playTogether(bouncerXF1, bouncerYF1)
            },
            AnimatorSet().apply {
                playTogether(bouncerXR1, bouncerYR1)
            },
            AnimatorSet().apply {
                playTogether(bouncerXF2, bouncerYF2)
            },
            AnimatorSet().apply {
                playTogether(bouncerXR2, bouncerYR2)
            }
        )

        duration = dur
        addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                function?.invoke()
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationStart(animation: Animator?) {

            }
        })

        start()
    }
}

fun View.animateClicksRotation(reverse: Boolean, dur: Long = 20, function: (() -> Unit)? = null) {
    if (!reverse) {
        ObjectAnimator.ofFloat(this, "rotation", 0f, 45f)
    } else {
        ObjectAnimator.ofFloat(this, "rotation", 45f, 0f)
    }.apply {
        duration = dur
        interpolator = DecelerateInterpolator()
        addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                function?.invoke()
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationStart(animation: Animator?) {

            }
        })

        start()
    }
}