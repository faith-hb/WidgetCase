package com.doyou.cvc.activity.sys.viewpager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.collection.LruCache
import androidx.viewpager.widget.PagerAdapter
import com.doyou.cvc.R

/**
 *
 * @autor hongbing
 * @date 2019-11-14
 */
class ViewPagerAdapter : PagerAdapter {

    private var images: List<Int>? = null
    private var mCachePagerItem = LruCache<Int, ImageView>(5)

    constructor(images: List<Int>?) : super() {
        this.images = images
    }

    fun getImages(): List<Int>? {
        return images
    }

    override fun getCount(): Int {
        return images!!.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_UNCHANGED
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ImageView)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var imgV = mCachePagerItem.get(position)
        if (imgV == null) {
            imgV = LayoutInflater.from(container.context).inflate(R.layout.viewpager_itemview, null) as ImageView
            imgV.tag = position
            mCachePagerItem.put(position, imgV)
        }
        if (images!![position] == 0) {
            imgV.setImageResource(R.mipmap.ic_launcher)
        } else {
            imgV.setImageResource(images!![position])
        }
        container.addView(imgV)
        return imgV
    }

}