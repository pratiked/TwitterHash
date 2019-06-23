package demo.pratiked.twitterhash.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import demo.pratiked.twitterhash.R;
import demo.pratiked.twitterhash.data.Tweet;
import demo.pratiked.twitterhash.data.User;
import demo.pratiked.twitterhash.util.DateTimeUtil;
import demo.pratiked.twitterhash.util.ImageUtil;
import demo.pratiked.twitterhash.util.Text;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.TweetViewHolder> {

    private List<Tweet> mTweetsList;

    public TweetsAdapter(List<Tweet> tweetList) {
        this.mTweetsList = tweetList;
    }

    @NonNull
    @Override
    public TweetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tweet, parent, false);
        return new TweetViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TweetViewHolder holder, int position) {

        Tweet currentTweet = mTweetsList.get(position);

        holder.setTweet(currentTweet.getText(), currentTweet.getRetweeted_status());
        holder.setUser(currentTweet.getUser());
        holder.setTime(currentTweet.getCreated_at());
        holder.setCounts(currentTweet.getRetweet_count(), currentTweet.getFavorite_count());
    }

    @Override
    public int getItemCount() {
        return mTweetsList.size();
    }

    public class TweetViewHolder extends RecyclerView.ViewHolder{

        private static final String TAG = "TweetViewHolder";

        private Context mContext;

        private CircleImageView imgProfilePhoto;
        private TextView txtName;
        private TextView txtUsername;
        private TextView txtTweet;
        private LinearLayout linearLayoutRetweet;
        private CircleImageView imgProfilePhotoRetweet;
        private TextView txtNameRetweet;
        private TextView txtUsernameRetweet;
        private TextView txtRetweet;
        private TextView txtTime;
        private TextView txtFavoriteCount;
        private TextView txtRetweetCount;

        private TweetViewHolder(@NonNull View itemView) {
            super(itemView);

            mContext = itemView.getContext();

            imgProfilePhoto = itemView.findViewById(R.id.img_profile_photo);
            txtName = itemView.findViewById(R.id.txt_name);
            txtUsername = itemView.findViewById(R.id.txt_username);
            txtTweet = itemView.findViewById(R.id.txt_tweet_text);
            linearLayoutRetweet = itemView.findViewById(R.id.ll_retweet);
            imgProfilePhotoRetweet = itemView.findViewById(R.id.img_profile_photo_retweet);
            txtNameRetweet = itemView.findViewById(R.id.txt_name_retweet);
            txtUsernameRetweet = itemView.findViewById(R.id.txt_username_retweet);
            txtRetweet = itemView.findViewById(R.id.txt_tweet_text_retweet);
            txtTime = itemView.findViewById(R.id.txt_time);
            txtFavoriteCount = itemView.findViewById(R.id.txt_favourite_count);
            txtRetweetCount = itemView.findViewById(R.id.txt_retweet_count);

        }

        private void setTweet(String tweet, String retweet){
            if (retweet != null && tweet != null){

                Tweet myTweet = new Tweet(retweet);

                String myTweetText = myTweet.getText();

                if (myTweetText != null){
                    txtTweet.setText("Retweeted");
                    txtRetweet.setText(Text.formatTweet(myTweetText, mContext));
                    setRetweetUser(myTweet.getUser());
                    linearLayoutRetweet.setVisibility(View.VISIBLE);
                } else {
                    txtTweet.setText(Text.formatTweet(tweet, mContext));
                    linearLayoutRetweet.setVisibility(View.GONE);
                }
            } else if (tweet != null){
                txtTweet.setText(Text.formatTweet(tweet, mContext));
                linearLayoutRetweet.setVisibility(View.GONE);
            }
        }

        private void setUser(String user){

            User currentUser = new User(user);

            String name = currentUser.getName();
            String screenName = currentUser.getScreen_name();
            String profilePictureUrl = currentUser.getProfile_image_url();

            if (name != null){
                txtName.setText(name);
            } else {
                txtName.setVisibility(View.GONE);
            }

            if (screenName != null){
                txtUsername.setText("@" + screenName);
            } else {
                txtUsername.setVisibility(View.GONE);
            }

            if (profilePictureUrl != null){
                ImageUtil.loadProfilePhoto(profilePictureUrl, imgProfilePhoto);
            }
        }

        private void setRetweetUser(String user){

            User currentUser = new User(user);

            String name = currentUser.getName();
            String screenName = currentUser.getScreen_name();
            String profilePictureUrl = currentUser.getProfile_image_url();

            if (name != null){
                txtNameRetweet.setText(name);
            } else {
                txtNameRetweet.setVisibility(View.GONE);
            }

            if (screenName != null){
                txtUsernameRetweet.setText("@" + screenName);
            } else {
                txtUsernameRetweet.setVisibility(View.GONE);
            }

            if (profilePictureUrl != null){
                ImageUtil.loadProfilePhoto(profilePictureUrl, imgProfilePhotoRetweet);
            }
        }

        private void setTime(String createdAt){
            txtTime.setText(DateTimeUtil.getRelativeTime(createdAt));
        }

        private void setCounts(int retweetCount, int favoriteCount){
            txtRetweetCount.setText(String.valueOf(retweetCount));
            txtFavoriteCount.setText(String.valueOf(favoriteCount));
        }
    }
}
