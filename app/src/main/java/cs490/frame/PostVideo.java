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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;

import static cs490.frame.LoginActivity.userEmail;

/**
 * Created by Scott on 10/6/2016.
 */

public class PostVideo extends AsyncTask<Post, Void, Boolean> {
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

        /*
        String toReturn = Base64.encodeToString(post.getVideo(),Base64.DEFAULT);
        VideoBean video = new VideoBean();
        video.setVideo(toReturn);
        */

        //Get the upload url
        MyBean response = null;
        try {
            response = myApiService.getBlobURL(0).execute();
        } catch (Exception e) {
            Log.e("postVideo", e.getMessage());
            return false;
        }

        if (response == null) {
            Log.e("postVideo", "response from getBlobURL was null");
            return false;
        }
        else if (!response.getData()) {
            Log.e("postVideo", "response from getBlobURL did not contain URL");
            return false;
        }

        Log.d("postVideo", "uploadURL: " + response.getInfo());

        //Url retrieved, now send the file to that url
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(response.getInfo());

        File video = new File(post.getVideoURI().getPath());
        FileBody fileBody = new FileBody(video);
        MultipartEntity reqEntity = new MultipartEntity();

        reqEntity.addPart("video", fileBody);

        httpPost.setEntity(reqEntity);
        HttpResponse resp = null;
        MyBean respon = null;
        try {
            resp = httpClient.execute(httpPost);
            if (resp == null) {
                Log.e("postVideo", "response from upload servlet was null");
                return false;
            }
            ObjectInputStream in = new ObjectInputStream(resp.getEntity().getContent());
            respon = (MyBean)in.readObject();
        } catch (Exception e) {
            Log.e("postVideo", e.getMessage());
            return false;
        }

        if(respon == null) {
            Log.e("postVideo", "MyBean response containing blobkey was null");
            return false;
        }
        if (respon.getInfo() == null || respon.getBlobKey() == null) {
            Log.e("postVideo", "MyBean response missing vital information");
            return false;
        }

        //file sent successfully, send the retrieval info to be stored with the post info
        String blobKey = respon.getBlobKey().toString();
        String servingUrl = respon.getInfo();
        Log.d("postVideo", "blobKey: " + blobKey + " servingUrl: " + servingUrl);

        response = null;
        try {
            response = myApiService.postVideo(userEmail, blobKey, servingUrl, post.getLat(), post.getLng()).execute();
        } catch (Exception e) {
            Log.e("postVideo", e.getMessage());
            return false;
        }
        if (response == null) {
            Log.e("postVideo", "MyBean response for posting serving info returned null");
            return false;
        }
        if (!response.getData()) {
            Log.e("postVideo", response.getInfo());
            return false;
        }

        return true;
    }
}
