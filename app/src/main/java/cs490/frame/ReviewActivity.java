package cs490.frame;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
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
        ImageView iFrame = (ImageView) findViewById(R.id.imageFrame);
        VideoView vFrame = (VideoView) findViewById(R.id.videoFrame);

        Bundle extras = getIntent().getExtras();
        String format = extras.getString("format");

        if(format.compareTo("photo") == 0)
        {
            String filePath = extras.getString("path");

            setPicture(iFrame, filePath);
            vFrame.setVisibility(View.GONE);
            iFrame.setVisibility(View.VISIBLE);
        }
        else if(format.compareTo("video") == 0)
        {

            //We have a video
            Uri fileUri = extras.getParcelable("uri");
            vFrame.setVideoURI(fileUri);
            vFrame.setMediaController(new MediaController(this));
            vFrame.setVisibility(View.VISIBLE);
            iFrame.setVisibility(View.GONE);
        }
        else
        {
            //We have something else?
        }
    }

    private void setPicture(ImageView frame, String path)
    {
        // Get the dimensions of the View
        int targetW = frame.getWidth();
        int targetH = frame.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        if(targetH == 0 && targetW == 0)
        {
            targetW = 720;
            targetH = 1280;
        }

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
        frame.setImageBitmap(bitmap);
    }
}
