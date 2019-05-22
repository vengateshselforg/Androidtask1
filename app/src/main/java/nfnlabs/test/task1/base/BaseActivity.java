package nfnlabs.test.task1.base;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public abstract class BaseActivity extends AppCompatActivity {

    protected void showToast(String msgStr) {
        Toast.makeText(this, msgStr, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(int msgStrRes) {
        Toast.makeText(this, msgStrRes, Toast.LENGTH_SHORT).show();
    }
}
