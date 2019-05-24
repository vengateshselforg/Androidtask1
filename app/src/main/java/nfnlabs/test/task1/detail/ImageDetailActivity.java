package nfnlabs.test.task1.detail;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.borjabravo.readmoretextview.ReadMoreTextView;

import org.greenrobot.eventbus.EventBus;

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
import nfnlabs.test.task1.model.FavouriteEvent;
import nfnlabs.test.task1.model.Fields;
import nfnlabs.test.task1.utils.AppPermissionUtils;
import nfnlabs.test.task1.utils.FileUtils;
import nfnlabs.test.task1.utils.ImageLoaderUtils;
import nfnlabs.test.task1.utils.NetworkUtils;
import nfnlabs.test.task1.utils.ObjectConverterHelper;

import static nfnlabs.test.task1.constants.Constants.FIELDS_STR_KEY;

public class ImageDetailActivity extends BaseActivity implements View.OnClickListener {

    private ImageView wallpaperImageView;
    private TextView titleText/*,descriptionText*/;
    private ReadMoreTextView descriptionText;
    private FloatingActionButton favoriteFabBtn;
    private FrameLayout flProgress;

    private Fields fields;
    private ImageDownloader imageDownloader;
    private long mLastClickTime;

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
            try {
                descriptionText.setTypeface(ResourcesCompat.getFont(this, R.font.source_sans_pro_light));
            } catch (Exception e) {
                e.printStackTrace();
            }
            descriptionText.setText(fields.getDescription());
        }
        if (fields.getUrl() != null && !fields.getUrl().isEmpty()) {
            ImageLoaderUtils.loadImage(this, wallpaperImageView, fields.getUrl());
        }

        if (fields.getIsFavourited() != null && fields.getIsFavourited() == 1) {
            favoriteFabBtn.setImageResource(R.drawable.ic_favorite_filled);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ida_favourite) {
            onFavoriteBtnClicked();
        }
    }

    private void onFavoriteBtnClicked() {
        favoriteFabBtn.setClickable(false);
        favoriteFabBtn.setEnabled(false);

        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        if (fields.getIsFavourited() != null && fields.getIsFavourited() == 1) {
            boolean isDeleted = DBHelper.getInstance(this)
                    .deleteFromFavourite(fields.getFieldId());
            if (isDeleted) {
                // Delete file from storage to free up space
                FileUtils.deleteFile(new File(fields.getUrl()));
                showToast("Item removed from favourite");
                //setResult(Activity.RESULT_OK);
                EventBus.getDefault().post(new FavouriteEvent());
                finish();
            } else {
                showToast("Item cannot be removed from favourite");
                favoriteFabBtn.setClickable(true);
                favoriteFabBtn.setEnabled(true);
            }
        } else {
            // Check for runtime permissions
            if (NetworkUtils.isNetworkConnected(this)) {
                if (fields != null && fields.getUrl() != null && !fields.getUrl().isEmpty()) {
                    onDownloadImage(this, fields.getUrl());
                } else {
                    showToast("Couldn't favourite item");
                    favoriteFabBtn.setClickable(true);
                    favoriteFabBtn.setEnabled(true);
                }
            } else {
                showToast("No Internet connection");
                favoriteFabBtn.setClickable(true);
                favoriteFabBtn.setEnabled(true);
            }
        }
    }

    private void onDownloadImage(Context context, String url) {
        if (!AppPermissionUtils.checkStoragePermission(context)) {
            if (!isFinishing()) {
                AlertDialog.Builder Builder = new AlertDialog.Builder(this);
                Builder.setCancelable(false);
                Builder.setTitle("Required Storage Permission?");
                Builder.setMessage("We need storage permission to save the downloaded image." +
                        "Please click OK in the following screen");
                Builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int which) {
                        AppPermissionUtils.askStoragePermission(context);
                        favoriteFabBtn.setEnabled(true);
                        favoriteFabBtn.setClickable(true);
                    }
                });
                AlertDialog dialog = Builder.create();
                dialog.show();
            }
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
                if (activityWeakRef.get().fields.getName() != null
                        && !activityWeakRef.get().fields.getName().isEmpty()) {
                    imageFileName = String.format(activityWeakRef.get().fields.getName()
                            + "JPEG_%s.jpg", timeStamp);
                }
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
                if (result) {
                    Toast.makeText(this.activityWeakRef.get(), "Item added to Favourites", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this.activityWeakRef.get(), "Item not added to Favourites", Toast.LENGTH_SHORT).show();
                    if (this.activityWeakRef.get().favoriteFabBtn != null) {
                        this.activityWeakRef.get().favoriteFabBtn.setClickable(true);
                        this.activityWeakRef.get().favoriteFabBtn.setEnabled(true);
                    }
                }
                //this.activityWeakRef.get().setResult(Activity.RESULT_OK);
                EventBus.getDefault().post(new FavouriteEvent());
                this.activityWeakRef.get().finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        cancelFileDownload();
    }

    @Override
    public void onBackPressed() {

        if (!isFinishing() && imageDownloader != null) {
            // If user presses back button
            AlertDialog.Builder Builder = new AlertDialog.Builder(this);
            Builder.setCancelable(false);
            Builder.setTitle("Exit?");
            Builder.setMessage("If you exit the page your download will be cancelled and item won't be" +
                    "added to favourites!");
            Builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                public void onClick(DialogInterface dialog, int which) {
                    cancelFileDownload();
                    dialog.dismiss();
                    finish();
                }
            });
            Builder.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = Builder.create();
            dialog.show();
        } else {
            super.onBackPressed();
        }
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

    private void cancelFileDownload() {
        if (imageDownloader != null) {
            imageDownloader.cancel(true);
        }
    }
}
