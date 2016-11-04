package cs490.frame;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.grant.myapplication.backend.myApi.MyApi;
import com.example.grant.myapplication.backend.myApi.model.ImageBean;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

/**
 * Created by Scott on 10/6/2016.
 */

public class GetImage extends AsyncTask<Integer, Void, ImageBean> {
    private static MyApi myApiService = null;
    private Context context;

    @Override
    protected ImageBean doInBackground(Integer...params) {
        if(myApiService == null) {  // Only do this once
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    .setRootUrl("https://frame-145601.appspot.com/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });

            myApiService = builder.build();
        }
        int id = params[0];

        try {
            ImageBean image =  myApiService.getImage(id).execute();
            if (image == null) {
                Log.e("getImage", "image is null");
            }
            return image;
        } catch (IOException e) {
            Log.e("getImage", e.getMessage());
            ImageBean ret = new ImageBean();
            ret.setInfo(e.getMessage());
            return ret;
        }
    }
}
