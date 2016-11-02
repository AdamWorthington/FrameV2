package cs490.frame;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import java.io.File;

import static android.R.attr.bitmap;

public class ReviewActivity extends AppCompatActivity
{
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_review);
        ImageView iFrame = (ImageView) findViewById(R.id.imageFrame);
        VideoView vFrame = (VideoView) findViewById(R.id.videoFrame);

        Bundle extras = getIntent().getExtras();
        String format = extras.getString("format");

        Button send = (Button) findViewById(R.id.sendButton);
        Button comment = (Button) findViewById(R.id.commentButton);

        final Location location = WorldController.curLoc;
        final String displayName = LoginActivity.username;

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Post post = new Post();
                if (location != null) {
                    post.setLat(location.getLatitude());
                    post.setLng(location.getLongitude());
                }
                else {
                    post.setLat(40.429049);
                    post.setLng(-86.906065);
                }
                String send = ImageConverter.encodeTobase64(bitmap, false);
                post.setPicture(send);
                if (displayName != null) {
                    post.setUser(displayName);
                }
                else {
                    post.setUser("user was null");
                }
                try {
                    boolean posted = new PostImage().execute(post).get();
                    Log.i("postedImage", "Posted: " + posted + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent ret = new Intent(ReviewActivity.this, WorldController.class);
                startActivity(ret);
            }
        });

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

        bitmap = BitmapFactory.decodeFile(path, bmOptions);
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap , 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        frame.setImageBitmap(rotatedBitmap);
    }
}
