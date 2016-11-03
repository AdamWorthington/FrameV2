package cs490.frame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.example.grant.myapplication.backend.myApi.MyApi;
import com.example.grant.myapplication.backend.myApi.model.VideoBean;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.model.StorageObject;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.blobstore.UploadOptions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by Scott on 10/6/2016.
 */

public class PostVideo extends AsyncTask<Post, Void, Boolean> {
    private static MyApi myApiService = null;
    private Context context;

    private static Storage getService() throws IOException, GeneralSecurityException {
        HttpTransport httpTransport = new com.google.api.client.http.javanet.NetHttpTransport();
        return new Storage.Builder(httpTransport, JacksonFactory.getDefaultInstance(), null)
                .setApplicationName("frame-145601")
                .build();
    }

    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    @Override
    protected Boolean doInBackground(Post...params) {
        //Bitmap bmp = BitmapFactory.decodeFile(imagePath);
        Post post = params[0];
        String bucket = "frame-145601.appspot.com";
        String objectName = "bobsaget";
        String contentType = "text/plain";
        byte[] bytes = post.getVideo();
        InputStreamContent contentStream =
                new InputStreamContent(contentType, new ByteArrayInputStream(bytes));
        StorageObject objectMetadata = new StorageObject().setName(objectName);
        Storage.Objects.Insert insertRequest =
                null;

        try {
            insertRequest = getService().objects().insert(bucket, objectMetadata, contentStream).setOauthToken("882154403463-69n6b5r4heikmica94shlp2hqqv81ku5.apps.googleusercontent.com");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

        try {
            insertRequest.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }


//        UploadOptions uploadOptions = UploadOptions.Builder
//                .withGoogleStorageBucketName("frame-145601.appspot.com")
//                .maxUploadSizeBytes(1048576);
//        String blobUploadUrl = blobstoreService.createUploadUrl("/upload",
//                uploadOptions);

//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        bmp.compress(Bitmap.CompressFormat.JPEG, 75, out);
//        byte[] imgByte = out.toByteArray();
//        String encodedImage = Base64.encodeToString(imgByte,
//                Base64.DEFAULT);
//
//        HttpClient httpClient = new DefaultHttpClient();
//        HttpPost httpPost = new HttpPost(
//                "https://frame-145601.appspot.com/_ah/api/myApi/v1/postVideo");
//        org.apache.http.HttpResponse response = null;
//        try {
//            response = httpClient.execute(httpPost);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        HttpEntity urlEntity = response.getEntity();
//        InputStream in = null;
//        try {
//            in = urlEntity.getContent();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String str = "";
//        while (true) {
//            int ch = 0;
//            try {
//                ch = in.read();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            if (ch == -1)
//                break;
//            str += (char) ch;
//        }




//        if(myApiService == null) {  // Only do this once
//            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
//                    new AndroidJsonFactory(), null)
//                    .setRootUrl("https://frame-145601.appspot.com/_ah/api/")
//                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
//                        @Override
//                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
//                            abstractGoogleClientRequest.setDisableGZipContent(true);
//                        }
//                    });
//
//            myApiService = builder.build();
//        }
//        Post post = new Post();
//        post = params[0];
//        Log.i("postImage", "Post Lat: " + post.getLat() + " Lng: " + post.getLng() + " user: " + post.getUser());
//
//        VideoBean video = new VideoBean();
//        video.setVideo(post.getPicture());
//
//
//        try {
//            return myApiService.postVideo(post.getUser(), post.getLat(), post.getLng(), video).execute().getData();
//        } catch (IOException e) {
//            Log.i("postImage", "IOException occured~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
//            Log.e("postImage", e.getMessage());
//            return false;
//        }
        return true;
    }
}
