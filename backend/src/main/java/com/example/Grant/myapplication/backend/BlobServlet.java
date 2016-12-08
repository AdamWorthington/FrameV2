package com.example.Grant.myapplication.backend;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.repackaged.com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Scott on 12/8/2016.
 */

public class BlobServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        BlobstoreService bsService = BlobstoreServiceFactory.getBlobstoreService();
        List<BlobKey> blobs = bsService.getUploads(req).get("video");

        BlobKey blobKey = blobs.get(0);

        ImagesService imagesService = ImagesServiceFactory.getImagesService();
        ServingUrlOptions servingOptions = ServingUrlOptions.Builder.withBlobKey(blobKey);

        String servingUrl = imagesService.getServingUrl(servingOptions);

        res.setStatus(HttpServletResponse.SC_OK);
        res.setContentType("application/json");

        MyBean ret = new MyBean();
        ret.setInfo(servingUrl);
        ret.setBlobKey(blobKey);

        PrintWriter out = res.getWriter();
        out.print(new Gson().toJson(ret));
        out.flush();
        out.close();
    }
}
