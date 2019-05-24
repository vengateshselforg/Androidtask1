
package nfnlabs.test.task1.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Fields {
    private Integer fieldId;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("name")
    private String mName;
    @SerializedName("url")
    private String mUrl;
    private Integer isFavourited;

    public Fields() {
    }

    public void setFieldId(Integer fieldId) {
        this.fieldId = fieldId;
    }

    public Integer getFieldId() {
        return fieldId;
    }

    public Fields(Integer fieldId) {
        this.fieldId = fieldId;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getName() {
        return mName;
    }

    public void setOwner(String owner) {
        mName = owner;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public Integer getIsFavourited() {
        return isFavourited;
    }

    public void setIsFavourited(Integer isFavourited) {
        this.isFavourited = isFavourited;
    }
}
