# WidgetCase

<!--https://shields.io/-->
<!--[![auc][aucsvg]][auc] [![api][apisvg]][api] [![License][licensesvg]][license]-->

[![](https://img.shields.io/badge/widget-1.7.8-green.svg)](https://github.com/faith-hb/WidgetCase/) [![](https://img.shields.io/badge/platform-android-blue.svg)](https://github.com/faith-hb/WidgetCase)



#### 项目介绍
跟着公司项目走了一年之久的自定义控件Library，由于公司项目偏向统计类，所以项目里控件更多的是这方面的控件，但既然把库都抽出来了，就绝不可能只是狭隘到这么一丢丢。因此此库会一直维护下去，直至Android倒下，本人倒下(决心表的很大)。当然一个人的力量是有限的，期待更多的志同道合的朋友加入，大家在一起干点轰轰烈烈的事情。


#### 效果刷一波，毕竟是靠脸吃饭的年代
<!--![功能目录](screenshots/main.jpg){:height="960px" width="540px"}-->

<!--![功能目录](screenshots/main.jpg = 100*)-->

##### 功能目录>>>
<img src="screenshots/main.jpg" width="360" height="640"/>

##### 峰值图>>>
![image](https://github.com/faith-hb/WidgetCase/blob/master/screenshots/taperchart.gif)




#### 自定义控件分布如下

##### 峰值图
1. TaperChart
2. TaperChartLayout
3. HorTaperChart(还有问题)

##### 圆形进度条
1. CircleProgress
2. GradientProBar
3. CircleProgressBarView
4. CircleView

##### 环形进度条
1. RingView
2. LegendRingView(内含图例)

##### 半圆/油表
1. HalfCircleProView

##### 横向进度条
1. HorProBar
2. SectionProBar
3. ShadowProBar
4. LoadingLineView

##### 渐变曲线
GradientLine


##### 系统控件重写
1. FixGridView
2. FixListView
3. NoScrollViewPager

##### 贝塞尔曲线练练手
PathMorphBezier


#### Download
Gradle:
``` groovy
implementation 'com.faithhb:toolslib:1.7.8'
```

or

aar:
``` groovy
implementation(name: 'hawkeye_widget-release', ext: 'aar')
```

#### 参与贡献

1. Fork 本项目
2. 新建 Feat_xxx 分支
3. 提交代码
4. 新建 Pull Request