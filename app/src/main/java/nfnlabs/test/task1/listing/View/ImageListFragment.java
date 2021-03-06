package nfnlabs.test.task1.listing.View;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import nfnlabs.test.task1.R;
import nfnlabs.test.task1.detail.ImageDetailActivity;
import nfnlabs.test.task1.listing.ImageListItemClickListener;
import nfnlabs.test.task1.listing.ListingContractor;
import nfnlabs.test.task1.listing.ListingPresenter;
import nfnlabs.test.task1.model.Fields;
import nfnlabs.test.task1.model.Record;
import nfnlabs.test.task1.utils.ObjectConverterHelper;

import static android.view.View.GONE;
import static nfnlabs.test.task1.constants.Constants.FIELDS_STR_KEY;
import static nfnlabs.test.task1.constants.Constants.NO_INTERNET;

public class ImageListFragment extends Fragment implements ListingContractor.ListingView, ImageListItemClickListener {
    private static final String TAG = "ImageListFragment";

    private static final String TAB_TYPE = "tab_type";
    public static final int IMAGE_DETAIL_PAGE = 112;

    private String tabType = "";

    private RecyclerView imageRecyclerView;
    private ProgressBar progressBar;
    private TextView stateText;

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
        setUpUI(view);
        setUpRecyclerView();

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
    }

    /**
     * This function fetches images from server
     */
    private void getImagesForHomeTab() {
        showProgress();
        listingPresenter.requestWallpaperList();
    }

    public void getImagesForFavouritesTab() {
        showProgress();
        listingPresenter.requestFavouriteWallpaperList(requireActivity());
    }

    private void setUpUI(View view) {
        imageRecyclerView = view.findViewById(R.id.ilf_rv_imagelist);
        progressBar = view.findViewById(R.id.ilf_pb_progress);
        stateText = view.findViewById(R.id.ilf_tv_state);
    }

    private void setUpRecyclerView() {
        imageListAdapter = new ImageListAdapter(new ArrayList<>());
        imageListAdapter.setItemClickListener(this);
        imageRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        imageRecyclerView.setAdapter(imageListAdapter);
    }

    @Override
    public void loadWallpaperListing(List<Record> records) {
        hideProgress();
        if (imageListAdapter != null) {
            setNonEmptyStateVisibilities();
            imageListAdapter.setSource(records);
            imageListAdapter.notifyDataSetChanged();
        } else {
            setEmptyOrErrorStateVisibilities();
            if (stateText != null) {
                stateText.setText(requireActivity().getString(R.string.no_items_found));
            }
        }
    }

    @Override
    public void setEmptyState() {
        setEmptyOrErrorStateVisibilities();
        hideProgress();
        if (stateText != null) {
            stateText.setText(requireActivity().getString(R.string.no_items_found));
        }
    }

    @Override
    public void setErrorState(int errorStateType) {
        setEmptyOrErrorStateVisibilities();
        hideProgress();
        if (stateText != null) {
            if (errorStateType == NO_INTERNET) {
                stateText.setText(requireActivity().getString(R.string.no_internet_msg));
            } else {
                stateText.setText(requireActivity().getString(R.string.error_occured));
            }
        }
    }

    @Override
    public void showProgress() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgress() {
        if (progressBar != null) {
            progressBar.setVisibility(GONE);
        }
    }

    private void setNonEmptyStateVisibilities() {
        imageRecyclerView.setVisibility(View.VISIBLE);
        stateText.setVisibility(GONE);
    }

    private void setEmptyOrErrorStateVisibilities() {
        imageRecyclerView.setVisibility(GONE);
        stateText.setVisibility(View.VISIBLE);
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

    @Override
    public void onImageItemClicked(Fields fields) {
        if (fields != null) {
            // Convert object to string to pass in intent
            String fieldsStr = ObjectConverterHelper.getFieldsString(fields);
            Intent intent = new Intent(requireActivity(), ImageDetailActivity.class);
            intent.putExtra(FIELDS_STR_KEY, fieldsStr);
            startActivityForResult(intent, IMAGE_DETAIL_PAGE);
        }
    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK)
            return;
        switch (requestCode) {
            case IMAGE_DETAIL_PAGE:
                refreshFavouritesPage();
                break;
        }
    }

    private void refreshFavouritesPage() {
        if (!tabType.isEmpty() && tabType.equalsIgnoreCase("home")) {
            //getImagesForFavouritesTab();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Unregister event bus
        EventBus.getDefault().unregister(this);
    }*/
}
