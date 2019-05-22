
package nfnlabs.test.task1.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Fields {

    @SerializedName("description")
    private String mDescription;
    @SerializedName("name")
    private String mName;
    @SerializedName("url")
    private String mUrl;

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

}
