package cs490.frame;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grant.myapplication.backend.myApi.model.Comment;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;

import static cs490.frame.LoginActivity.userEmail;
import static cs490.frame.WorldController.curCaption;
import static cs490.frame.WorldController.curLikes;
import static cs490.frame.WorldController.curPost;

public class DisplayImageActivity extends AppCompatActivity {
    ArrayList<Comment> comments;
    CommentsAdapter adapter;
    boolean hasUpvoted = false;
    boolean hasDownvoted = false;
    boolean hasFavorited = false;
    Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);

        image = null;
        String picture = WorldController.showPicture;

        if (picture != null) {
            image = ImageConverter.decodeBase64(picture);
        }

        ImageView view = (ImageView) findViewById(R.id.imageView);
        if (image != null) {
            Matrix matrix = new Matrix();
            if(Build.MANUFACTURER.compareTo("motorola") != 0)
                matrix.postRotate(90);
            Bitmap rotatedBitmap = Bitmap.createBitmap(image , 0, 0, image.getWidth(), image.getHeight(), matrix, true);
            view.setImageBitmap(rotatedBitmap);
        }

        comments = new ArrayList<>();
        ListView commentList = (ListView) findViewById(R.id.commentsList);
        adapter = new CommentsAdapter(this, comments);
        commentList.setAdapter(adapter);

        /*
        TextView upvotes = (TextView) findViewById(R.id.upvoteCount);
        upvotes.setText(curLikes);
        TextView caption = (TextView) findViewById(R.id.caption);
        caption.setText(curCaption);
        */

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
    }

    private void sendFavorite()
    {
        if(hasFavorited)
            return;

        hasFavorited = true;

        Date d = new Date();
        String fileName = d.toString().concat(".jpeg");

        File dir = new File(this.getFilesDir(), "saved");
        if(!dir.exists())
            dir.mkdir();
        File file = new File(dir, fileName);

        try {
            FileOutputStream out = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 100, out);
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
        comments.add(add);
        textComment.clearComposingText();
        if(adapter != null)
            adapter.notifyDataSetChanged();

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

        if(hasUpvoted)
        {
            hasUpvoted = false;
        }
    }


}
