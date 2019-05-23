package nfnlabs.test.task1;

import android.app.Application;

import nfnlabs.test.task1.db.DBHelper;

public class Task1App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Init db
        DBHelper.getInstance(this);
    }
}
