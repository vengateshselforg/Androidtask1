
package nfnlabs.test.task1.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Record {

    @SerializedName("createdTime")
    private String mCreatedTime;
    @SerializedName("fields")
    private Fields mFields;
    @SerializedName("id")
    private String mId;

    public String getCreatedTime() {
        return mCreatedTime;
    }

    public void setCreatedTime(String createdTime) {
        mCreatedTime = createdTime;
    }

    public Fields getFields() {
        return mFields;
    }

    public void setFields(Fields fields) {
        mFields = fields;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

}
