package demo.pratiked.twitterhash.util;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import demo.pratiked.twitterhash.R;

public class Text {

    public static SpannableString formatTweet(String tweet, Context context){

        SpannableString spannableTweet = new SpannableString(tweet);
        Matcher matcherHash = Pattern.compile("#([A-Za-z0-9_-]+)").matcher(spannableTweet);
        while (matcherHash.find()) {
            spannableTweet.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorAccent)), matcherHash.start(), matcherHash.end(), 0);
        }
        Matcher matcherUser = Pattern.compile("@([A-Za-z0-9_-]+)").matcher(spannableTweet);
        while (matcherUser.find()) {
            spannableTweet.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorAccent)), matcherUser.start(), matcherUser.end(), 0);
        }
        return spannableTweet;
    }
}
