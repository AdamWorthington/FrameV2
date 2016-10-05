package cs490.frame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class CameraActivity extends AppCompatActivity {


    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    static final int REQUEST_VIDEO_CAPTURE = 1;
    static final int REQUEST_IMAGE_CAPTURE = 2;
    static final int REQUEST_CAMERA_ACCESS = 3;
    static final int REQUEST_VIDEO_ACCESS  = 4;

    private String savedPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_camera);

        Button cameraButton = (Button) findViewById(R.id.picture_button);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCameraPicture();
            }
        });
        Button videoButton = (Button) findViewById(R.id.video_button);
        videoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCameraVideo();
            }
        });
    }

    private void onCameraPicture()
    {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA},
                    REQUEST_CAMERA_ACCESS);

            return;
        }

        switchToCameraActivity();
    }

    private File createFile(boolean isPhoto) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName;
        String suffix;

        if(isPhoto)
        {
            fileName = "JPEG_" + timeStamp + "_";
            suffix = ".jpg";
        }
        else
        {
            fileName = "MP4_" + timeStamp + "_";
            suffix = ".mp4";
        }

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                fileName,  /* prefix */
                suffix,         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }

    private void switchToCameraActivity()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createFile(true);
            savedPhotoPath = photoFile.getAbsolutePath();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        if(photoFile != null)
        {
            Uri photoUri = FileProvider.getUriForFile(this, "cs490.frame", photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void switchToVideoActivity()
    {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        File videoFile = null;
        try {
            videoFile = createFile(false);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        if(videoFile != null)
        {
            Uri videoUri = FileProvider.getUriForFile(this, "cs490.frame", videoFile);
            takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
            takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }

    }

    private void onCameraVideo()
    {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA},
                    REQUEST_VIDEO_ACCESS);

            return;
        }

        switchToVideoActivity();
    }

    @Override
    @SuppressWarnings({"MissingPermission"})
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_ACCESS:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    switchToCameraActivity();
                } else {
                    // permission denied. Return to the worldview
                    finish();
                }
                return;
            }
            case REQUEST_VIDEO_ACCESS:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    switchToVideoActivity();
                } else {
                    // permission denied. Return to the worldview
                    finish();
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoURI = intent.getData();

            Intent resultIntent = new Intent(this, ReviewActivity.class);
            resultIntent.putExtra("uri", videoURI);
            resultIntent.putExtra("format", "video");
            startActivity(resultIntent);
        }
        else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            //We can be guaranteed that the photo URI will be what we passed in.
            Intent resultIntent = new Intent(this, ReviewActivity.class);
            resultIntent.putExtra("path", savedPhotoPath);
            resultIntent.putExtra("format", "photo");
            startActivity(resultIntent);
        }
    }
}
