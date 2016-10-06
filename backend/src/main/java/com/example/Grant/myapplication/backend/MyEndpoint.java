/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.example.Grant.myapplication.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import java.sql.Connection;
import java.util.ArrayList;

import javax.inject.Named;

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

        ArrayList<SQLStatements.ImageAttributeHolder> attributes = SQLStatements.getAttributes(conn);

        response.setData(attributes);
        return response;
    }

    @ApiMethod(name = "postImage")
    public MyBean postImage(@Named("picture") String picture, @Named("user") String user, @Named("lat") double lat, @Named("lon") double lon) {
        MyBean response = new MyBean();

        Connection conn = SQLStatements.createConnection();

        if (conn == null) {
            response.setInfo("Connection Failure in postImage");
            return response;
        }

        boolean posted = SQLStatements.postImage(conn, picture, user, lat, lon);

        response.setData(posted);
        return response;
    }

}
