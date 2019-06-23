package demo.pratiked.twitterhash.data.remote;

import java.util.List;

import demo.pratiked.twitterhash.data.Tweet;
import demo.pratiked.twitterhash.util.Constants;

public interface Callbacks {

    public interface TweetsListener{
        void onSuccess(List<Tweet> tweetList, Constants.QUERY_TYPE queryType);
        void onFailure(String error);
    }
}
