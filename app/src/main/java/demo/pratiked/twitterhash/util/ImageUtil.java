package demo.pratiked.twitterhash.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import demo.pratiked.twitterhash.R;

public class ImageUtil {

    public static void loadProfilePhoto(String url, ImageView imageView){

        Context context = imageView.getContext();

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_profile_picure)
                .dontAnimate()
                .fitCenter();


        try {
            Glide.with(context)
                    .load(url)
                    .apply(requestOptions)
                    .into(imageView);

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
