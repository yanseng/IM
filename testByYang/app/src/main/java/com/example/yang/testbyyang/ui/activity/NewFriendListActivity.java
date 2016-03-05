package com.example.yang.testbyyang.ui.activity;

import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import com.example.yang.testbyyang.models.ApiResult;
import com.example.yang.testbyyang.models.Friends;
import com.example.yang.testbyyang.models.Status;
import com.example.yang.testbyyang.ui.widget.LoadingDialog;
import com.sea_monster.exception.BaseException;
import com.sea_monster.network.AbstractHttpRequest;

import java.util.List;

/**
 * Created by yang on 2016/3/1.
 */
public class NewFriendListActivity extends BaseApiActivity implements Handler.Callback{

    private static final String TAG = NewFriendListActivity.class.getSimpleName();
    private AbstractHttpRequest<Friends> getFriendHttpRequest;
    private AbstractHttpRequest<Status> mRequestFriendHttpRequest;

    private ListView mNewFriendList;
    //private NewFriendListAdapter adapter;
    private List<ApiResult> mResultList;
    private LoadingDialog mDialog;
    private Handler mHandler;

    @Override
    public void onCallApiSuccess(AbstractHttpRequest request, Object obj) {

    }

    @Override
    public void onCallApiFailure(AbstractHttpRequest request, BaseException e) {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
