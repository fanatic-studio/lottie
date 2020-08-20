package vd.android.vdLottie.entry;

import android.content.Context;

import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.common.WXException;

import app.vd.framework.extend.annotation.ModuleEntry;
import vd.android.vdLottie.component.WXLottie;

@ModuleEntry
public class vdLottieEntry {

    /**
     * APP启动会运行此函数方法
     * @param content Application
     */
    public void init(Context content) {
        try {
            WXSDKEngine.registerComponent("lottie", WXLottie.class);
        } catch (WXException e) {
            e.printStackTrace();
        }
    }
}
