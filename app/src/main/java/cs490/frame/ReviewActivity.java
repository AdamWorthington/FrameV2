package cs490.frame;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import java.io.File;

public class ReviewActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        FrameLayout frame = (FrameLayout) findViewById(R.id.reviewFrame);

        Bundle extras = getIntent().getExtras();
        String format = extras.getString("format");

        if(format.compareTo("photo") == 0)
        {
            String filePath = extras.getString("path");

            setPicture(frame, filePath);
        }
        else if(format.compareTo("video") == 0)
        {
            //We have a video
            Uri fileUri = extras.getParcelable("uri");
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

    private void setPicture(FrameLayout parent, String path)
    {
        //We have a photo
        ImageView photo = new ImageView(this);
        photo.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        // Get the dimensions of the View
        int targetW = parent.getWidth();
        int targetH = parent.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        }


        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
        photo.setImageBitmap(bitmap);
        parent.addView(photo);
    }
}
