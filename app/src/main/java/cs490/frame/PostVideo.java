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
import com.google.api.client.util.IOUtils;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.repackaged.com.google.common.io.CharStreams;
import com.google.appengine.repackaged.org.codehaus.jackson.map.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.StringWriter;

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

        //Get the upload url
        MyBean response = null;
        try {
            response = myApiService.getBlobURL().execute();
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

        /* Tried making file out of URI, didnt work. Couldnt open due to ENOENT (No such file or directory)
        File video = new File(post.getVideoURI().getPath());
        Log.d("postVideo", "uri path: " + post.getVideoURI().getPath());
        FileBody fileBody = null;
        if (video != null )fileBody = new FileBody(video);
        else {
            Log.e("postVideo", "error creating video file");
            return false;
        }
        */


        ContentBody contentBody = new InputStreamBody(new ByteArrayInputStream(post.getVideo()), Integer.toString(response.getPostID()));

        MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
        reqEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        reqEntity.addPart(Integer.toString(response.getPostID()), contentBody);

        httpPost.setEntity(reqEntity.build());
        HttpResponse resp = null;
        String blobkey = null;
        try {
            resp = httpClient.execute(httpPost);
            if (resp == null) {
                Log.e("postVideo", "response from upload servlet was null");
                return false;
            }
            int respCode = resp.getStatusLine().getStatusCode();
            Log.d("postVideo", "Response Code: " + respCode);

            switch (respCode) {
                case 500:
                    HttpEntity entity = resp.getEntity();
                    if (entity != null) {
                        String responseBody = EntityUtils.toString(entity);
                        Log.e("postVideo", "Case 500: " + responseBody);
                        return false;
                    }
                    break;

                case 404:
                    HttpEntity entity1 = resp.getEntity();
                    if (entity1 != null) {
                        String responseBody = EntityUtils.toString(entity1);
                        Log.e("postVideo", "Case 404: " + responseBody);
                        return false;
                    }
                    break;

                default:
                    InputStream in = resp.getEntity().getContent();
                    blobkey = CharStreams.toString(new InputStreamReader(in));
                    Log.d("postVideo", "blobkey: " + blobkey);
            }
        } catch (IOException e) {
            Log.e("postVideo", "Err sending file to server: " + e.getMessage());
            return false;
        }

        if (blobkey == null) {
            Log.e("postVideo", "blobkey was null");
            return false;
        }

        //file sent successfully, send the retrieval info to be stored with the post info

        response = null;
        try {
            response = myApiService.postVideo(userEmail, blobkey, post.getLat(), post.getLng()).execute();
        } catch (Exception e) {
            Log.e("postVideo", "post video failed: " + e.getMessage());
            return false;
        }
        if (response == null) {
            Log.e("postVideo", "MyBean response for posting serving info returned null");
            return false;
        }
        if (response.getData() == false) {
            Log.e("postVideo", "response was false: " + response.getInfo());
            return false;
        }

        return true;
    }
}
