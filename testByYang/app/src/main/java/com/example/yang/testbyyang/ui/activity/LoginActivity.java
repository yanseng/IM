package com.example.yang.testbyyang.ui.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.yang.testbyyang.R;
import com.example.yang.testbyyang.models.Friends;
import com.example.yang.testbyyang.models.Groups;
import com.example.yang.testbyyang.models.User;
import com.example.yang.testbyyang.ui.widget.LoadingDialog;
import com.sea_monster.exception.BaseException;
import com.sea_monster.network.AbstractHttpRequest;

public class LoginActivity extends BaseApiActivity implements View.OnClickListener, Handler.Callback{
    private static final String TAG = LoginActivity.class.getSimpleName();
    private int REQUEST_CODE_REGISTER = 200;
    public static final String INTENT_IMAIL = "intent_email";
    public static final String INTENT_PASSWORD = "intent_password";
    private int HANDLER_LOGIN_SUCCESS = 1;
    private int HANDLER_LOGIN_FAILURE = 2;
    private int HANDLER_LOGIN_HAS_FOCUS = 3;
    private int HANDLER_LOGIN_HAS_NO_FOCUS = 4;

    private AbstractHttpRequest<User> loginHttpRequest;
    private AbstractHttpRequest<Friends> getUserInfoHttpRequest;
    private AbstractHttpRequest<Groups> mGetMyGroupsRequest;
    private LoadingDialog mDialog;
    private Handler mHandler;

    EditTextHolder mEditUserNameEt;
    EditTextHolder mEditPassWordEt;

    List<UserInfos>
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

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

    @Override
    public void onClick(View v) {

    }
}
