package nfnlabs.test.task1.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ObjectConverter {
    public static Gson newInstance() {
        GsonBuilder builder = new GsonBuilder();
        return builder.create();
    }
}
