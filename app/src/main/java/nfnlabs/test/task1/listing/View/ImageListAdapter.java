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
import nfnlabs.test.task1.model.Record;

/**
 * Created by BalaKrishnan on 12/05/19.
 */
public class ImageListAdapter extends RecyclerView.Adapter<ImageListViewHolder> {

    List<Record> source;
    private static final String TAG = "ImageListAdapter";

    public ImageListAdapter(List<Record> source) {
        this.source = source;
    }

    @NonNull
    @Override
    public ImageListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ImageListViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_image, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImageListViewHolder imageListViewHolder, int i) {
        Glide.with(imageListViewHolder.itemImageView.getContext())
                .load(source.get(i).getFields().getUrl())
                .into(imageListViewHolder.itemImageView);
        imageListViewHolder.itemName.setText(source.get(i).getFields().getName());
    }

    @Override
    public int getItemCount() {
        return source.size();
    }

}

class ImageListViewHolder extends RecyclerView.ViewHolder {
    ImageView itemImageView;
    TextView itemName;

    public ImageListViewHolder(@NonNull View itemView) {
        super(itemView);
        itemImageView = itemView.findViewById(R.id.ii_iv_image);
        itemName = itemView.findViewById(R.id.ii_tv_name);
    }
}
