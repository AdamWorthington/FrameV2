package cs490.frame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;

public class ScrapbookViewActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener {

    private Bitmap bitmap;

    private MediaPlayer mMediaPlayer;
    private File video;

    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Surface s = new Surface(surface);

        try {
            mMediaPlayer= new MediaPlayer();
            mMediaPlayer.setDataSource(video.getAbsolutePath());
            mMediaPlayer.setSurface(s);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrapbook_view);

        ImageView iFrame = (ImageView) findViewById(R.id.imageFrame);
        TextureView vFrame = (TextureView) findViewById(R.id.videoFrame);

        Bundle extras = getIntent().getExtras();
        final String format = extras.getString("format");

        if(format.compareTo("photo") == 0)
        {
            String filePath = extras.getString("path");

            setPicture(iFrame, filePath);
            vFrame.setVisibility(View.GONE);
            iFrame.setVisibility(View.VISIBLE);
        }
        else if(format.compareTo("video") == 0)
        {
            vFrame.setSurfaceTextureListener(this);
            vFrame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mMediaPlayer.isPlaying())
                        mMediaPlayer.pause();
                    else
                        mMediaPlayer.start();
                }
            });
            File fil = (File)extras.getSerializable("file");
            video = fil;
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
        if(Build.MANUFACTURER.compareTo("motorola") != 0)
            matrix.postRotate(90);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap , 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        frame.setImageBitmap(rotatedBitmap);
    }
}
