package demo.pratiked.twitterhash.util;

import android.util.Base64;

import java.nio.charset.StandardCharsets;

public class Config {

    //TODO: get the secrets
    //TODO: encode secrets RFC 1738
    private static final String CONSUMER_KEY = "<-add consumer key->";
    private static final String CONSUMER_SECRET = "<-add consumer secret->";

    private static String getToken(){
        return CONSUMER_KEY + ":" + CONSUMER_SECRET;
    }

    private static String getEncodedToken(){
        byte[] data = getToken().getBytes(StandardCharsets.UTF_8);
        return "Basic " + Base64.encodeToString(data, Base64.DEFAULT);
    }

    public static String getBearerToken(){
        return "Bearer <-add token->";
    }
}
