package demo.pratiked.twitterhash.data;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

    private String name;
    private String screen_name;
    private String profile_image_url;

    public User(String user) {

        String name = null;
        String screen_name = null;
        String profile_image_url = null;

        try {
            JSONObject jsonObjectUser = new JSONObject(user);

            if (jsonObjectUser.has("name")){
                name = jsonObjectUser.getString("name");
            }

            if (jsonObjectUser.has("screen_name")){
                screen_name = jsonObjectUser.getString("screen_name");
            }

            if (jsonObjectUser.has("profile_image_url")){
                profile_image_url = jsonObjectUser.getString("profile_image_url");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.name = name;
        this.screen_name = screen_name;
        this.profile_image_url = profile_image_url;
    }

    public String getName() {
        return name;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }
}
