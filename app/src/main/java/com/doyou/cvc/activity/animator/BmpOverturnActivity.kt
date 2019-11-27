package com.doyou.cvc.activity.animator

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.doyou.cvc.R
import kotlinx.android.synthetic.main.activity_bmp_overturn.*

class BmpOverturnActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmp_overturn)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.title_animator_bmp_overturn)

        bovBtn.setOnClickListener {
            bov.propReset()
            startAnim(false)
        }
        startAnim(true)
    }

    private fun startAnim(isFirst: Boolean) {
        val bottomFlipAnimator = ObjectAnimator.ofFloat(bov, bov.propIsBottomFlip, 30f)
        bottomFlipAnimator.duration = 1000

        val topFlipAnimator = ObjectAnimator.ofFloat(bov, bov.propIsTopFlip, -30f)
        topFlipAnimator.startDelay = 200
        topFlipAnimator.duration = 1000

        val rotationFlipAnimator = ObjectAnimator.ofFloat(bov, bov.propFlipRotation, 270f)
        rotationFlipAnimator.startDelay = 200
        rotationFlipAnimator.duration = 1000

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(bottomFlipAnimator, rotationFlipAnimator, topFlipAnimator)
        if (isFirst) {
            animatorSet.startDelay = 1000
        }
        animatorSet.start()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
