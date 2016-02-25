package com.example.yang.testbyyang.database;

import android.content.Context;

/**
 * Created by yang on 2016/2/26.
 */
public class DBHelper {

    private static DBHelper instance;
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    private DBHelper(Context context){
        if (daoSession == null) {
            if (daoMaster == null) {
                DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(context, context.getPackageName(), null);
                daoMaster = new DaoMaster(helper.getWritableDatabase());
            }
            daoSession = daoMaster.newSession();
        }
    }

    public static DBHelper getInstance(Context context){
        if(instance == null){
            synchronized (DBHelper.class){
                if(instance == null){
                    instance = new DBHelper(context);
                }
            }

        }
        return instance;
    }

    public DaoMaster getDaoMaster() {
        return daoMaster;
    }

    public void setDaoMaster(DaoMaster daoMaster) {
        this.daoMaster = daoMaster;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public void setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
    }

}
