package cs490.frame;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.grant.myapplication.backend.myApi.model.IAHBean;
import com.example.grant.myapplication.backend.myApi.model.ImageAttributeHolder;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.example.grant.myapplication.backend.myApi.MyApi;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Scott on 10/6/2016.
 */

public class GetAllAttributes extends AsyncTask<Void, Void, ArrayList<ImageAttributeHolder>> {
    private static MyApi myApiService = null;
    private Context context;

    @Override
    protected ArrayList<ImageAttributeHolder> doInBackground(Void...params) {
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

        try {
            IAHBean container =  myApiService.getAttributes().execute();
            if (container == null) {
                Log.e("getAllAttributes", "container is null");
            }
            ArrayList<ImageAttributeHolder> ret = (ArrayList<ImageAttributeHolder>) container.getList();
            if (ret == null) {
                Log.e("getAllAttributes", "ret is null");
                if (container.getList() == null) {
                    Log.e("getAllAttributes", "container.getData() is null");
                }
            }
            return ret;
        } catch (IOException e) {
            Log.e("getAllAttributes", e.getMessage());
            return null;
        }
    }
}
