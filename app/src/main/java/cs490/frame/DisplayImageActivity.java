package cs490.frame;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.grant.myapplication.backend.myApi.model.Comment;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import static cs490.frame.LoginActivity.userEmail;
import static cs490.frame.WorldController.curCaption;
import static cs490.frame.WorldController.curLikes;
import static cs490.frame.WorldController.curPost;

public class DisplayImageActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener {
    ArrayList<Comment> comments;
    CommentsAdapter adapter;
    boolean hasUpvoted = false;
    boolean hasDownvoted = false;
    boolean hasFavorited = false;
    boolean isPhoto = true;
    Bitmap image;
    File video;

    private MediaPlayer mMediaPlayer;

    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Surface s = new Surface(surface);

        try {
            mMediaPlayer= new MediaPlayer();
            mMediaPlayer.setDataSource(video.getAbsolutePath());
            mMediaPlayer.setSurface(s);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed()
    {
        if(mMediaPlayer != null)
            mMediaPlayer.release();

        super.onBackPressed();
    }


    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);

        TextureView vFrame = (TextureView) findViewById(R.id.videoView);
        ImageView view = (ImageView) findViewById(R.id.imageView);

        Bundle extras = getIntent().getExtras();
        String format = extras.getString("format");
        if(format.compareTo("video") == 0)
        {
            vFrame.setSurfaceTextureListener(this);
            vFrame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mMediaPlayer.isPlaying())
                        mMediaPlayer.pause();
                    else
                        mMediaPlayer.start();
                }
            });
            File fil = (File)extras.getSerializable("videoFile");
            video = fil;
            vFrame.setVisibility(View.VISIBLE);
            view.setVisibility(View.GONE);
            isPhoto = false;
        }
        else
        {
            isPhoto = true;
            String picture = WorldController.showPicture;
            if (picture != null) {
                image = ImageConverter.decodeBase64(picture);
            }

            if (image != null) {
                Matrix matrix = new Matrix();
                if(Build.MANUFACTURER.compareTo("motorola") != 0)
                    matrix.postRotate(90);
                Bitmap rotatedBitmap = Bitmap.createBitmap(image , 0, 0, image.getWidth(), image.getHeight(), matrix, true);
                view.setImageBitmap(rotatedBitmap);
            }

            vFrame.setVisibility(View.GONE);
            view.setVisibility(View.VISIBLE);
        }

        image = null;

        String caption = WorldController.curCaption;

        TextView captionText = (TextView) findViewById(R.id.caption);
        if(caption != null)
            captionText.setText(caption);

        comments = new ArrayList<>();
        ListView commentList = (ListView) findViewById(R.id.commentsList);

        try {
            Comment cc = new GetComments().execute(WorldController.curPost).get();
            if(cc != null)
                comments.addAll(cc.getComments());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        adapter = new CommentsAdapter(this, comments);
        commentList.setAdapter(adapter);
        setListViewHeightBasedOnChildren(commentList);

        Log.d("displayActivity", "curCaption: " + curCaption);

        ImageButton upvote = (ImageButton) findViewById(R.id.upvoteButton);
        upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUpvote();
            }
        });

        ImageButton downvote = (ImageButton) findViewById(R.id.downvoteButton);
        downvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendDownvote();
            }
        });

        Button commentButton = (Button) findViewById(R.id.postCommentButton);
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendComment();
            }
        });

        ImageButton favoriteButton = (ImageButton) findViewById(R.id.favoriteButton);
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendFavorite();
            }
        });

        TextView upvoteCount = (TextView) findViewById(R.id.upvoteCount);
        upvoteCount.setText(Integer.toString(WorldController.curLikes));
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    private void sendFavorite()
    {
        if(hasFavorited)
            return;

        hasFavorited = true;

        Date d = new Date();
        String fileName = d.toString();
        if(isPhoto)
            fileName = fileName.concat(".jpeg");
        else
            fileName = fileName.concat(".mp4");

        File dir = new File(this.getFilesDir(), "saved");
        if(!dir.exists())
            dir.mkdir();
        File file = new File(dir, fileName);
        file.setReadable(true, false);

        try {
            FileOutputStream out = new FileOutputStream(file);
            if(isPhoto)
                image.compress(Bitmap.CompressFormat.JPEG, 100, out);
            else
            {
                FileInputStream input = new FileInputStream(video);
                IOUtils.copy(input, out);

                input.close();
            }
            out.flush();
            out.close();
            Toast.makeText(this, "Added to Scrapbook", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendComment()
    {
        EditText textComment = (EditText) findViewById(R.id.commentText);
        String comment = textComment.getText().toString();
        if(comment.isEmpty())
            return;

        Comment add = new Comment();
        add.setPostID(curPost);
        add.setComment(comment);
        add.setUser(userEmail);
        try {
            boolean success = new PostComment().execute(add).get();
        } catch (InterruptedException e) {
            Log.e("displayActivity", "InterruptedException: "+e.getMessage());
        } catch (ExecutionException e) {
            Log.e("displayActivity", "ExecutionException: "+e.getMessage());
        }
        comments.add(add);
        textComment.clearComposingText();
        if(adapter != null) {
            adapter.notifyDataSetChanged();
            ListView commentList = (ListView) findViewById(R.id.commentsList);
            setListViewHeightBasedOnChildren(commentList);
        }
    }

    private void sendUpvote()
    {
        if(hasUpvoted)
            return;
        else
            hasUpvoted = true;

        TextView upvoteCount = (TextView) findViewById(R.id.upvoteCount);
        int current = Integer.parseInt(upvoteCount.getText().toString());
        current++;
        upvoteCount.setText(Integer.toString(current));
        Post p = new Post();
        p.setPostID(WorldController.curPost);
        p.setLikes(current);
        new UpdateLikes().execute(p);

        if(hasDownvoted)
        {
            hasDownvoted = false;
        }
    }

    private void sendDownvote()
    {
        if(hasDownvoted)
            return;
        else
            hasDownvoted = true;

        TextView downvoteCount = (TextView) findViewById(R.id.upvoteCount);
        int current = Integer.parseInt(downvoteCount.getText().toString());
        current--;
        downvoteCount.setText(Integer.toString(current));
        Post p = new Post();
        p.setPostID(WorldController.curPost);
        p.setLikes(current);
        new UpdateLikes().execute(p);

        if(hasUpvoted)
        {
            hasUpvoted = false;
        }
    }


}
