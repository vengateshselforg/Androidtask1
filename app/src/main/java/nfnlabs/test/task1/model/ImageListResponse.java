
package nfnlabs.test.task1.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class ImageListResponse {

    @SerializedName("records")
    private List<Record> mRecords;

    public List<Record> getRecords() {
        return mRecords;
    }

    public void setRecords(List<Record> records) {
        mRecords = records;
    }

}
