package cs490.frame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by cwilh on 12/8/2016.
 */
public class ScrapbookAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<File> images;

    public ScrapbookAdapter(Context c, ArrayList<File> images) {
        mContext = c;
        this.images = images;
    }

    public int getCount() {
        return images.size();
    }

    public File getItem(int position) {
        return images.get(position);

    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
            boolean photo = images.get(position).getAbsolutePath().endsWith(".jpeg");
            if(!photo) {
                imageView.setImageBitmap(ThumbnailUtils.createVideoThumbnail(images.get(position).getAbsolutePath(),
                        MediaStore.Images.Thumbnails.MINI_KIND));
            }
            else {
                imageView.setImageBitmap(BitmapFactory.decodeFile(images.get(position).getAbsolutePath()));
            }
        } else {
            imageView = (ImageView) convertView;
        }

        return imageView;
    }
}