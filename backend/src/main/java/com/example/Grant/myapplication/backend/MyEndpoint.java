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

    @ApiMethod(name = "postComment", httpMethod = ApiMethod.HttpMethod.POST)
    public MyBean postComment(Comment comment) {
        MyBean ret = new MyBean();

        Connection conn = SQLStatements.createConnection();

        if (conn == null) {
            ret.setInfo("Connection Failure in postComment");
            ret.setData(false);
            return ret;
        }

        ret.setData(SQLStatements.postComment(conn, comment.getPostID(), comment.getComment(), comment.getUser()));

        return ret;
    }

    @ApiMethod(name = "getComments", httpMethod = ApiMethod.HttpMethod.GET)
    public Comment getComments(@Named("postID")int postID) {
        Comment ret = new Comment();

        Connection conn = SQLStatements.createConnection();

        if (conn == null) {
            ret = null;
            return ret;
        }

        ArrayList<Comment> comments = SQLStatements.getComments(conn, postID);
        ret.setComments(comments);
        return ret;
    }

    @ApiMethod(name = "addToScrapbook", httpMethod = ApiMethod.HttpMethod.POST)
    public MyBean addToScrapbook(@Named("postID") int postID, @Named("user") String user) {
        MyBean ret = new MyBean();

        Connection conn = SQLStatements.createConnection();

        if (conn == null) {
            ret.setInfo("Connection Failure in getImage");
            ret.setData(false);
            return ret;
        }

        //TODO: Call database function here
        //ret.setData(SQLStatements.addToScrapbook(conn, postID, user));

        return ret;
    }

    @ApiMethod(name = "updateLikes", httpMethod = ApiMethod.HttpMethod.POST)
    public MyBean updateLikes(@Named("postID") int postID, @Named("likes") int likes) {
        MyBean ret = new MyBean();

        Connection conn = SQLStatements.createConnection();

        if (conn == null) {
            ret.setInfo("Connection Failure in getImage");
            ret.setData(false);
            return ret;
        }

        //TODO: Call database function here
        //ret.setData(SQLStatements.updateLikes(conn, postID, likes));

        return ret;
    }
}
