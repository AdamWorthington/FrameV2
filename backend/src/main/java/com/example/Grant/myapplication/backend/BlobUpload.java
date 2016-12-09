package com.example.Grant.myapplication.backend;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesServiceFailureException;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Scott on 12/8/2016.
 */

public class BlobUpload extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ImagesServiceFailureException {
        BlobstoreService bsService = BlobstoreServiceFactory.getBlobstoreService();
        Map<String, List<BlobKey>> blobs = bsService.getUploads(req);

        List<BlobKey> blobKeys = blobs.get(Integer.toString(MyEndpoint.i-1));

        String ret = blobKeys.get(0).toString();
        log("blobkey: " + ret);
        if (res == null) log("RESPONE OBJECT IS NULL");
        else {
            res.setStatus(HttpServletResponse.SC_OK);
            res.setContentType("application/json");
        }

        PrintWriter out = res.getWriter();
        out.print(ret);
        out.flush();
        out.close();
    }
}
