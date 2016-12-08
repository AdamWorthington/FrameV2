package cs490.frame;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.common.io.Files;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import static cs490.frame.LoginActivity.userEmail;
import static cs490.frame.LoginActivity.username;

public class ReviewActivity extends AppCompatActivity implements CommentDialogFragment.NoticeDialogListener
{
    private Bitmap bitmap;
    private String comment;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_review);
        ImageView iFrame = (ImageView) findViewById(R.id.imageFrame);
        VideoView vFrame = (VideoView) findViewById(R.id.videoFrame);

        Bundle extras = getIntent().getExtras();
        final String format = extras.getString("format");
        final Uri fileUri = extras.getParcelable("uri");

        Button send = (Button) findViewById(R.id.sendButton);
        Button comment = (Button) findViewById(R.id.commentButton);
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommentDialogFragment dialogFragment = new CommentDialogFragment();
                dialogFragment.setExistingComment(ReviewActivity.this.comment);
                dialogFragment.show(getSupportFragmentManager(), "AddNoteDialogFragment");
            }
        });

        final Location location = WorldController.curLoc;

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (format.compareTo("photo") == 0) {
                    Post post = new Post();
                    if (location != null) {
                        post.setLat(location.getLatitude());
                        post.setLng(location.getLongitude());
                    } else {
                        post.setLat(40.429049);
                        post.setLng(-86.906065);
                    }
                    String send = ImageConverter.encodeTobase64(bitmap, false);
                    post.setPicture(send);
                    if (username != null) {
                        post.setUser(username);
                    } else {
                        post.setUser("user was null");
                    }
                    if (userEmail != null) {
                        post.setUserEmail(userEmail);
                    } else {
                        post.setUser("user was null");
                    }
                    try {
                        boolean posted = new PostImage().execute(post).get();
                        Log.i("postedImage", "Posted: " + posted + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                        if (posted == false) Toast.makeText(ReviewActivity.this, "Failed to post image", Toast.LENGTH_LONG).show();
                        else finish();
                    } catch (Exception e) {
                        Log.e("reviewactivity", "failed to post image" + e.getMessage());
                        Toast.makeText(ReviewActivity.this, "Failed to post image", Toast.LENGTH_LONG).show();
                    }
                }
                else if (format.compareTo("video") == 0) {
                    Post post = new Post();
                    if (location != null) {
                        post.setLat(location.getLatitude());
                        post.setLng(location.getLongitude());
                    } else {
                        post.setLat(40.429049);
                        post.setLng(-86.906065);
                    }
                    if (username != null) {
                        post.setUser(username);
                    } else {
                        post.setUser("user was null");
                    }
                    try {

                        InputStream iStream = getContentResolver().openInputStream(fileUri);
                        // write the inputStream to a FileOutputStream
                        File tempVideo = new File(getCacheDir(), "tempVideo");
                        FileOutputStream outputStream = new FileOutputStream(tempVideo);

                        int read = 0;
                        byte[] bytes = new byte[1024];

                        while ((read = iStream.read(bytes)) != -1) {
                            outputStream.write(bytes, 0, read);
                        }
                        byte[] array = Files.toByteArray(tempVideo);

                        if (array == null) Log.e("ReviewActivity", "byte array null");
                        else {
                            post.setVideo(array);
                            post.setVideoURI(fileUri);
                            //TODO: Send the post here
                            boolean posted = new PostVideo().execute(post).get();
                            if (!posted) {
                                Toast.makeText(ReviewActivity.this, "Video functionality still under construction.", Toast.LENGTH_LONG).show();
                            } else {
                                finish();
                            }
                        }
                        /*
                        post.setVideoURI(fileUri);
                        boolean posted = new PostVideo().execute(post).get();
                        if (!posted) {
                            Toast.makeText(ReviewActivity.this, "Video failed to post.", Toast.LENGTH_LONG).show();
                        }
                        */
                    } catch (Exception e) {
                        Log.e("sendvideo", e.getMessage());
                        Toast.makeText(ReviewActivity.this, "Error finding video file.", Toast.LENGTH_LONG).show();
                    }
                }
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
            vFrame.setVideoURI(fileUri);
            vFrame.setMediaController(new MediaController(this));
            vFrame.setVisibility(View.VISIBLE);
            vFrame.requestFocus();
            vFrame.start();
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
        if(Build.MANUFACTURER.compareTo("motorola") != 0)
            matrix.postRotate(90);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap , 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        frame.setImageBitmap(rotatedBitmap);
    }

    @Override
    public void onDialogPositiveClick(CommentDialogFragment dialog, String comment) {
        this.comment = comment;
        TextView caption = (TextView) findViewById(R.id.captionContent);
        caption.setText(comment);
        if(comment.isEmpty())
            caption.setText("None");
    }

    @Override
    public void onDialogNegativeClick(CommentDialogFragment dialog) {

    }
}
