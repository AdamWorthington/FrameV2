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
import com.google.common.io.CharStreams;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;

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
        Post post = new Post();
        post = params[0];

        //Get the upload url from the api
        MyBean response = null;
        try {
             response = myApiService.getBlobURL().execute();
        } catch (IOException e) {
            Log.i("postVideo", "IOException occured~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            Log.e("postVideo", e.getMessage());
            return false;
        }
        Log.d("postVideo", "upload URL: " + response.getInfo());

        //Upload video byte array to the upload url to be stored in blob store
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(response.getInfo());

        ContentBody contentBody = new InputStreamBody(new ByteArrayInputStream(post.getVideo()), Integer.toString(response.getPostID()));

        MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
        reqEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        reqEntity.addPart(Integer.toString(response.getPostID()), contentBody);

        httpPost.setEntity(reqEntity.build());

        HttpResponse resp = null;
        String blobkey = null;
        try {
            //Upload then read response which should be blobkey
            resp = httpClient.execute(httpPost);
            if (resp == null) {
                Log.e("postVideo", "response from upload servlet was null");
                return false;
            }
            int respCode = resp.getStatusLine().getStatusCode();
            Log.d("postVideo", "RESPONSEE CODE: " + respCode);
            switch (respCode) {
                case 500:
                    Log.e("postVideo", "Upload URL returned ERROR 500");
                    return false;

                case 404:
                    Log.e("postVideo", "Upload URL returned ERROR 404");
                    return false;

                case 400:
                    Log.e("postVideo", "Upload URL returned ERROR 404");
                    return false;

                default:
                    InputStream in = resp.getEntity().getContent();
                    blobkey = CharStreams.toString(new InputStreamReader(in));
            }
        } catch (MalformedURLException e) {
            Log.e("postVideo", "MalformedURLException Out: " + e.getMessage());
            return false;
        } catch (IOException e) {
            Log.e("postVideo", "IOException Out: " + e.getMessage());
            return false;
        }
        if (blobkey == null) {
            Log.e("postVideo", "blobkey was null");
            return false;
        }

        //Upload success, received blobkey; redirect back at API
        Log.d("postVideo", "blobkey: " + blobkey);

        //File successfully uploaded and blobkey successfully retrieved, store the new info as a post
        MyBean ret = new MyBean();
        ret.setData(false);
        try {
            if (post.getCaption() != null) ret = myApiService.postVideo(post.getUser(), blobkey, post.getLat(), post.getLng()).setCaption(post.getCaption()).execute();
            else ret = myApiService.postVideo(post.getUser(), blobkey, post.getLat(), post.getLng()).execute();
        } catch (IOException e) {
            Log.i("postVideo", "IOException occured~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            Log.e("postVideo", "IOException postVideo return: " + e.getMessage());
            return false;
        }
        return ret.getData();
    }
}
