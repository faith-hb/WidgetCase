package com.doyou.tools

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

/**
 * Description：剪贴板工具类
 * @author:hongb
 * @date:2024/7/2
 */
object ClipboardUtil {
    fun copyContent(context: Context, str: String?) {
        if (str.isNullOrEmpty()) return
        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Label", str)
        cm.primaryClip = clipData
    }
}