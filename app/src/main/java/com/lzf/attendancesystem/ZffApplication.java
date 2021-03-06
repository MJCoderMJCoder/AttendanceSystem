package com.lzf.attendancesystem;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.FaceEngine;
import com.lzf.attendancesystem.bean.Admin;
import com.lzf.attendancesystem.bean.DaoMaster;
import com.lzf.attendancesystem.bean.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * Created by MJCoder on 2019-03-14.
 */

public class ZffApplication extends Application {

    private volatile static FaceEngine faceEngine;
    private volatile static DaoSession daoSession;

    /**
     * 双检锁/双重校验锁（DCL，即 double-checked locking）
     * 是否 Lazy 初始化：是
     * 是否多线程安全：是
     * 描述：这种方式采用双锁机制，安全且在多线程情况下能保持高性能。
     *
     * @return FaceEngine
     */
    public static FaceEngine getFaceEngine() {
        if (faceEngine == null) {
            synchronized (FaceEngine.class) {
                if (faceEngine == null) {
                    faceEngine = new FaceEngine();
                }
            }
        }
        return faceEngine;
    }

    public static DaoSession getDaoSession(Context context) {
        try {
            if (daoSession == null) {
                synchronized (DaoSession.class) {
                    if (daoSession == null) {
                        // do this once, for example in your Application class
                        //// note: DevOpenHelper is for dev only, use a OpenHelper subclass instead
                        DaoMaster.OpenHelper helper = new DaoMaster.OpenHelper(context, "ATTENDANCE_SYSTEM_DATABASE.db", null) {
                            @Override
                            public void onCreate(Database db) {
                                //                                Log.v("Database", "Creating tables for schema version " + DaoMaster.SCHEMA_VERSION);
                                super.onCreate(db);
                            }

                            @Override
                            public void onCreate(SQLiteDatabase db) {
                                //                                Log.v("SQLiteDatabase", "Creating tables for schema version " + DaoMaster.SCHEMA_VERSION);
                                super.onCreate(db);
                            }

                            @Override
                            public void onUpgrade(Database db, int oldVersion, int newVersion) {
                                //                                Log.v("Database", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
                                super.onUpgrade(db, oldVersion, newVersion);
                            }

                            @Override
                            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                                //                                Log.v("SQLiteDatabase", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
                                super.onUpgrade(db, oldVersion, newVersion);
                            }
                        };
                        daoSession = new DaoMaster(helper.getWritableDatabase()).newSession();
                        daoSession.getAdminDao().insert(new Admin(1, "admin", "root", "纸纷飞", "男", "598157378@qq.com", "18334706003"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return daoSession;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        getDaoSession(this);
    }
}
