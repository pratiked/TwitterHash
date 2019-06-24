package demo.pratiked.twitterhash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import demo.pratiked.twitterhash.adapters.TweetsAdapter;
import demo.pratiked.twitterhash.data.Tweet;
import demo.pratiked.twitterhash.data.remote.Callbacks;
import demo.pratiked.twitterhash.data.remote.Twitter;
import demo.pratiked.twitterhash.util.Constants;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private List<Tweet> mTweetList;
    private TweetsAdapter mTweetsAdapter;
    private String mActiveHashtag = "Twitter";
    private Handler mHandlerTimer;
    private Runnable mRunnableTimer;
    private int mTimer;

    private int mTotalItemCount;
    private int mLastVisiblePosition;
    private int mThreshold = 3;
    private boolean mIsLoading = false;

    @BindView(R.id.txt_title)
    TextView txtTitle;

    @BindView(R.id.rv_tweets_feed)
    RecyclerView recyclerViewFeed;

    @BindView(R.id.pb_tweets_feed)
    ProgressBar progressBarFeed;

    @BindView(R.id.img_search)
    ImageView imgSearch;

    @BindView(R.id.edt_search)
    EditText edtSearch;

    @BindView(R.id.txt_new)
    TextView txtNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Twitter.registerTweetsListener(tweetsListener);

        setTitle();
        txtNew.setVisibility(View.GONE);
        mTweetList = new ArrayList<>();

        mTweetsAdapter = new TweetsAdapter(mTweetList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerViewFeed.setLayoutManager(linearLayoutManager);
        recyclerViewFeed.setHasFixedSize(true);
        recyclerViewFeed.setAdapter(mTweetsAdapter);

        showProgress();
        getTweets(Constants.QUERY_TYPE.NORMAL);

        recyclerViewFeed.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                mTotalItemCount = linearLayoutManager.getItemCount();
                mLastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition();

                if (!mIsLoading && mTotalItemCount <= mLastVisiblePosition + mThreshold){
                    stopTimer();
                    mIsLoading = true;
                    Log.i(TAG, "onScrolled: getting next page");
                    Twitter.getTweets(MainActivity.this, mActiveHashtag, Constants.QUERY_TYPE.NEXT_PAGE);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Twitter.unregisterTweetsListener();
        Twitter.cancelTask();

    }

    @Override
    protected void onPause() {
        super.onPause();

        stopTimer();
    }

    @OnClick(R.id.img_search)
    void search(){

        String search = edtSearch.getText().toString().trim();

        if (!isValidSearch(search)){
            Toast.makeText(this, "Invalid Hashtag!", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.i(TAG, "search: hashtag: " + search);

        mActiveHashtag = search;
        setTitle();
        hideKeyboard();
        imgSearch.setEnabled(false);
        edtSearch.setEnabled(false);

        mTotalItemCount = 0;
        mLastVisiblePosition = 0;
        mIsLoading = false;
        txtNew.setVisibility(View.GONE);

        showProgress();
        getTweets(Constants.QUERY_TYPE.NORMAL);

    }

    @OnClick(R.id.txt_new)
    void goToNew(){

        txtNew.setVisibility(View.GONE);
        mTweetsAdapter.notifyDataSetChanged();
        recyclerViewFeed.scrollToPosition(0);
        startTimer();
    }

    public static boolean isValidSearch(String search){

        if (TextUtils.isEmpty(search)){
            return false;
        }

        if (search.contains("#") || search.contains("@") || search.contains(" ")){
            return false;
        }

        return true;
    }

    private void getTweets(Constants.QUERY_TYPE queryType){
        stopTimer();
        Twitter.getTweets(this, mActiveHashtag, queryType);
    }


    private Callbacks.TweetsListener tweetsListener = new Callbacks.TweetsListener() {
        @Override
        public void onSuccess(List<Tweet> tweetList, Constants.QUERY_TYPE queryType) {

            int tweetListSize = tweetList.size();

            Log.i(TAG, "onSuccess: number of tweets: " + tweetListSize);

            switch (queryType){
                case NEW:

                    if (tweetListSize > 0){

                        for (int i = 0; i < tweetList.size(); i++){
                            mTweetList.add(i, tweetList.get(i));
                        }

                        txtNew.setVisibility(View.VISIBLE);

                    } else {
                        startTimer();
                    }
                    break;
                case NEXT_PAGE:

                    mTweetList.addAll(tweetList);
                    startTimer();
                    mTweetsAdapter.notifyDataSetChanged();
                    break;
                case NORMAL:
                default:

                    mTweetList.clear();
                    recyclerViewFeed.removeAllViews();
                    recyclerViewFeed.removeAllViewsInLayout();

                    mTweetList.addAll(tweetList);
                    startTimer();
                    mTweetsAdapter.notifyDataSetChanged();
                    break;
            }

            mIsLoading = false;
            hideProgress();
            edtSearch.setText("");
            edtSearch.setEnabled(true);
            imgSearch.setEnabled(true);
        }

        @Override
        public void onFailure(String error) {

            hideProgress();
            edtSearch.setEnabled(true);
            imgSearch.setEnabled(true);
        }
    };

    private void startTimer(){
        Log.i(TAG, "startTimer");
        mTimer = 0;
        mHandlerTimer = new Handler();
        mRunnableTimer = new Runnable() {
            @Override
            public void run() {
                mTimer++;

                Log.i(TAG, "run: " + mTimer);

                if (mTimer == 10){
                    stopTimer();

                    getTweets(Constants.QUERY_TYPE.NEW);
                }

                if (mHandlerTimer != null && mRunnableTimer != null){
                    mHandlerTimer.postDelayed(this, 1000);
                }
            }
        };
        mHandlerTimer.post(mRunnableTimer);
    }

    private void stopTimer(){

        Log.i(TAG, "stopTimer");

        if (mHandlerTimer != null && mRunnableTimer != null){
            mHandlerTimer.removeCallbacks(mRunnableTimer);
            mHandlerTimer = null;
            mRunnableTimer = null;
        }
    }

    private void setTitle(){
        String title = "#" + mActiveHashtag;
        txtTitle.setText(title);
    }

    private void showProgress(){
        recyclerViewFeed.setVisibility(View.GONE);
        progressBarFeed.setVisibility(View.VISIBLE);
    }

    private void hideProgress(){
        recyclerViewFeed.setVisibility(View.VISIBLE);
        progressBarFeed.setVisibility(View.GONE);
    }

    private void hideKeyboard(){

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
