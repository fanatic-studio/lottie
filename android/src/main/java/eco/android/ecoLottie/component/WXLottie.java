package eco.android.ecoLottie.component;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXVContainer;

import java.util.HashMap;
import java.util.Map;

import app.eco.framework.extend.module.ecoCommon;
import app.eco.framework.extend.module.ecoConstants;
import app.eco.framework.extend.module.ecoIhttp;
import app.eco.framework.extend.module.ecoJson;
import app.eco.framework.extend.module.ecoPage;
import app.eco.framework.extend.module.ecoParse;
import app.eco.framework.extend.module.http.HttpResponseParser;
import eco.android.ecoLottie.R;


public class WXLottie extends WXVContainer<ViewGroup> {

    private View mView;

    private LottieAnimationView mLottie;

    public WXLottie(WXSDKInstance instance, WXVContainer parent, BasicComponentData basicComponentData) {
        super(instance, parent, basicComponentData);
    }

    @Override
    protected ViewGroup initComponentHostView(@NonNull Context context) {
        mView = ((Activity) context).getLayoutInflater().inflate(R.layout.layout_lottie, null);
        initPagerView();
        //
        if (getEvents().contains(ecoConstants.Event.READY)) {
            fireEvent(ecoConstants.Event.READY, null);
        }
        //
        return (ViewGroup) mView;
    }

    @Override
    public void addSubView(View view, int index) {

    }


    @Override
    protected boolean setProperty(String key, Object param) {
        return initProperty(key, param) || super.setProperty(key, param);
    }

    @Override
    public void destroy() {
        if (mLottie == null) {
            return;
        }
        mLottie.cancelAnimation();
        mLottie = null;
        super.destroy();
    }

    private void initPagerView() {
        mLottie = mView.findViewById(R.id.v_lottie);
        setLoop(true);
    }

    private boolean initProperty(String key, Object val) {
        switch (key) {
            case "eco":
                JSONObject json = ecoJson.parseObject(ecoParse.parseStr(val, ""));
                if (json.size() > 0) {
                    for (Map.Entry<String, Object> entry : json.entrySet()) {
                        initProperty(entry.getKey(), entry.getValue());
                    }
                }
                return true;

            case "src":
            case "url":
                setSrc(ecoParse.parseStr(val, ""));
                return true;

            case "loop":
                setLoop(ecoParse.parseBool(val, true));
                return true;

            case "speed":
                setSpeed(ecoParse.parseFloat(val));
                return true;

            case "content":
                setContent(ecoParse.parseStr(val));
                return true;

            default:
                return false;
        }
    }

    /***************************************************************************************************/
    /***************************************************************************************************/
    /***************************************************************************************************/

    /**
     * 设置播放地址
     * @param src
     */
    @JSMethod
    public void setSrc(String src){
        if (mLottie == null) {
            return;
        }
        final Map<String, Object> retData = new HashMap<>();
        String newSrc = ecoPage.rewriteUrl(getContext(), src);
        retData.put("src", newSrc);
        if (getEvents().contains(ecoConstants.Event.STATE_CHANGED)) {
            retData.put("status", "loading");
            fireEvent(ecoConstants.Event.STATE_CHANGED, retData);
        }
        ecoIhttp.get("WXLottie::" + ecoCommon.md5(newSrc), newSrc, null, new ecoIhttp.ResultCallback() {
            @Override
            public void progress(long total, long current, boolean isDownloading) {

            }

            @Override
            public void success(HttpResponseParser resData, boolean isCache) {
                setContent(resData.getBody());
                if (getEvents().contains(ecoConstants.Event.STATE_CHANGED)) {
                    retData.put("status", "success");
                    fireEvent(ecoConstants.Event.STATE_CHANGED, retData);
                }
            }

            @Override
            public void error(String error, int errCode) {
                if (getEvents().contains(ecoConstants.Event.STATE_CHANGED)) {
                    retData.put("status", "error");
                    retData.put("error", error);
                    fireEvent(ecoConstants.Event.STATE_CHANGED, retData);
                }
            }

            @Override
            public void complete() {

            }
        });
    }

    /**
     * 循环播放次数
     * @param loop
     */
    @JSMethod
    public void setLoop(boolean loop) {
        if (mLottie == null) {
            return;
        }
        mLottie.setRepeatCount(loop ? -1 : 0);
    }
    /**
     * 设置进度
     * @param progress
     */
    @JSMethod
    public void setProgress(float progress) {
        if (mLottie == null) {
            return;
        }
        mLottie.setProgress(progress);
    }

    /**
     * 设置帧
     * @param frame
     */
    @JSMethod
    public void setFrameStart(int frame) {
        if (mLottie == null) {
            return;
        }
//        mLottie.setFrame(frame);
        mLottie.setMinAndMaxFrame(frame,100);
    }


    /**
     * 设置帧
     * @param frame
     */
    @JSMethod
    public void setFrameEnd(int frame) {
        if (mLottie == null) {
            return;
        }
//        mLottie.setFrame(frame);
        mLottie.setMinAndMaxFrame(0,frame);
    }

    /**
     * 设置速度
     * @param value
     */
    @JSMethod
    public void setSpeed(float value) {
        if (mLottie == null) {
            return;
        }
        mLottie.setSpeed(value);
    }

    /**
     * 设置播放内容
     * @param content
     */
    @JSMethod
    public void setContent(String content) {
        if (mLottie == null) {
            return;
        }
        mLottie.setAnimationFromJson(content, null);
        mLottie.playAnimation();
    }

    /**
     * 播放
     */
    @JSMethod
    public void play() {
        if (mLottie == null) {
            return;
        }
        mLottie.playAnimation();
    }

    /**
     * 暂停
     */
    @JSMethod
    public void pause() {
        if (mLottie == null) {
            return;
        }
        mLottie.pauseAnimation();
    }

    /**
     * 恢复
     */
    @JSMethod
    public void resume() {
        if (mLottie == null) {
            return;
        }
        mLottie.resumeAnimation();
    }

    /**
     * 停止
     */
    @JSMethod
    public void stop() {
        if (mLottie == null) {
            return;
        }
        mLottie.cancelAnimation();
        mLottie.setProgress(0);
    }
}
