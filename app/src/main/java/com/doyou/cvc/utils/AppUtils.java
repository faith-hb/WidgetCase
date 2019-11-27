package com.doyou.cvc.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.dongni.tools.EmptyUtils;

/**
 * 工具类
 *
 * @autor hongbing
 * @date 2019-11-27
 */
public final class AppUtils {

    /**
     * 复制文本
     *
     * @param context
     * @param content
     */
    public static void copyContent(Context context, String content) {
        if (EmptyUtils.isNotEmpty(content)) {
            ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("Label", content);
            cm.setPrimaryClip(clipData);
        }
    }

    /**
     * 调用系统浏览器打开指定网址
     *
     * @param context
     * @param url
     */
    public static void openUrlByBrowser(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }
}
