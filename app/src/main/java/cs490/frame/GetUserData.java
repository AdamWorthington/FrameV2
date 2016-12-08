package cs490.frame;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.grant.myapplication.backend.myApi.MyApi;
import com.example.grant.myapplication.backend.myApi.model.ImageBean;
import com.example.grant.myapplication.backend.myApi.model.UserDataHolder;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

/**
 * Created by Scott on 10/6/2016.
 */

public class GetUserData extends AsyncTask<String, Void, UserDataHolder> {
    private static MyApi myApiService = null;
    private Context context;

    @Override
    protected UserDataHolder doInBackground(String...params) {
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
        String userEmail = params[0];

        try {
            UserDataHolder ret =  myApiService.getUserData(userEmail).execute();
            if (ret == null) {
                Log.e("getUserData", "userData is null");
                return null;
            }
            return ret;
        } catch (IOException e) {
            Log.e("getUserData", e.getMessage());
            return null;
        }
    }
}
