package com.example.yang.testbyyang.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.yang.testbyyang.DemoContext;
import com.example.yang.testbyyang.R;
import com.example.yang.testbyyang.models.ApiResult;
import com.example.yang.testbyyang.models.Groups;
import com.example.yang.testbyyang.models.Status;
import com.example.yang.testbyyang.ui.activity.GroupDetailActivity;
import com.example.yang.testbyyang.ui.adapter.GroupListAdapter;
import com.example.yang.testbyyang.ui.widget.LoadingDialog;
import com.example.yang.testbyyang.ui.widget.WinToast;
import com.example.yang.testbyyang.utils.Constants;
import com.sea_monster.exception.BaseException;
import com.sea_monster.network.AbstractHttpRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;

/**
 * Created by yang on 2016/3/1.
 */
public class GroupListFragment extends BaseFragment implements AdapterView.OnItemClickListener{
    private String TAG = GroupListFragment.class.getSimpleName();
    private int RESULTCODE = 100;
    private ListView mGroupListView;
    private GroupListAdapter mGroupListAdapter;
    private List<ApiResult> mResultList;
    private AbstractHttpRequest<Groups> mGetAllGroupsRequest;
    private AbstractHttpRequest<Status> mUserRequest;
    private HashMap<String, Group> mGroupMap;
    private ApiResult result;
    private Handler mHandler;
    private LoadingDialog mDialog;
    public static final String GroupListData = "GroupListData";
    public static String maxGroupList = "1000";

    Bundle mBundle;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.de_fr_group_list, container, false);

        mGroupListView = (ListView) view.findViewById(R.id.de_group_list);
        mGroupListView.setItemsCanFocus(false);
        mGroupListView.setOnItemClickListener(this);

        mDialog = new LoadingDialog(getActivity());
        mResultList = new ArrayList<ApiResult>();
        mHandler = new Handler();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        savedInstanceState = mBundle;
        if (savedInstanceState != null) {
            mResultList = savedInstanceState.getParcelableArrayList(GroupListData);
            mGroupListAdapter = new GroupListAdapter(getActivity(), mResultList, mGroupMap);
            mGroupListView.setAdapter(mGroupListAdapter);
            mGroupListAdapter.setOnItemButtonClick(onItemButtonClick);
            mGroupListAdapter.notifyDataSetChanged();
            return;
        }

        if (DemoContext.getInstance() == null)
            return;

        mGroupMap = DemoContext.getInstance().getGroupMap();
        mGetAllGroupsRequest = DemoContext.getInstance().getDemoApi().getAllGroups(this);
    }

    @Override
    public void onCallApiSuccess(AbstractHttpRequest request, Object obj) {
        if (mGetAllGroupsRequest != null && mGetAllGroupsRequest.equals(request)) {

            if (obj instanceof Groups) {

                final Groups groups = (Groups) obj;
                if (groups.getCode() == 200) {
                    for (int i = 0; i < groups.getResult().size(); i++) {
                        maxGroupList = groups.getResult().get(0).getMax_number();
                        mResultList.add(groups.getResult().get(i));
                    }

                    mGroupListAdapter = new GroupListAdapter(getActivity(), mResultList, mGroupMap);
                    mGroupListView.setAdapter(mGroupListAdapter);
                    mGroupListAdapter.setOnItemButtonClick(onItemButtonClick);
                } else {
                    WinToast.toast(getActivity(), groups.getCode());
                }
            }
        } else if (mUserRequest != null && mUserRequest.equals(request)) {
            WinToast.toast(getActivity(), R.string.group_join_success);

            if (result != null) {

                setGroupMap(result, 1);

                refreshAdapter();

                RongIM.getInstance().getRongIMClient().joinGroup(result.getId(), result.getName(), new RongIMClient.OperationCallback() {
                    @Override
                    public void onSuccess() {
                        if (mDialog != null)
                            mDialog.dismiss();
                        RongIM.getInstance().startGroupChat(getActivity(), result.getId(), result.getName());
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                });
            }
        }
    }

    GroupListAdapter.OnItemButtonClick onItemButtonClick = new GroupListAdapter.OnItemButtonClick() {
        @Override
        public boolean onButtonClick(int position, View view) {
            if (mGroupListAdapter == null)
                return false;

            result = mGroupListAdapter.getItem(position);

            if (result == null)
                return false;

            if (mGroupMap == null)
                return false;

            if (mGroupMap.containsKey(result.getId())) {
                RongIM.getInstance().startGroupChat(getActivity(), result.getId(), result.getName());
            } else {

                if (DemoContext.getInstance() != null) {

                    if (result.getNumber().equals(maxGroupList)) {
                        WinToast.toast(getActivity(), "群组人数已满");
                        return false;
                    }

                    if (mDialog != null && !mDialog.isShowing())
                        mDialog.show();

                    mUserRequest = DemoContext.getInstance().getDemoApi().joinGroup(result.getId(), GroupListFragment.this);
                }

            }
            return true;
        }
    };

    @Override
    public void onCallApiFailure(AbstractHttpRequest request, BaseException e) {
        if (mUserRequest != null && mUserRequest.equals(request)) {
            if (mDialog != null)
                mDialog.dismiss();
        } else if (mGetAllGroupsRequest != null && mGetAllGroupsRequest.equals(request)) {
            Log.e(TAG, "---获取群组列表失败 ----");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mResultList != null && position != -1 && position < mResultList.size()) {

            Uri uri = Uri.parse("demo://" + getActivity().getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversationSetting")
                    .appendPath(String.valueOf(Conversation.ConversationType.GROUP))
                    .appendQueryParameter("targetId", mResultList.get(position).getId()).build();

            Intent intent = new Intent(getActivity(), GroupDetailActivity.class);
            intent.putExtra("INTENT_GROUP", (Parcelable) mResultList.get(position));

            intent.setData(uri);
            startActivityForResult(intent, RESULTCODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case Constants.GROUP_JOIN_REQUESTCODE:
            case Constants.GROUP_QUIT_REQUESTCODE:
                refreshAdapter();
                break;
        }
    }

    /**
     * 设置群组信息提供者
     *
     * @param result
     * @param i      0,退出；1 加入
     */
    public static void setGroupMap(ApiResult result, int i) {

        if (DemoContext.getInstance() != null && result != null) {
            HashMap<String, Group> groupHashMap = DemoContext.getInstance().getGroupMap();

            if (result.getId() == null)
                return;

            if (i == 1) {
                if (result.getPortrait() != null)
                    groupHashMap.put(result.getId(), new Group(result.getId(), result.getName(), Uri.parse(result.getPortrait())));
                else
                    groupHashMap.put(result.getId(), new Group(result.getId(), result.getName(), null));
            } else if (i == 0) {
                groupHashMap.remove(result.getId());
            }
            DemoContext.getInstance().setGroupMap(groupHashMap);

        }
    }

    private void refreshAdapter() {

        if (mGroupListAdapter == null) {
            mGroupListAdapter = new GroupListAdapter(getActivity(), mResultList, mGroupMap);
            mGroupListView.setAdapter(mGroupListAdapter);

        } else {
            mGroupListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(GroupListData, (ArrayList<? extends Parcelable>) mResultList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBundle = new Bundle();
        mBundle.putParcelableArrayList(GroupListData, (ArrayList<? extends Parcelable>) mResultList);
    }

    @Override
    public void onDestroy() {
        if (mGroupListAdapter != null) {
            mGroupListAdapter = null;
        }
        super.onDestroy();
    }
}
