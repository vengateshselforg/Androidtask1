package nfnlabs.test.task1.detail;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import nfnlabs.test.task1.R;
import nfnlabs.test.task1.base.BaseActivity;
import nfnlabs.test.task1.db.DBHelper;
import nfnlabs.test.task1.model.Fields;
import nfnlabs.test.task1.utils.AppPermissionUtils;
import nfnlabs.test.task1.utils.ImageLoaderUtils;
import nfnlabs.test.task1.utils.NetworkUtils;
import nfnlabs.test.task1.utils.ObjectConverterHelper;

import static nfnlabs.test.task1.constants.Constants.FIELDS_STR_KEY;

public class ImageDetailActivity extends BaseActivity implements View.OnClickListener {

    private ImageView wallpaperImageView;
    private TextView titleText, descriptionText;
    private FloatingActionButton favoriteFabBtn;
    private FrameLayout flProgress;

    private Fields fields;
    private ImageDownloader imageDownloader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        Intent intent = getIntent();
        if (intent != null) {
            setUpUI();

            String fieldsStr = intent.getStringExtra(FIELDS_STR_KEY);
            if (fieldsStr != null && !fieldsStr.isEmpty()) {
                fields = ObjectConverterHelper.getFields(fieldsStr);
                if (fields != null) {
                    setImageDetail(fields);
                } else {
                    showToast(R.string.err_showing_details);
                }
            } else {
                showToast(R.string.err_showing_details);
            }
        } else {
            showToast(R.string.err_showing_details);
        }
    }

    private void setUpUI() {
        wallpaperImageView = findViewById(R.id.ida_iv_image);
        titleText = findViewById(R.id.ida_tv_title);
        descriptionText = findViewById(R.id.ida_tv_description);
        favoriteFabBtn = findViewById(R.id.ida_favourite);
        flProgress = findViewById(R.id.flProgress);

        favoriteFabBtn.setOnClickListener(this);
    }

    private void setImageDetail(Fields fields) {
        if (fields.getName() != null && !fields.getName().isEmpty()) {
            titleText.setText(fields.getName());
        }
        if (fields.getDescription() != null && !fields.getDescription().isEmpty()) {
            descriptionText.setText(fields.getDescription());
        }
        if (fields.getUrl() != null && !fields.getUrl().isEmpty()) {
            ImageLoaderUtils.loadImage(this, wallpaperImageView, fields.getUrl());
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ida_favourite) {
            onFavoriteBtnClicked();
        }
    }

    private void onFavoriteBtnClicked() {
        // Check for runtime permissions

        if (NetworkUtils.isNetworkConnected(this)) {
            if (fields != null && fields.getUrl() != null && !fields.getUrl().isEmpty()) {
                onDownloadImage(this, fields.getUrl());
            } else {

            }
        } else {

        }
    }

    private void onDownloadImage(Context context, String url) {
        if (!AppPermissionUtils.checkStoragePermission(context)) {
            AppPermissionUtils.askStoragePermission(context);
        } else {
            imageDownloader = new ImageDownloader(this);
            imageDownloader.execute(url);
        }
    }

    public static class ImageDownloader extends AsyncTask<String, Integer, Boolean> {

        private WeakReference<ImageDetailActivity> activityWeakRef;

        public ImageDownloader(ImageDetailActivity activity) {
            this.activityWeakRef = new WeakReference<>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (this.activityWeakRef.get() != null) {
                this.activityWeakRef.get().showProgress();
            }
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            int count;
            boolean isFavourited = false;
            try {
                URL mUrl = new URL(urls[0]);
                URLConnection urlConnection = mUrl.openConnection();
                urlConnection.connect();
                int lengthOfFile = urlConnection.getContentLength();

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                String imageFileName = String.format("JPEG_%s.jpg", timeStamp);
                String folder = Environment.getExternalStorageDirectory() +
                        File.separator + "Task1";
                File directory = new File(folder);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                InputStream input = new BufferedInputStream(mUrl.openStream());
                OutputStream output = new FileOutputStream(folder + "/" + imageFileName);
                byte[] data = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress((int) (total * 100 / lengthOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();

                isFavourited = DBHelper.getInstance(activityWeakRef.get())
                        .addFieldsToFavourite(activityWeakRef.get().fields, folder + "/" + imageFileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return isFavourited;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (this.activityWeakRef.get() != null) {
                this.activityWeakRef.get().hideProgress();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (imageDownloader != null) {
            imageDownloader.cancel(true);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void showProgress() {
        if (flProgress != null) {
            flProgress.setVisibility(View.VISIBLE);
        }
    }

    public void hideProgress() {
        if (flProgress != null) {
            flProgress.setVisibility(View.GONE);
        }
    }
}
