package nfnlabs.test.task1.detail;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import nfnlabs.test.task1.R;
import nfnlabs.test.task1.base.BaseActivity;
import nfnlabs.test.task1.model.Fields;
import nfnlabs.test.task1.utils.ImageLoaderUtils;
import nfnlabs.test.task1.utils.ObjectConverterHelper;

import static nfnlabs.test.task1.constants.Constants.FIELDS_STR_KEY;

public class ImageDetailActivity extends BaseActivity {

    private ImageView wallpaperImageView;
    private TextView titleText, descriptionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        Intent intent = getIntent();
        if (intent != null) {
            setUpUI();

            String fieldsStr = intent.getStringExtra(FIELDS_STR_KEY);
            if (fieldsStr != null && !fieldsStr.isEmpty()) {
                Fields fields = ObjectConverterHelper.getFields(fieldsStr);
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
}
