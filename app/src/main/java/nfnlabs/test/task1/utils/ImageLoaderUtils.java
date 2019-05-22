package nfnlabs.test.task1.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import nfnlabs.test.task1.R;

public class ImageLoaderUtils {
    // Utils class created because if we want to change the
    // library in future we can edit only this file

    public static void loadImage(Context context, ImageView imageView, String url) {
        if (url != null && !url.isEmpty()) {
            Glide.with(context)
                    .load(url)
                    .into(imageView);
        } else {
            Glide.with(context)
                    .load(R.drawable.image_loding_placeholder)
                    .into(imageView);
        }
    }
}
