package cs490.frame;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class DisplayImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);

        Bitmap image = null;
        String picture = WorldController.showPicture;

        if (picture != null) {
            image = ImageConverter.decodeBase64(picture);
        }

        ImageView view = (ImageView) findViewById(R.id.imageView);
        if (image != null) {
            view.setImageBitmap(image);
        }
    }
}
