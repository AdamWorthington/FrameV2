package cs490.frame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by Scott on 10/6/2016.
 */

public class ImageConverter {

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedByte = Base64.decode(input, 0);

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length, o);
        o.inSampleSize = calculateInSampleSize(o, 300, 300);
        o.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length, o);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
    {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth)
        {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth)
            {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static String encodeTobase64(Bitmap image, Boolean text)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if(!text){
            image.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        }
        else{
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        }
        byte[] b = baos.toByteArray();
        String toReturn = Base64.encodeToString(b,Base64.DEFAULT);
        return toReturn;
    }
}
