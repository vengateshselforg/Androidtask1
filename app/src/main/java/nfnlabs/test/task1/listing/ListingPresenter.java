package nfnlabs.test.task1.listing;

import nfnlabs.test.task1.model.ImageListResponse;
import nfnlabs.test.task1.utils.ErrorUtils;

import static nfnlabs.test.task1.constants.Constants.NO_INTERNET;

/**
 * Created by BalaKrishnan on 15/05/19.
 */
public class ListingPresenter implements ListingContractor.ListingPresenter {
    private ListingContractor.ListingView listingView;
    private ListingContractor.ListingInteractor listingInteractor;

    public ListingPresenter(ListingContractor.ListingView listingView) {
        this.listingView = listingView;
        listingInteractor = new ListingInteractor(this);
    }

    @Override
    public void requestWallpaperList() {
        listingInteractor.requestWallpaperList();
    }

    @Override
    public void handleResponse(ImageListResponse imageListResponse) {
        if (imageListResponse.getRecords().size() > 0)
            listingView.loadWallpaperListing(imageListResponse.getRecords());
        else
            listingView.setEmptyState();
//        listingView.setEmptyState();
    }

    @Override
    public void apiFailure(Throwable throwable) {
        int error = ErrorUtils.getError(throwable);
        listingView.setErrorState(error);
    }

    @Override
    public void cleanup() {
        // Decouple presenter from view
        this.listingView = null;
    }
}
