package com.example.yang.testbyyang.ui.activity;

import com.sea_monster.exception.BaseException;
import com.sea_monster.network.AbstractHttpRequest;
import com.sea_monster.network.ApiCallback;

/**
 * Created by yang on 2016/2/24.
 */
public abstract class BaseApiActivity extends BaseActivity implements ApiCallback {
    public abstract void onCallApiSuccess(AbstractHttpRequest request, Object obj);
    public abstract void onCallApiFailure(AbstractHttpRequest request, BaseException e);

    /**
     * 网络请求成功回调
     * @param abstractHttpRequest
     * @param o
     */
    @Override
    public void onComplete(final AbstractHttpRequest abstractHttpRequest, final Object o) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onCallApiSuccess(abstractHttpRequest, o);
            }
        });
    }
    /**
     * 网络请求失败回调
     * @param abstractHttpRequest
     * @param e
     */
    @Override
    public void onFailure(final AbstractHttpRequest abstractHttpRequest, final BaseException e) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onCallApiFailure(abstractHttpRequest, e);
            }
        });
    }
}
