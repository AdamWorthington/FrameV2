package cs490.frame;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.grant.myapplication.backend.myApi.MyApi;
import com.example.grant.myapplication.backend.myApi.model.Comment;
import com.example.grant.myapplication.backend.myApi.model.MyBean;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

/**
 * Created by Scott on 10/6/2016.
 */

public class PostComment extends AsyncTask<Comment, Void, Boolean> {
    private static MyApi myApiService = null;
    private Context context;

    @Override
    protected Boolean doInBackground(Comment...params) {
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

        Comment comment = params[0];
        Log.i("postComment", "postID: " + comment.getPostID() + " user: " + comment.getUser() + " comment: " + comment.getComment());

        try {
            MyBean response = myApiService.postComment(comment).execute();
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
