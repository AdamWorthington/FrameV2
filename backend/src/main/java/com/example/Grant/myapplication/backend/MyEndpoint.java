/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.example.Grant.myapplication.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "myApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.myapplication.Grant.example.com",
                ownerName = "backend.myapplication.Grant.example.com",
                packagePath = ""
        )
)
public class MyEndpoint {

    /**
     * A simple endpoint method that takes a name and says Hi back
     */

    private final GcsService gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
            .initialRetryDelayMillis(10)
            .retryMaxAttempts(10)
            .totalRetryPeriodMillis(15000)
            .build());

    /**Used below to determine the size of chucks to read in. Should be > 1kb and < 10MB */
    private static final int BUFFER_SIZE = 2 * 1024 * 1024;

    @ApiMethod(name = "getImage")
    public ImageBean getImage(@Named("id") int id) {
        ImageBean response = new ImageBean();

        Connection conn = SQLStatements.createConnection();

        if (conn == null) {
            response.setInfo("Connection Failure in getImage");
            return response;
        }

        String image = SQLStatements.getImage(conn, id);

        response.setData(image);
        return response;
    }

    @ApiMethod(name = "getAttributes")
    public IAHBean getAttributes() {
        IAHBean response = new IAHBean();

        Connection conn = SQLStatements.createConnection();

        if (conn == null) {
            response.setInfo("Connection Failure in getAttributes");
            return response;
        }

        ArrayList<ImageAttributeHolder> attributes = SQLStatements.getAttributes(conn);

        response.setData(attributes);
        return response;
    }

    @ApiMethod(name = "postImage", httpMethod= ApiMethod.HttpMethod.POST)
    public MyBean postImage(ImageBean picture, @Named("user") String user, @Named("lat") double lat, @Named("lon") double lon) {
        MyBean response = new MyBean();

        Connection conn = SQLStatements.createConnection();

        if (conn == null) {
            response.setInfo("Connection Failure in postImage");
            return response;
        }

        boolean posted = SQLStatements.postImage(conn, picture.getData(), user, lat, lon);

        response.setData(posted);
        return response;
    }

    @ApiMethod(name = "postVideo", httpMethod= ApiMethod.HttpMethod.POST)
    public MyBean postVideo(VideoBean video, @Named("user") String user, @Named("lat") double lat, @Named("lon") double lon) {
        MyBean response = new MyBean();

        Connection conn = SQLStatements.createConnection();

        if (conn == null) {
            response.setInfo("Connection Failure in postImage");
            return response;
        }

        boolean posted = true;// = SQLStatements.postImage(conn, "video.getData()", user, lat, lon);

        response.setData(posted);
        return response;
    }


    /*
    //    public void postVideo(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    @ApiMethod(name = "postVideo", httpMethod= ApiMethod.HttpMethod.POST)
    public MyBean postVideo(VideoBean video, @Named("user") String user, @Named("lat") double lat, @Named("lon") double lon) throws IOException, SQLException {

        MyBean response = new MyBean();
        GcsFileOptions instance = GcsFileOptions.getDefaultInstance();
        GcsFilename fileName = new GcsFilename("frame-145601.appspot.com", "Bob Saget");//getFileName(req);
        GcsOutputChannel outputChannel;
        outputChannel = gcsService.createOrReplace(fileName, instance);
        copy(video.getData().getBinaryStream(), Channels.newOutputStream(outputChannel));
        return response;
    }

    private GcsFilename getFileName(HttpServletRequest req) {
        String[] splits = req.getRequestURI().split("/", 4);
        if (!splits[0].equals("") || !splits[1].equals("gcs")) {
            throw new IllegalArgumentException("The URL is not formed as expected. " +
                    "Expecting /gcs/<bucket>/<object>");
        }
        return new GcsFilename(splits[2], splits[3]);
    }


    private void copy(InputStream input, OutputStream output) throws IOException {
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = input.read(buffer);
            while (bytesRead != -1) {
                output.write(buffer, 0, bytesRead);
                bytesRead = input.read(buffer);
            }
        } finally {
            input.close();
            output.close();
        }
    }
    */
}
