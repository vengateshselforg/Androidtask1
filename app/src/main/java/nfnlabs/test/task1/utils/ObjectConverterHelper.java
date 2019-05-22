package nfnlabs.test.task1.utils;

import com.google.gson.Gson;

import nfnlabs.test.task1.model.Fields;

public class ObjectConverterHelper {
    private static Gson gson = ObjectConverter.newInstance();

    public static String getFieldsString(Fields fields) {
        return gson.toJson(fields);
    }

    public static Fields getFields(String str) {
        return gson.fromJson(str, Fields.class);
    }
}
