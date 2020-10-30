package eco.android.ecoLottie.entry;

import android.content.Context;

import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.common.WXException;

import app.eco.framework.extend.annotation.ModuleEntry;
import eco.android.ecoLottie.component.WXLottie;

@ModuleEntry
public class ecoLottieEntry {

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
