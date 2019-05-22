package nfnlabs.test.task1.listing.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import nfnlabs.test.task1.R;
import nfnlabs.test.task1.listing.ListingContractor;
import nfnlabs.test.task1.listing.ListingPresenter;
import nfnlabs.test.task1.model.Record;

public class ListActivity extends AppCompatActivity implements ListingContractor.ListingView {
    RecyclerView imageRecyclerView;
    ImageListAdapter imageListAdapter;
    private static final String TAG = "ListActivity";
    ListingContractor.ListingPresenter listingPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listingPresenter = new ListingPresenter(this);
        setUpUi();
        setUpRecyclerView();
        listingPresenter.requestWallpaperList();
    }
    /**
     * Initialize all UI components here
     */
    private void setUpUi() {
        imageRecyclerView = findViewById(R.id.ma_rv_imagelist);
    }

    /**
     * RecyclerView setup goes here
     */
    private void setUpRecyclerView() {
        imageListAdapter = new ImageListAdapter(new ArrayList<>());
        imageRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        imageRecyclerView.setAdapter(imageListAdapter);
    }

    @Override
    public void loadWallpaperListing(List<Record> records) {
        imageListAdapter.source = records;
        imageListAdapter.notifyDataSetChanged();
    }

    @Override
    public void setEmptyState() {
        // TODO Show Empty state
        Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setErrorState(int errorStateType) {
        // TODO Show Error state
        Toast.makeText(this, "Please try again later", Toast.LENGTH_SHORT).show();
    }
}
