package nfnlabs.test.task1.listing;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import nfnlabs.test.task1.model.ImageListResponse;
import nfnlabs.test.task1.network.RetrofitClient;
import nfnlabs.test.task1.network.RetrofitService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by BalaKrishnan on 15/05/19.
 */
public class ListingInteractor implements ListingContractor.ListingInteractor {
    ListingContractor.ListingPresenter listingPresenter;

    public ListingInteractor(ListingContractor.ListingPresenter listingPresenter) {
        this.listingPresenter = listingPresenter;
    }

    private static final String TAG = "ListingInteractor";

    @Override
    public void requestWallpaperList() {
        RetrofitClient.getClient().create(RetrofitService.class).getResult("ImageList").enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Gson gson = new GsonBuilder().create();
                try {
                    String responseText = response.body().string();
                    listingPresenter.handleResponse(gson.fromJson(responseText, ImageListResponse.class));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listingPresenter.apiFailure(t);
            }
        });
    }
}
