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

import java.util.ArrayList;

public class DisplayImageActivity extends AppCompatActivity {
    ArrayList<Comment> comments;
    CommentsAdapter adapter;
    boolean hasUpvoted = false;
    boolean hasDownvoted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);

        Bitmap image = null;
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
    }

    private void sendComment()
    {
        EditText textComment = (EditText) findViewById(R.id.commentText);
        String comment = textComment.getText().toString();
        if(comment.isEmpty())
            return;

        comments.add(new Comment(-1, "user", comment));
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
            TextView downvoteCount = (TextView) findViewById(R.id.downVoteCount);
            int down = Integer.parseInt(downvoteCount.getText().toString());
            down--;
            downvoteCount.setText(Integer.toString(down));
            hasDownvoted = false;
        }
    }

    private void sendDownvote()
    {
        if(hasDownvoted)
            return;
        else
            hasDownvoted = true;

        TextView downvoteCount = (TextView) findViewById(R.id.downVoteCount);
        int current = Integer.parseInt(downvoteCount.getText().toString());
        current++;
        downvoteCount.setText(Integer.toString(current));

        if(hasUpvoted)
        {
            TextView upvoteCount = (TextView) findViewById(R.id.upvoteCount);
            int up = Integer.parseInt(upvoteCount.getText().toString());
            up--;
            upvoteCount.setText(Integer.toString(up));
            hasUpvoted = false;
        }
    }


}
