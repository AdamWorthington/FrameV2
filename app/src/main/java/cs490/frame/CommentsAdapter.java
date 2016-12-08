package cs490.frame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.ArrayList;

/**
 * Created by cwilh on 12/5/2016.
 */
public class CommentsAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Comment> mDataSource;

    public CommentsAdapter(Context context, ArrayList<Comment> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public Comment getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        if(convertView == null)
        {
            convertView = mInflater.inflate(R.layout.list_item_comment, parent, false);
        }
        Comment c = getItem(position);
        TextView tvName = (TextView) convertView.findViewById(R.id.commentId);
        TextView tvContent = (TextView) convertView.findViewById(R.id.commentContent);
        tvName.setText(c.getUser());
        tvContent.setText(c.getComment());

        return convertView;
    }
}
