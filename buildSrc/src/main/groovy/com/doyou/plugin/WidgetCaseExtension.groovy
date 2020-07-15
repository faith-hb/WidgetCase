package com.doyou.plugin

/**
 * 插件实际功能逻辑类
 * @autor hongbing
 * @date 2019-10-11
 */
class WidgetCaseExtension {

    void widgetCase() {
        println("hello widget case.")
    }

    // 签名配置信息
    String keyAlias = 'widgetcase'
    String signPwd = '2019888'
    String keyStoreFileDir = '../widgetcase'

}
