package com.example.Grant.myapplication.backend;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesServiceFailureException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Scott on 12/9/2016.
 */

public class BlobDownload extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ImagesServiceFailureException {
        String keyString = req.getParameter("blob-key");
        if (keyString == null);

        BlobstoreService bsService = BlobstoreServiceFactory.getBlobstoreService();
        BlobKey blobKey = new BlobKey(keyString);
        log("blobkey: " + blobKey.toString());
        bsService.serve(blobKey, res);
    }
}
