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
}
