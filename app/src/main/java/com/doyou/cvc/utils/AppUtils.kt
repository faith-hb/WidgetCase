package com.doyou.cvc.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.doyou.tools.EmptyUtil.Companion.isNotEmpty

/**
 * 工具类
 *
 * @autor hongbing
 * @date 2019-11-27
 */
object AppUtils {
    /**
     * 复制文本
     *
     * @param context
     * @param content
     */
    fun copyContent(context: Context, content: String?) {
        if (isNotEmpty(content)) {
            val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("Label", content)
            cm.primaryClip = clipData
        }
    }

    /**
     * 调用系统浏览器打开指定网址
     *
     * @param context
     * @param url
     */
    fun openUrlByBrowser(context: Context, url: String?) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(intent)
    }
}