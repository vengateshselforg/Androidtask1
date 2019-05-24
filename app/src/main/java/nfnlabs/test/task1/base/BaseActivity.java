package nfnlabs.test.task1.base;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.List;

public abstract class BaseActivity extends AppCompatActivity {

    protected void showToast(String msgStr) {
        Toast.makeText(this, msgStr, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(int msgStrRes) {
        Toast.makeText(this, msgStrRes, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (!fragments.isEmpty()) {
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                if (fragment != null) fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}
