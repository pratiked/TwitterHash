package demo.pratiked.twitterhash.data.remote;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import demo.pratiked.twitterhash.data.Tweet;
import demo.pratiked.twitterhash.util.Api;
import demo.pratiked.twitterhash.util.Config;
import demo.pratiked.twitterhash.util.Constants;

public class Twitter {

    private static final String TAG = "Twitter";

    private static QueryTweetsTask mQueryTweetsTask;
    private static Callbacks.TweetsListener mTweetsListener;

    public static void registerTweetsListener(Callbacks.TweetsListener tweetsListener){
        mTweetsListener = tweetsListener;
    }

    public static void unregisterTweetsListener(){
        mTweetsListener = null;
    }

    public static void getTweets(Context context, String hashtag, Constants.QUERY_TYPE queryType){

        Log.i(TAG, "getTweets: hashtag: " + hashtag);

        mQueryTweetsTask = new QueryTweetsTask(context, hashtag, queryType);
        mQueryTweetsTask.execute();
    }

    public static void cancelTask(){
        if (mQueryTweetsTask != null){
            mQueryTweetsTask.cancel(true);
        }
    }

    public static class QueryTweetsTask extends AsyncTask<Void, Void, List<Tweet>>{

        private String mHashtag;
        private Constants.QUERY_TYPE mQueryType;
        private WeakReference<Context> mWeakReferenceContext;

        QueryTweetsTask(Context context, String hashtag, Constants.QUERY_TYPE queryType) {
            mWeakReferenceContext = new WeakReference<>(context);
            this.mHashtag = hashtag;
            this.mQueryType = queryType;
        }

        @Override
        protected List<Tweet> doInBackground(Void... voids) {

            List<Tweet> tweetList = new ArrayList<>();
            URL url = null;

            try {

                switch (mQueryType){
                    case NEW:
                        url = new URL(Api.getNewSearchQuery(mWeakReferenceContext.get()));
                        break;
                    case NEXT_PAGE:
                        url = new URL(Api.getNextSearchQuery(mWeakReferenceContext.get()));
                        break;
                    case NORMAL:
                    default:
                        url = new URL(Api.getNormalSearchQuery(mHashtag));
                        break;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            HttpURLConnection httpConn;
            InputStream inputStream = null;

            StringBuilder response = new StringBuilder();

            if (url != null){

                try {

                    httpConn = (HttpURLConnection) url.openConnection();
                    String contentType = "application/json";
                    httpConn.setRequestMethod("GET");
                    httpConn.setRequestProperty("Content-type", contentType);
                    httpConn.setRequestProperty("Authorization", Config.getBearerToken());
                    httpConn.setConnectTimeout(15000);
                    httpConn.setReadTimeout(15000);
                    inputStream = new BufferedInputStream(httpConn.getInputStream());

                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    Log.i(TAG, "doInBackground: response: success");
                    tweetList.addAll(parseTweets(response.toString(), mWeakReferenceContext.get()));

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {

                    try {
                        if (inputStream != null){
                            inputStream.close();
                        }

                    } catch (IOException | NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }

            return tweetList;
        }

        @Override
        protected void onPostExecute(List<Tweet> tweets) {
            super.onPostExecute(tweets);

            if (!isCancelled()){
                if (mTweetsListener != null){
                    mTweetsListener.onSuccess(tweets, mQueryType);
                }
            } else {
                Log.i(TAG, "onPostExecute: network call cancelled");
            }
        }


        private List<Tweet> parseTweets(String response, Context context){

            List<Tweet> tweetList = new ArrayList<>();

            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArrayTweets = jsonObject.getJSONArray("statuses");

                Log.i(TAG, "parseTweets: array size: " + jsonArrayTweets.length());

                //JSONObject jsonObjectTweet;
                String tweetObject;
                Tweet tweet;

                for (int i = 0; i < jsonArrayTweets.length(); i++){

                    tweetObject = jsonArrayTweets.getString(i);
                    tweet = new Tweet(tweetObject);
                    tweetList.add(tweet);
                }

                storeSearchMetadata(jsonObject.getJSONObject("search_metadata"), context, mQueryType);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return tweetList;
        }

        private void storeSearchMetadata(JSONObject searchMetadata, Context context, Constants.QUERY_TYPE query_type){

            try {
                //refresh url gives newly added tweets and uses since_id
                String refreshUrl = null;
                if (searchMetadata.has("refresh_url")){
                    refreshUrl = searchMetadata.getString("refresh_url");
                }


                //next result gives tweets older or equal to last fetched tweet. It uses max_id.
                String nextResult = null;
                if (searchMetadata.has("next_results")){
                    nextResult = searchMetadata.getString("next_results");
                }

                Log.i(TAG, "storeSearchMetadata: refreshUrl: " + refreshUrl);
                Log.i(TAG, "storeSearchMetadata: nextResult: " + nextResult);

                SharedPreferences sharedPref = context.getSharedPreferences(Constants.SHARED_PREF_QUERY, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                if (refreshUrl != null && (query_type == Constants.QUERY_TYPE.NEW || query_type == Constants.QUERY_TYPE.NORMAL)){
                    Log.i(TAG, "storeSearchMetadata: refresh url changed");
                    editor.putString(Constants.SHARED_PREF_QUERY_NEW, refreshUrl);
                }

                if (nextResult != null && (query_type == Constants.QUERY_TYPE.NEXT_PAGE || query_type == Constants.QUERY_TYPE.NORMAL)){
                    Log.i(TAG, "storeSearchMetadata: next url changed");
                    editor.putString(Constants.SHARED_PREF_QUERY_NEXT, nextResult);
                }

                editor.apply();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
