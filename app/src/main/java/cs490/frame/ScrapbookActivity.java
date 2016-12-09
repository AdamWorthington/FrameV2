package cs490.frame;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScrapbookActivity extends AppCompatActivity {

    private ScrapbookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrapbook);

        ArrayList<Bitmap> favorited = new ArrayList<Bitmap>();

        File file = new File(this.getFilesDir(), "saved");
        if(!file.exists())
        {
            file.mkdir();
        }
        File[] fils = file.listFiles();
        ArrayList<File> _fils = new ArrayList<File>(Arrays.asList(fils));

        GridView grid = (GridView) findViewById(R.id.gridview);
        adapter = new ScrapbookAdapter(this, _fils);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                File f = adapter.getItem(i);
                Intent viewActivity = new Intent(ScrapbookActivity.this, ScrapbookViewActivity.class);
                if(f.getAbsolutePath().endsWith(".jpeg")) {
                    viewActivity.putExtra("path", f.getAbsolutePath());
                    viewActivity.putExtra("format", "photo");

                }
                else {
                    viewActivity.putExtra("file", f);
                    viewActivity.putExtra("format", "video");
                }
                ScrapbookActivity.this.startActivity(viewActivity);
            }
        });

        TextView empty = (TextView) findViewById(R.id.emptyScrapbook);
        if(adapter.getCount() == 0)
        {
            empty.setVisibility(View.VISIBLE);
            grid.setVisibility(View.GONE);
        }
        else {
            empty.setVisibility(View.GONE);
            grid.setVisibility(View.VISIBLE);
        }
    }
}
