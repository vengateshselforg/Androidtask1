package nfnlabs.test.task1.listing.View;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import nfnlabs.test.task1.R;
import nfnlabs.test.task1.listing.ImageListItemClickListener;
import nfnlabs.test.task1.model.Fields;
import nfnlabs.test.task1.model.Record;

/**
 * Created by BalaKrishnan on 12/05/19.
 */
public class ImageListAdapter extends RecyclerView.Adapter<ImageListViewHolder> {
    private static final String TAG = "ImageListAdapter";

    List<Record> source;
    private ImageListItemClickListener itemClickListener;

    public ImageListAdapter(List<Record> source) {
        this.source = source;
    }

    public void setSource(List<Record> source) {
        this.source = source;
    }

    public void setItemClickListener(ImageListItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ImageListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ImageListViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_image, viewGroup, false), itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageListViewHolder imageListViewHolder, int i) {
        imageListViewHolder.setImageItem(source.get(i).getFields());
    }

    @Override
    public int getItemCount() {
        return source.size();
    }

}

class ImageListViewHolder extends RecyclerView.ViewHolder {
    private ImageView itemImageView;
    private TextView itemName;

    public Fields fields;

    public ImageListViewHolder(@NonNull View itemView, ImageListItemClickListener itemClickListener) {
        super(itemView);
        itemImageView = itemView.findViewById(R.id.ii_iv_image);
        itemName = itemView.findViewById(R.id.ii_tv_name);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onImageItemClicked(fields);
                }
            }
        });
    }

    public void setImageItem(Fields fields) {
        this.fields = fields;

        Glide.with(itemImageView.getContext())
                .load(fields.getUrl())
                .into(itemImageView);
        itemName.setText(fields.getName());
    }
}