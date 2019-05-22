package nfnlabs.test.task1.utils;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import static nfnlabs.test.task1.constants.Constants.NO_INTERNET;
import static nfnlabs.test.task1.constants.Constants.UNKNOWN_ERROR;

public class ErrorUtils {
    public static int getError(Throwable throwable) {
        if (throwable instanceof UnknownHostException
                || throwable instanceof SocketTimeoutException) {
            return NO_INTERNET;
        } else {
            return UNKNOWN_ERROR;
        }
    }
}
