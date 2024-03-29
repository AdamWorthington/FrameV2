package cs490.frame;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.grant.myapplication.backend.myApi.MyApi;
import com.example.grant.myapplication.backend.myApi.model.Comment;
import com.example.grant.myapplication.backend.myApi.model.IAHBean;
import com.example.grant.myapplication.backend.myApi.model.ImageAttributeHolder;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Scott on 12/8/2016.
 */

public class GetComments extends AsyncTask<Integer, Void, Comment> {
    private static MyApi myApiService = null;
    private Context context;

    @Override
    protected Comment doInBackground(Integer...params) {
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
        int postID = params[0];

        try {
            Comment response =  myApiService.getComments(postID).execute();
            if (response == null) {
                Log.e("getComments", "comments object is null");
                return null;
            }
            if (response.getComments() == null) {
                Log.e("getComments", "comments arraylist is null");
                return null;
            }
            return response;
        } catch (IOException e) {
            Log.e("getComments", e.getMessage());
            return null;
        }
    }
}
