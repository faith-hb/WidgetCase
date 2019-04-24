## Add project specific ProGuard rules here.
## You can control the set of applied configuration files using the
## proguardFiles setting in build.gradle.
##
## For more details, see
##   http://developer.android.com/guide/developing/tools/proguard.html
#
## If your project uses WebView with JS, uncomment the following
## and specify the fully qualified class name to the JavaScript interface
## class:
##-keepclassmembers class fqcn.of.javascript.interface.for.webview {
##   public *;
##}
#
## Uncomment this to preserve the line number information for
## debugging stack traces.
##-keepattributes SourceFile,LineNumberTable
#
## If you keep the line number information, uncomment this to
## hide the original source file name.
##-renamesourcefileattribute SourceFile
#
#
#
#
##############################################
##
## 混淆知识点：
## 1.https://www.jianshu.com/p/60e82aafcfd0
##
##############################################
# 代码混淆压缩比，在0~7之间，默认为5，一般不做修改
-optimizationpasses 5
#
## 混合时不使用大小写混合，混合后的类名为小写
#-dontusemixedcaseclassnames
#
## 指定不去忽略非公共库的类
#-dontskipnonpubliclibraryclasses
#
## 这句话能够使我们的项目混淆后产生映射文件
## 包含有类名->混淆后类名的映射关系
#-verbose
#
## 指定不去忽略非公共库的类成员
#-dontskipnonpubliclibraryclassmembers
#
## 不做预校验，preverify是proguard的四个步骤之一，Android不需要preverify，去掉这一步能够加快混淆速度。
##-dontpreverify
#
## 保留Annotation不混淆
#-keepattributes *Annotation*,InnerClasses
#
## 避免混淆泛型
#-keepattributes Signature
#
## 抛出异常时保留代码行号
#-keepattributes SourceFile,LineNumberTable
#
## 指定混淆是采用的算法，后面的参数是一个过滤器
## 这个过滤器是谷歌推荐的算法，一般不做更改
#-optimizations !code/simplification/cast,!field/*,!class/merging/*
#
#
##############################################
##
## Android开发中一些需要保留的公共部分
##
##############################################
#
## 保留support下的所有类及其内部类
#-keep class android.support.** {*;}
#
## 保留继承的
#-keep public class * extends android.support.v4.**
#-keep public class * extends android.support.v7.**
#-keep public class * extends android.support.annotation.**
#
## 保留R下面的资源
#-keep class **.R$* {*;}
#
## 保留本地native方法不被混淆
#-keepclasseswithmembernames class * {
#    native <methods>;
#}
#
#
## 保留枚举类不被混淆
#-keepclassmembers enum * {
#    public static **[] values();
#    public static ** valueOf(java.lang.String);
#}
#
## 保留我们自定义控件（继承自View）不被混淆
#-keep public class * extends android.view.View{
#    *** get*();
#    void set*(***);
#    public <init>(android.content.Context);
#    public <init>(android.content.Context, android.util.AttributeSet);
#    public <init>(android.content.Context, android.util.AttributeSet, int);
#}
#
## 保留Parcelable序列化类不被混淆
#-keep class * implements android.os.Parcelable {
#    public static final android.os.Parcelable$Creator *;
#}
#
## 保留Serializable序列化的类不被混淆
#-keepnames class * implements java.io.Serializable
#-keepclassmembers class * implements java.io.Serializable {
#    static final long serialVersionUID;
#    private static final java.io.ObjectStreamField[] serialPersistentFields;
#    !static !transient <fields>;
#    !private <fields>;
#    !private <methods>;
#    private void writeObject(java.io.ObjectOutputStream);
#    private void readObject(java.io.ObjectInputStream);
#    java.lang.Object writeReplace();
#    java.lang.Object readResolve();
#}
#
#
## 不混淆某个类（使用者可以看到类名）
#-keep class com.doyou.cv.widget.RingView
## 不混淆某个类中以 public 开始的方法（使用者可以看到该方法）
#-keepclassmembers class com.doyou.cv.widget.RingView {
#    public *;
#}




# https://www.jianshu.com/p/e8b41382a7c2
# 表示混淆时不使用大小写混合类名
-dontusemixedcaseclassnames
# 表示不跳过library中的非public的类
-dontskipnonpubliclibraryclasses
# 打印混淆的详细信息
-verbose

# Optimization is turned off by default. Dex does not like code run
# through the ProGuard optimize and preverify steps (and performs some
# of these optimizations on its own).
-dontoptimize
# 表示不进行校验,这个校验作用 在java平台上的
-dontpreverify
# Note that if you want to enable optimization, you cannot just
# include optimization flags in your own project configuration file;
# instead you will need to point to the
# "proguard-android-optimize.txt" file instead of this one from your
# project.properties file.

-keepattributes *Annotation*
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService

# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames class * {
    native <methods>;
}

# keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

# We want to keep methods in Activity that could be used in the XML attribute onClick
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator CREATOR;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

# -dontwarn 含义：声明不输出那些未找到的引用和一些错误
# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.
-dontwarn android.support.**

# -dontnote 含义：声明不输出那些潜在的错误和缺失
# 处理重复定义的混淆文件问题（there were 11 duplicate class definitions，比如android.net.http.SslError | org.apache.http.params.HttpParams等）
-dontnote android.net.http.*
-dontnote org.apache.http.**

# Understand the @Keep support annotation.
-keep class android.support.annotation.Keep

-keep @android.support.annotation.Keep class * {*;}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <methods>;
}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <fields>;
}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <init>(...);
}

# 第三方库不混淆
-dontwarn com.dongni.**
-keep class com.dongni.** { *; }

# 页面modle避免混淆
-keep class com.doyou.cv.bean.**{*; }
-keep class com.doyou.cv.WLogger{*; }


-keep class com.doyou.cv.widget.**
-keepclassmembers class com.doyou.cv.widget.** {
    public *;
}

# 不混淆某个类（使用者可以看到类名）
#-keep class com.doyou.cv.widget.RingView
## 不混淆某个类中以 public 开始的方法（使用者可以看到该方法）
#-keepclassmembers class com.doyou.cv.widget.RingView {
#    public *;
#}
## 不混淆某个类（使用者可以看到类名）
#-keep class com.doyou.cv.widget.horbar.HorBarView
## 不混淆某个类中以 public 开始的方法（使用者可以看到该方法）
#-keepclassmembers class com.doyou.cv.widget.horbar.HorBarView {
#    public *;
#}
## 不混淆某个类（使用者可以看到类名）
#-keep class com.doyou.cv.widget.TaperChart
## 不混淆某个类中以 public 开始的方法（使用者可以看到该方法）
#-keepclassmembers class com.doyou.cv.widget.TaperChart {
#    public *;
#}# 不混淆某个类（使用者可以看到类名）
#-keep class com.doyou.cv.widget.TaperChartLayout
## 不混淆某个类中以 public 开始的方法（使用者可以看到该方法）
#-keepclassmembers class com.doyou.cv.widget.TaperChartLayout {
#    public *;
#}