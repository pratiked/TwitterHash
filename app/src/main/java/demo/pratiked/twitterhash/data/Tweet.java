package demo.pratiked.twitterhash.data;

import org.json.JSONException;
import org.json.JSONObject;

public class Tweet {

    private String created_at;
    private String id_str;
    private String text;
    private String entities;
    private String metadata;
    private String source;
    private String user;
    private String retweeted_status;
    private boolean truncated;
    private boolean is_quote_status;
    private boolean favorited;
    private boolean retweeted;
    private long id;
    private int retweet_count;
    private int favorite_count;

    public Tweet(String tweetObject) {

        String created_at = null;
        String id_str = null;
        String text = null;
        String entities = null;
        String metadata = null;
        String source = null;
        String user = null;
        String retweeted_status = null;
        boolean truncated = false;
        boolean is_quote_status = false;
        boolean favorited = false;
        boolean retweeted = false;
        long id = 0;
        int retweet_count = 0;
        int favorite_count = 0;

        try {

            JSONObject jsonObjectTweet = new JSONObject(tweetObject);

            if (jsonObjectTweet.has("created_at")){
                created_at = jsonObjectTweet.getString("created_at");
            }

            if (jsonObjectTweet.has("id_str")){
                id_str = jsonObjectTweet.getString("id_str");
            }

            if (jsonObjectTweet.has("text")){
                text = jsonObjectTweet.getString("text");
            }


            if (jsonObjectTweet.has("entities")){
                entities = jsonObjectTweet.getString("entities");
            }


            if (jsonObjectTweet.has("metadata")){
                metadata = jsonObjectTweet.getString("metadata");
            }

            if (jsonObjectTweet.has("source")){
                source = jsonObjectTweet.getString("source");
            }

            if (jsonObjectTweet.has("user")){
                user = jsonObjectTweet.getString("user");
            }

            if (jsonObjectTweet.has("retweeted_status")){
                retweeted_status = jsonObjectTweet.getString("retweeted_status");
            }

            if (jsonObjectTweet.has("truncated")){
                truncated = jsonObjectTweet.getBoolean("truncated");
            }

            if (jsonObjectTweet.has("is_quote_status")){
                is_quote_status = jsonObjectTweet.getBoolean("is_quote_status");
            }

            if (jsonObjectTweet.has("favorited")){
                favorited = jsonObjectTweet.getBoolean("favorited");
            }

            if (jsonObjectTweet.has("retweeted")){
                retweeted = jsonObjectTweet.getBoolean("retweeted");
            }

            if (jsonObjectTweet.has("id")){
                id = jsonObjectTweet.getLong("id");
            }

            if (jsonObjectTweet.has("retweet_count")){
                retweet_count = jsonObjectTweet.getInt("retweet_count");
            }

            if (jsonObjectTweet.has("favorite_count")){
                favorite_count = jsonObjectTweet.getInt("favorite_count");
            }

        } catch (JSONException e){
            e.printStackTrace();
        }


        this.created_at = created_at;
        this.id_str = id_str;
        this.text = text;
        this.entities = entities;
        this.metadata = metadata;
        this.source = source;
        this.user = user;
        this.retweeted_status = retweeted_status;
        this.truncated = truncated;
        this.is_quote_status = is_quote_status;
        this.favorited = favorited;
        this.retweeted = retweeted;
        this.id = id;
        this.retweet_count = retweet_count;
        this.favorite_count = favorite_count;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getId_str() {
        return id_str;
    }

    public void setId_str(String id_str) {
        this.id_str = id_str;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getEntities() {
        return entities;
    }

    public void setEntities(String entities) {
        this.entities = entities;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRetweeted_status() {
        return retweeted_status;
    }

    public void setRetweeted_status(String retweeted_status) {
        this.retweeted_status = retweeted_status;
    }

    public boolean isTruncated() {
        return truncated;
    }

    public void setTruncated(boolean truncated) {
        this.truncated = truncated;
    }

    public boolean isIs_quote_status() {
        return is_quote_status;
    }

    public void setIs_quote_status(boolean is_quote_status) {
        this.is_quote_status = is_quote_status;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public boolean isRetweeted() {
        return retweeted;
    }

    public void setRetweeted(boolean retweeted) {
        this.retweeted = retweeted;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRetweet_count() {
        return retweet_count;
    }

    public void setRetweet_count(int retweet_count) {
        this.retweet_count = retweet_count;
    }

    public int getFavorite_count() {
        return favorite_count;
    }

    public void setFavorite_count(int favorite_count) {
        this.favorite_count = favorite_count;
    }
}
