package com.androidstudy.alfoneshub.utils;

import android.app.Application;

//import com.androidstudy.alfoneshub.models.DaoMaster;
//import com.androidstudy.alfoneshub.models.DaoSession;

//import org.greenrobot.greendao.database.Database;

/**
 * Created by juma on 18/01/17.
 */

public class AlfonesCommunication extends Application {

    //private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        //DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "alfones-db");
        //Database db = helper.getWritableDb();
        //daoSession = new DaoMaster(db).newSession();

    }

    //public DaoSession getDaoSession() {
      //  return daoSession;
    //}

}
