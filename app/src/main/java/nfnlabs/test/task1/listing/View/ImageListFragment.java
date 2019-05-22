package nfnlabs.test.task1.listing.View;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import nfnlabs.test.task1.R;
import nfnlabs.test.task1.listing.ListingContractor;
import nfnlabs.test.task1.listing.ListingPresenter;
import nfnlabs.test.task1.model.Record;

public class ImageListFragment extends Fragment implements ListingContractor.ListingView {
    private static final String TAG = "ImageListFragment";

    private static final String TAB_TYPE = "tab_type";

    private String tabType;

    private RecyclerView imageRecyclerView;

    private ImageListAdapter imageListAdapter;
    private ListingContractor.ListingPresenter listingPresenter;

    public ImageListFragment() {
        // Required empty public constructor
    }

    public static ImageListFragment newInstance(String tabType) {
        ImageListFragment fragment = new ImageListFragment();
        Bundle args = new Bundle();
        args.putString(TAB_TYPE, tabType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tabType = getArguments().getString(TAB_TYPE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Create presenter instance
        listingPresenter = new ListingPresenter(this);

        if (tabType != null && !tabType.isEmpty()) {
            if (tabType.equalsIgnoreCase("home")) {
                getImagesForHomeTab();
            } else {
                getImagesForFavouritesTab();
            }
        } else {
            // If condition failed do home tab action
            getImagesForHomeTab();
        }


        setUpUI(view);
        setUpRecyclerView();
    }

    /**
     * This function fetches images from server
     */
    private void getImagesForHomeTab() {
        listingPresenter.requestWallpaperList();
    }

    private void getImagesForFavouritesTab() {

    }

    private void setUpUI(View view) {
        imageRecyclerView = view.findViewById(R.id.ilf_rv_imagelist);
    }

    private void setUpRecyclerView() {
        imageListAdapter = new ImageListAdapter(new ArrayList<>());
        imageRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        imageRecyclerView.setAdapter(imageListAdapter);
    }

    @Override
    public void loadWallpaperListing(List<Record> records) {
        if (imageListAdapter != null) {
            imageListAdapter.setSource(records);
            imageListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setEmptyState() {

    }

    @Override
    public void setErrorState(int errorStateType) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Decouple fragment and presenter to prevent memory leaks
        if (listingPresenter != null) {
            listingPresenter.cleanup();
        }
        listingPresenter = null;
    }
}
