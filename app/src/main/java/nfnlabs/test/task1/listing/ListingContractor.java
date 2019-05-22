package nfnlabs.test.task1.listing;

import java.util.List;

import nfnlabs.test.task1.base.BasePresenter;
import nfnlabs.test.task1.model.ImageListResponse;
import nfnlabs.test.task1.model.Record;

/**
 * Created by BalaKrishnan on 15/05/19.
 */
public interface ListingContractor {
    interface ListingView {
        void loadWallpaperListing(List<Record> records);

        void setEmptyState();

        void setErrorState(int errorStateType);

        void showProgress();

        void hideProgress();
    }

    interface ListingPresenter extends BasePresenter {
        void requestWallpaperList();

        void handleResponse(ImageListResponse imageListResponse);

        void apiFailure(Throwable throwable);
    }

    interface ListingInteractor {
        void requestWallpaperList();
    }

}
