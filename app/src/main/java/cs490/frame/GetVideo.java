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
import com.google.api.client.util.IOUtils;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Scott on 12/9/2016.
 */

public class GetVideo extends AsyncTask<String, Void, byte[]> {
    private static MyApi myApiService = null;
    private Context context;

    @Override
    protected byte[] doInBackground(String...params) {
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
        String keyString = params[0];
        String blobstring = keyString.substring(9, keyString.length()-1);
        Log.d("getVideo", "keyString: " + keyString);
        Log.d("getVideo", "blobstring: " + blobstring);

        List<NameValuePair> parameters = new LinkedList<NameValuePair>();
        parameters.add(new BasicNameValuePair("blob-key", blobstring));
        String paramString = URLEncodedUtils.format(parameters, "UTF-8");

        Log.d("getvideo", "paramString: " + paramString);

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("https://frame-145601.appspot.com/download?" + paramString);
        //httpGet.getParams().setParameter("blob-key", keyString);

        /*
        MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
        reqEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        reqEntity.addPart(Integer.toString(response.getPostID()), contentBody);
        */

        HttpResponse resp = null;
        byte[] ret = null;
        try {
            //Upload then read response which should be blobkey
            resp = httpClient.execute(httpGet);
            if (resp == null) {
                Log.e("postVideo", "response from upload servlet was null");
                return null;
            }
            int respCode = resp.getStatusLine().getStatusCode();
            Log.d("postVideo", "RESPONSE CODE: " + respCode);
            switch (respCode) {
                case 500:
                    Log.e("getVideo", "Download URL returned ERROR 500");
                    return null;

                case 404:
                    Log.e("getVideo", "Download URL returned ERROR 404");
                    return null;

                case 400:
                    Log.e("getVideo", "Download URL returned ERROR 404");
                    return null;

                case 204:
                    Log.e("getVideo", "Download URL returned ERROR 204");
                    return null;

                default:
                    InputStream in = resp.getEntity().getContent();
                    ret = ByteStreams.toByteArray(in);
            }
        } catch (MalformedURLException e) {
            Log.e("getVideo", "MalformedURLException Out: " + e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e("getVideo", "IOException Out: " + e.getMessage());
            return null;
        }

        return ret;

    }
}
