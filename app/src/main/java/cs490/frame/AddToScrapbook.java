package cs490.frame;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.grant.myapplication.backend.myApi.MyApi;
import com.example.grant.myapplication.backend.myApi.model.MyBean;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

/**
 * Created by Scott on 10/6/2016.
 */

public class AddToScrapbook extends AsyncTask<Post, Void, Boolean> {
    private static MyApi myApiService = null;
    private Context context;

    @Override
    protected Boolean doInBackground(Post...params) {
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

        Post post = params[0];
        Log.i("addToScrapbook", "postID: " + post.getPostID() + " user: " + post.getUser());

        try {
            MyBean response = myApiService.addToScrapbook(post.getPostID(), post.getUser()).execute();
            if (response.getData() == false) {
                Log.e("PostComment", "response message: " + response.getInfo());
            }
            return response.getData();
        } catch (IOException e) {
            Log.i("postComment", "IOException occured~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            Log.e("postComment", e.getMessage());
            return false;
        }
    }
}
