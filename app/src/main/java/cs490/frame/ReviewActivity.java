package cs490.frame;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

public class ReviewActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        RelativeLayout frame = (RelativeLayout) findViewById(R.id.reviewFrame);

        Bundle extras = getIntent().getExtras();
        Uri fileUri = extras.getParcelable("uri");
        String format = extras.getString("format");

        if(format.compareTo("photo") == 0)
        {
            //We have a photo
            ImageView photo = new ImageView(this);
            photo.setImageURI(fileUri);
            photo.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.FILL_PARENT));

            frame.addView(photo);
        }
        else if(format.compareTo("video") == 0)
        {
            //We have a video
            VideoView video = new VideoView(this);
            video.setVideoURI(fileUri);
            video.setMediaController(new MediaController(this));
            video.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.FILL_PARENT));

            frame.addView(video);
        }
        else
        {
            //We have something else?
        }
    }
}
