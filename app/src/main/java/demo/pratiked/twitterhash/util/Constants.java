package demo.pratiked.twitterhash.util;

public class Constants {

    public enum QUERY_TYPE{
        NORMAL,
        NEXT_PAGE,
        NEW
    }

    public static final String SHARED_PREF_QUERY = "queries";
    public static final String SHARED_PREF_QUERY_NEW = "new";
    public static final String SHARED_PREF_QUERY_NEXT = "next";

    public static final String DATE_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy";
}
