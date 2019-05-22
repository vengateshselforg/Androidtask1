package nfnlabs.test.task1.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by BalaKrishnan on 12/05/19.
 */
public interface RetrofitService {
    @GET()
    Call<ResponseBody> getResult(@Url String username);

}
