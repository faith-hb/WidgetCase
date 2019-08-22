# WidgetCase

<!--https://shields.io/-->
<!--[![auc][aucsvg]][auc] [![api][apisvg]][api] [![License][licensesvg]][license]-->

[![](https://img.shields.io/badge/widget-1.1.2-green.svg)](https://github.com/faith-hb/WidgetCase/) [![](https://img.shields.io/badge/platform-android-blue.svg)](https://github.com/faith-hb/WidgetCase) ![](https://img.shields.io/badge/Gradle-5.1.1-orange.svg) ![](https://img.shields.io/badge/maven-2.0-lightgrey.svg)

> [中文文档](https://github.com/faith-hb/WidgetCase/blob/master/README.md)

#### project introduction
With a year of custom control Library, stability and compatibility have been guaranteed. Because the company projects tend to statistics,
so the project is more in this aspect of the control; But now that I've pulled out all the libraries, I can't just talk about the statistics class,
I can diverge to more scenarios. So, the library will be maintained until Android goes down, and I go down. Of course, a person's power is always limited,
in this look forward to more like-minded code friends to join, we work together to do something spectacular.


#### Effect brush a wave, after all, is to see the face to eat


##### Function directory
<img src="screenshots/main.jpg" width="280" height="606"/>   <img src="screenshots/main_menu.jpg" width="280" height="606"/>   <img src="screenshots/main_btm.jpg" width="280" height="606"/>

##### Peak figure>>>[TaperChart & TaperChartLayout & HorTaperChart] & The gradient curve>>>[GradientLine]
![image](http://47.93.38.184/github/widgetcase/taperchart.gif)  ![image](http://47.93.38.184/github/widgetcase/hortapercharts.gif)  ![image](http://47.93.38.184/github/widgetcase/gradientline.gif)

#####  (Wave loading view)[CircleWaveView] & (The drop-down effect)[RefreshView]
![image](https://github.com/faith-hb/WidgetCase/blob/master/screenshots/circlewaveview.gif)  ![image](https://github.com/faith-hb/WidgetCase/blob/master/screenshots/refreshview.gif)

#####  Graphic mixed>>>[ImgTxtMixtureView] & Gestures to zoom>>>[MatrixView]
<img src="screenshots/imgtxtmixture.png" width="320" height="565"/>  ![image](https://github.com/faith-hb/WidgetCase/blob/master/screenshots/matrixview.gif)

##### Graded ring progress>>>[GradientProBar] & Gestures zoom view>>>[MatrixView]
![image](https://github.com/faith-hb/WidgetCase/blob/master/screenshots/gradientprobar.gif)  ![image](https://github.com/faith-hb/WidgetCase/blob/master/screenshots/matrixview.gif)

##### Circle the progress>>>[RingView & LegendRingView]
<img src="screenshots/ringview_123.jpeg" width="280" height="497"/>   <img src="screenshots/ringview_456.png" width="280" height="497"/>   <img src="screenshots/ringview_789.png" width="280" height="497"/>

##### LFT：Circular progress with bottom corner marks>>>[CircleProgressBarView]  MID：Ring with scale>>>[GradientProBar]  RGT：Horizontal progress-Progress indicator>>>[HorProBar]
<img src="screenshots/circleprogressbarview.jpeg" width="280" height="497"/>   <img src="screenshots/gradientprobar.jpeg" width="280" height="497"/>   <img src="screenshots/horprobar.png" width="280" height="497"/>

#####  Image filter>>>[ColorMatrixActivity & ColorHueActivity]
![image](https://github.com/faith-hb/WidgetCase/blob/master/screenshots/colormartix.gif)

#### The custom controls are distributed as follows

##### Peak figure
1. [TaperChart](https://github.com/faith-hb/WidgetCase/blob/master/hawkeye_widget/src/main/java/com/doyou/cv/widget/TaperChart.java)
2. [TaperChartLayout](https://github.com/faith-hb/WidgetCase/blob/master/hawkeye_widget/src/main/java/com/doyou/cv/widget/TaperChartLayout.java)
3. [HorTaperChart](https://github.com/faith-hb/WidgetCase/blob/master/hawkeye_widget/src/main/java/com/doyou/cv/widget/HorTaperChart.java)

##### Wave loading view
[CircleWaveView](https://github.com/faith-hb/WidgetCase/blob/master/hawkeye_widget/src/main/java/com/doyou/cv/widget/wave/CircleWaveView.java)

##### The drop-down effect
[RefreshView](https://github.com/faith-hb/WidgetCase/blob/master/hawkeye_widget/src/main/java/com/doyou/cv/widget/touch/RefreshView.java)

##### Circular progress bar
1. [CircleProgress](https://github.com/faith-hb/WidgetCase/blob/master/hawkeye_widget/src/main/java/com/doyou/cv/widget/circle/CircleProgress.java)
2. [GradientProBar](https://github.com/faith-hb/WidgetCase/blob/master/hawkeye_widget/src/main/java/com/doyou/cv/widget/circle/GradientProBar.java)
3. [CircleProgressBarView](https://github.com/faith-hb/WidgetCase/blob/master/hawkeye_widget/src/main/java/com/doyou/cv/widget/circle/CircleProgressBarView.java)
4. [CircleView](https://github.com/faith-hb/WidgetCase/blob/master/hawkeye_widget/src/main/java/com/doyou/cv/widget/circle/CircleView.java)

##### Ring progress bar
1. [RingView](https://github.com/faith-hb/WidgetCase/blob/master/hawkeye_widget/src/main/java/com/doyou/cv/widget/ring/RingView.java)
2. [LegendRingView(It contains legend)](https://github.com/faith-hb/WidgetCase/blob/master/hawkeye_widget/src/main/java/com/doyou/cv/widget/ring/LegendRingView.java)

##### semicircle/Fuel meter
1. [HalfCircleProView](https://github.com/faith-hb/WidgetCase/blob/master/hawkeye_widget/src/main/java/com/doyou/cv/widget/HalfCircleProView.java)

##### Horizontal progress bar
1. [HorProBar](https://github.com/faith-hb/WidgetCase/blob/master/hawkeye_widget/src/main/java/com/doyou/cv/widget/horbar/HorProBar.java)
2. [SectionProBar](https://github.com/faith-hb/WidgetCase/blob/master/hawkeye_widget/src/main/java/com/doyou/cv/widget/horbar/SectionProBar.java)
3. [ShadowProBar](https://github.com/faith-hb/WidgetCase/blob/master/hawkeye_widget/src/main/java/com/doyou/cv/widget/horbar/ShadowProBar.java)
4. [LoadingLineView](https://github.com/faith-hb/WidgetCase/blob/master/hawkeye_widget/src/main/java/com/doyou/cv/widget/horbar/LoadingLineView.java)

##### The gradient curve
[GradientLine](https://github.com/faith-hb/WidgetCase/blob/master/hawkeye_widget/src/main/java/com/doyou/cv/widget/GradientLine.java)

##### Graphic mixed
[ImgTxtMixtureView](https://github.com/faith-hb/WidgetCase/blob/master/hawkeye_widget/src/main/java/com/doyou/cv/widget/view/ImgTxtMixtureView.java)

##### Anti - WeChat picture viewer, double - click zoom in/out
[MatrixView](https://github.com/faith-hb/WidgetCase/blob/master/hawkeye_widget/src/main/java/com/doyou/cv/widget/touch/MatrixView.java)

##### System control rewrite
1. [FixGridView](https://github.com/faith-hb/WidgetCase/blob/master/hawkeye_widget/src/main/java/com/doyou/cv/widget/sys/FixGridView.java)
2. [FixListView](https://github.com/faith-hb/WidgetCase/blob/master/hawkeye_widget/src/main/java/com/doyou/cv/widget/sys/FixListView.java)
3. [NoScrollViewPager](https://github.com/faith-hb/WidgetCase/blob/master/hawkeye_widget/src/main/java/com/doyou/cv/widget/sys/NoScrollViewPager.java)

##### Learn the bezier curve
[PathMorphBezier](https://github.com/faith-hb/WidgetCase/blob/master/hawkeye_widget/src/main/java/com/doyou/cv/widget/PathMorphBezier.java)

##### Image filter
1. [ColorMatrix](https://github.com/faith-hb/WidgetCase/blob/master/app/src/main/java/com/doyou/cvc/release/colorfilter/ColorMatrixActivity.kt)
2. [ColorHue](https://github.com/faith-hb/WidgetCase/blob/master/app/src/main/java/com/doyou/cvc/release/colorfilter/ColorHueActivity.kt)

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

widgetcase The latest version：[![](https://img.shields.io/badge/widget-1.0.3-green.svg)](http://47.93.38.184:8081/nexus/service/local/repositories/github/content/com/github/widgetcase/1.0.3/widgetcase-1.0.3.aar)

or

aar:
``` groovy
implementation(name: 'hawkeye_widget-release', ext: 'aar')
```

or

Add local dependencies：
``` groovy
implementation project(':hawkeye_widget')
```

#### Proguard
``` groovy
-dontwarn com.dongni.**
-keep class com.dongni.** { *; }

# page modle To avoid confusion
-keep class com.doyou.cv.bean.**{*; }
-keep class com.doyou.cv.WLogger{*; }

-keep class com.doyou.cv.widget.**
-keepclassmembers class com.doyou.cv.widget.** {
    public *;
}

# Utility classes avoid confusion
-keep class com.doyou.cv.utils.**
-keepclassmembers class com.doyou.cv.utils.** {
    public *;
}
```

#### What if there is a problem?
First of all, if there is a problem, you can issue issue. I will often go to see it. If it can be solved, it will be solved in time.

#### Want to join in the maintenance of this open source project, how to operate?
You can fork down the project first and then modify some netizens' problems or the problems you find and the places that need to be optimized by yourself, and then send them to me in the form of pr. I will immediately check the code, test and merge the code.
I will invite him to be the developer of this project. He will be really convenient and quick to maintain this open source library.

#### contact me：
WeChat：faith-hb<br>
QQ：907167515<br>
Email:hongbinghp@gmail.com

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