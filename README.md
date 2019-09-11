# WidgetCase

<!--https://shields.io/-->
<!--[![auc][aucsvg]][auc] [![api][apisvg]][api] [![License][licensesvg]][license]-->

[![](https://img.shields.io/badge/widget-1.1.2-green.svg)](https://github.com/faith-hb/WidgetCase/) [![](https://img.shields.io/badge/platform-android-blue.svg)](https://github.com/faith-hb/WidgetCase) ![](https://img.shields.io/badge/Gradle-5.1.1-orange.svg) ![](https://img.shields.io/badge/maven-2.0-lightgrey.svg)

> [English Introduction](https://github.com/faith-hb/WidgetCase/blob/master/README_EN.md)

#### 项目介绍
不会自定义View？不会触摸反馈？莫慌，这里将消除你的畏惧感。本库是跟着公司项目走了一年之久的自定义控件Library，稳定性和兼容性已有所保障。由于公司项目偏向于统计类，所以项目里更多的是这方面的控件；但既然把库都抽出来了，就绝不可能只是涉及到统计类这块，会发散到更多的应用场景中去。因此，此库会一直维护下去(决心表的很大)。当然一个人的力量总是有限的，在这期待更多志同道合的码友们的加入，大家在一起干点轰轰烈烈的事情。

#### 特别声明
Library中部分控件来自HenCoder plus 3期课程内容，HenCoder是一个针对Android进阶很有帮助的内容（质量非常高）输出平台，如对HenCoder有兴趣的可以去 https://hencoder.com/ 了解更多情况


#### 效果刷一波，毕竟是看脸吃饭的年代
<!--![功能目录](screenshots/main.jpg){:height="960px" width="540px"}-->

<!--![功能目录](screenshots/main.jpg = 100*)-->

##### 功能目录
<img src="screenshots/main.jpg" width="280" height="606"/>   <img src="screenshots/main_menu.jpg" width="280" height="606"/>   <img src="screenshots/main_btm.jpg" width="280" height="606"/>

##### 峰值图>>>[TaperChart & TaperChartLayout & HorTaperChart] & 渐变曲线>>>[GradientLine]
![image](http://47.93.38.184/github/widgetcase/taperchart.gif)  ![image](http://47.93.38.184/github/widgetcase/hortapercharts.gif)  ![image](http://47.93.38.184/github/widgetcase/gradientline.gif)

#####  (波浪加载控件)[CircleWaveView] & (仿网易一元夺宝下拉效果)[RefreshView]
![image](https://github.com/faith-hb/WidgetCase/blob/master/screenshots/circlewaveview.gif)  ![image](https://github.com/faith-hb/WidgetCase/blob/master/screenshots/refreshview.gif)

#####  图文混排>>>[ImgTxtMixtureView] & 手势放大缩小图片>>>[MatrixView]
<img src="screenshots/imgtxtmixture.png" width="320" height="565"/>  ![image](https://github.com/faith-hb/WidgetCase/blob/master/screenshots/matrixview.gif)

##### 圆环进度>>>[RingView & LegendRingView]
<img src="screenshots/ringview_123.jpeg" width="280" height="497"/>   <img src="screenshots/ringview_456.png" width="280" height="497"/>   <img src="screenshots/ringview_789.png" width="280" height="497"/>

##### LFT：圆环进度带底部角标>>>[CircleProgressBarView]  MID：带刻度的圆环>>>[GradientProBar]  RGT：横向进度-带进度提示>>>[HorProBar]
<img src="screenshots/circleprogressbarview.jpeg" width="280" height="497"/>   <img src="screenshots/gradientprobar.jpeg" width="280" height="497"/>   <img src="screenshots/horprobar.png" width="280" height="497"/>

#####  图片滤镜>>>[ColorMatrixActivity & ColorHueActivity]
![image](https://github.com/faith-hb/WidgetCase/blob/master/screenshots/colormartix.gif)

#### 自定义控件分布如下

##### 峰值图
1. [TaperChart](https://github.com/faith-hb/WidgetCase/blob/master/hawkeye_widget/src/main/java/com/doyou/cv/widget/taperchart/TaperChart.java)
2. [TaperChartLayout](https://github.com/faith-hb/WidgetCase/blob/master/hawkeye_widget/src/main/java/com/doyou/cv/widget/taperchart/TaperChartLayout.java)
3. [HorTaperChart](https://github.com/faith-hb/WidgetCase/blob/master/hawkeye_widget/src/main/java/com/doyou/cv/widget/taperchart/HorTaperChart.java)

##### 波浪加载控件
[CircleWaveView](https://github.com/faith-hb/WidgetCase/blob/master/hawkeye_widget/src/main/java/com/doyou/cv/widget/wave/CircleWaveView.java)

##### 仿网易一元夺宝下拉刷新控件
[RefreshView](https://github.com/faith-hb/WidgetCase/blob/master/hawkeye_widget/src/main/java/com/doyou/cv/widget/touch/RefreshView.java)

##### 圆形进度条
1. [CircleProgress](https://github.com/faith-hb/WidgetCase/blob/master/hawkeye_widget/src/main/java/com/doyou/cv/widget/circle/CircleProgress.java)
2. [GradientProBar](https://github.com/faith-hb/WidgetCase/blob/master/hawkeye_widget/src/main/java/com/doyou/cv/widget/circle/GradientProBar.java)
3. [CircleProgressBarView](https://github.com/faith-hb/WidgetCase/blob/master/hawkeye_widget/src/main/java/com/doyou/cv/widget/circle/CircleProgressBarView.java)
4. [CircleView](https://github.com/faith-hb/WidgetCase/blob/master/hawkeye_widget/src/main/java/com/doyou/cv/widget/circle/CircleView.java)

##### 环形进度条
1. [RingView](https://github.com/faith-hb/WidgetCase/blob/master/hawkeye_widget/src/main/java/com/doyou/cv/widget/ring/RingView.java)
2. [LegendRingView(内含图例)](https://github.com/faith-hb/WidgetCase/blob/master/hawkeye_widget/src/main/java/com/doyou/cv/widget/ring/LegendRingView.java)

##### 半圆/油表
1. [HalfCircleProView](https://github.com/faith-hb/WidgetCase/blob/master/hawkeye_widget/src/main/java/com/doyou/cv/widget/HalfCircleProView.java)

##### 横向进度条
1. [HorProBar](https://github.com/faith-hb/WidgetCase/blob/master/hawkeye_widget/src/main/java/com/doyou/cv/widget/horbar/HorProBar.java)
2. [SectionProBar](https://github.com/faith-hb/WidgetCase/blob/master/hawkeye_widget/src/main/java/com/doyou/cv/widget/horbar/SectionProBar.java)
3. [ShadowProBar](https://github.com/faith-hb/WidgetCase/blob/master/hawkeye_widget/src/main/java/com/doyou/cv/widget/horbar/ShadowProBar.java)
4. [LoadingLineView](https://github.com/faith-hb/WidgetCase/blob/master/hawkeye_widget/src/main/java/com/doyou/cv/widget/horbar/LoadingLineView.java)

##### 渐变曲线
[GradientLine](https://github.com/faith-hb/WidgetCase/blob/master/hawkeye_widget/src/main/java/com/doyou/cv/widget/GradientLine.java)

##### 图文混排
[ImgTxtMixtureView](https://github.com/faith-hb/WidgetCase/blob/master/hawkeye_widget/src/main/java/com/doyou/cv/widget/view/ImgTxtMixtureView.java)

##### 防微信图片查看器，双击放大/缩小
[MatrixView](https://github.com/faith-hb/WidgetCase/blob/master/hawkeye_widget/src/main/java/com/doyou/cv/widget/touch/MatrixView.java)

##### 系统控件重写
1. [FixGridView](https://github.com/faith-hb/WidgetCase/blob/master/hawkeye_widget/src/main/java/com/doyou/cv/widget/sys/FixGridView.java)
2. [FixListView](https://github.com/faith-hb/WidgetCase/blob/master/hawkeye_widget/src/main/java/com/doyou/cv/widget/sys/FixListView.java)
3. [NoScrollViewPager](https://github.com/faith-hb/WidgetCase/blob/master/hawkeye_widget/src/main/java/com/doyou/cv/widget/sys/NoScrollViewPager.java)

##### 贝塞尔曲线练练手
[PathMorphBezier](https://github.com/faith-hb/WidgetCase/blob/master/hawkeye_widget/src/main/java/com/doyou/cv/widget/PathMorphBezier.java)

##### 图片滤镜
1. [ColorMatrix](https://github.com/faith-hb/WidgetCase/blob/master/app/src/main/java/com/doyou/cvc/release/colorfilter/ColorMatrixActivity.kt)
2. [ColorHue](https://github.com/faith-hb/WidgetCase/blob/master/app/src/main/java/com/doyou/cvc/release/colorfilter/ColorHueActivity.kt)

##### 时间刻度尺（支持缩放）
[TimeRulerView](https://github.com/faith-hb/WidgetCase/blob/master/hawkeye_widget/src/main/java/com/doyou/cv/widget/timeruler/TimeRulerView.java)

#### Download

Step 1.

``` groovy
allprojects {
    repositories {
        ...
        maven {
            url 'http://47.93.38.184:8081/nexus/content/repositories/faithhb/'
        }
        maven {
            url 'http://47.93.38.184:8081/nexus/content/repositories/github/' // widget发布版本已切换到该目录
        }
    }
}
```

Step 2.

Gradle:
``` groovy
implementation 'com.faithhb:widgetcase:1.0.3'
```

widgetcase最新版本：[![](https://img.shields.io/badge/widget-1.0.3-green.svg)](http://47.93.38.184:8081/nexus/service/local/repositories/github/content/com/github/widgetcase/1.0.3/widgetcase-1.0.3.aar)

or

aar:
``` groovy
implementation(name: 'hawkeye_widget-release', ext: 'aar')
```

or

添加本地依赖：
``` groovy
implementation project(':hawkeye_widget')
```

#### Proguard
``` groovy
-dontwarn com.dongni.**
-keep class com.dongni.** { *; }

# 页面modle避免混淆
-keep class com.doyou.cv.bean.**{*; }
-keep class com.doyou.cv.WLogger{*; }

-keep class com.doyou.cv.widget.**
-keepclassmembers class com.doyou.cv.widget.** {
    public *;
}

# 工具类避免混淆
-keep class com.doyou.cv.utils.**
-keepclassmembers class com.doyou.cv.utils.** {
    public *;
}
```

#### 有问题怎么办？
首先有问题的话，可以提issue，我会经常去看的，也可以加我微信。

#### 想加入一起来维护这个开源项目，该怎么操作？
你可以先fork下来这个项目然后自己去修改一些网友的问题或者自己发现的问题和需要优化的地方；当然也可以加入自己原创的自定义控件；再通过发pr的形式给我，我会第一时间查看代码并进行测试及合并代码。
这样坚持提交超过3个pr的小伙伴（并且质量不错的），我会直接邀请他称为该项目的开发者，真正做到方便，快速的维护这个开源库。。。

#### 联系方式：
微信：faith-hb<br>
QQ：907167515

#### License

   Copyright 2019 faith-hb

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
