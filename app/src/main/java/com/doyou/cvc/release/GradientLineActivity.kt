package com.doyou.cvc.release

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.doyou.cv.widget.GradientLine
import com.doyou.cvc.R
import kotlinx.android.synthetic.main.activity_gradientline.*

class GradientLineActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gradientline)

        gLineUp.postDelayed({
            gLineUp.setAnim(true)
            gLineUp.setMode(GradientLine.LineMode.DOWN)
        },500)

        gLineFlat.setAnim(true)
        gLineFlat.postDelayed({
            gLineFlat.setMode(GradientLine.LineMode.FLAT)
        },500)

        gLineDown.postDelayed({
            gLineDown.setAnim(true,640)
            gLineDown.setMode(GradientLine.LineMode.UP)
        },500)


        gLineBtn.setOnClickListener {
            gLineUp.postDelayed({
                gLineUp.handleStartAnim()
            }, 120)


            gLineFlat.handleStartAnim()

            gLineDown.postDelayed({
                gLineDown.handleStartAnim()
            }, 120)
        }

        gLineAgain.setAnim(true,640)
        gLineAgain.setMode(GradientLine.LineMode.FLAT)
        var isTure = false
        gLineBtn2.setOnClickListener {
            if (isTure){
                isTure = false
                gLineAgain.setMode(GradientLine.LineMode.FLAT)
                gLineAgain.handleStartAnim()
            }else{
                isTure = true
                gLineAgain.setMode(GradientLine.LineMode.UP)
                gLineAgain.handleStartAnim()
            }
        }
    }
}
