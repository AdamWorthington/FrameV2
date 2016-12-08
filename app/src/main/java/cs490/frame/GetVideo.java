package cs490.frame;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.grant.myapplication.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.appengine.repackaged.com.google.common.io.CharStreams;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Scott on 12/8/2016.
 */

public class GetVideo extends AsyncTask<Post, Void, byte[]> {
    private static MyApi myApiService = null;
    private Context context;

    @Override
    protected byte[] doInBackground(Post...params) {
        if (myApiService == null) {  // Only do this once
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
        byte[] ret = null;
        Post post = params[0];
        if (post == null) return null;
        if (post.getBlobkey() == null) return null;

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("https://frame-145601.appspot.com/_ah/download");
        httpGet.getParams().setParameter("blob-key", post.getBlobkey());

        HttpResponse resp;
        try {
            resp = httpClient.execute(httpGet);
            if (resp == null) {
                Log.e("getVideo", "response from download servlet was null");
                return null;
            }
            int respCode = resp.getStatusLine().getStatusCode();
            Log.d("getVideo", "Response Code: " + respCode);

            switch (respCode) {
                case 500:
                    HttpEntity entity = resp.getEntity();
                    if (entity != null) {
                        String responseBody = EntityUtils.toString(entity);
                        Log.e("getVideo", "Case 500: " + responseBody);
                        return null;
                    }
                    break;

                case 404:
                    HttpEntity entity1 = resp.getEntity();
                    if (entity1 != null) {
                        String responseBody = EntityUtils.toString(entity1);
                        Log.e("getVideo", "Case 404: " + responseBody);
                        return null;
                    }
                    break;

                default:
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    resp.getEntity().writeTo(baos);
                    ret = baos.toByteArray();

            }
        } catch (IOException e) {
            Log.e("getVideo", "failure to execute get statement: " + e.getMessage());
            return null;
        }



        return ret;
    }
}
