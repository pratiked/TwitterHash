package demo.pratiked.twitterhash.util;

import android.content.Context;
import android.content.SharedPreferences;

public class Api {

    private static final String HOST = "https://api.twitter.com";

    private static final String SEARCH_TWEETS = "/1.1/search/tweets.json";

    public static String getNormalSearchQuery(String hashtag){
        //default count = 15
        return HOST + SEARCH_TWEETS + "?q=%23"+ hashtag +"&result_type=recent";
    }

    public static String getNewSearchQuery(Context context){

        SharedPreferences sharedPref = context.getSharedPreferences(Constants.SHARED_PREF_QUERY, Context.MODE_PRIVATE);
        String newQuery = sharedPref.getString(Constants.SHARED_PREF_QUERY_NEW, null);
        if (newQuery != null){
            return HOST + SEARCH_TWEETS + newQuery;
        } else {
            return "";
        }

    }

    public static String getNextSearchQuery(Context context){

        SharedPreferences sharedPref = context.getSharedPreferences(Constants.SHARED_PREF_QUERY, Context.MODE_PRIVATE);
        String nextQuery = sharedPref.getString(Constants.SHARED_PREF_QUERY_NEXT, null);
        if (nextQuery != null){
            return HOST + SEARCH_TWEETS + nextQuery;
        } else {
            return "";
        }

    }
}
