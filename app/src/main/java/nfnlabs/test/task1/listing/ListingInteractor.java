package nfnlabs.test.task1.listing;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nfnlabs.test.task1.db.DBHelper;
import nfnlabs.test.task1.model.Fields;
import nfnlabs.test.task1.model.ImageListResponse;
import nfnlabs.test.task1.model.Record;
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

    @Override
    public void requestFavouriteWallpaperList(Context context) {
        List<Fields> fields = DBHelper.getInstance(context)
                .getFavouritedFields();
        if (fields != null && !fields.isEmpty()) {
            List<Record> records = new ArrayList<>();
            for (Fields f : fields) {
                Record r = new Record();
                r.setFields(f);
                records.add(r);
            }
            ImageListResponse response = new ImageListResponse();
            response.setRecords(records);
            listingPresenter.handleResponse(response);
        } else {
            ImageListResponse response = new ImageListResponse();
            response.setRecords(new ArrayList<>());
            listingPresenter.handleResponse(response);
        }
    }
}
