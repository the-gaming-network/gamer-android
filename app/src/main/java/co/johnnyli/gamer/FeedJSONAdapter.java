package co.johnnyli.gamer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

public class FeedJSONAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater mInflater;
    JSONArray mJSONArray;

    public FeedJSONAdapter(Context context, LayoutInflater inflater) {
        mContext = context;
        mInflater = inflater;
        mJSONArray = new JSONArray();
    }

    @Override
    public int getCount() {
        return mJSONArray.length();
    }

    @Override
    public Object getItem(int position) {
        return mJSONArray.optJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.feed_list_item, null);
            holder = new ViewHolder();
            holder.avatarView = (ImageView) convertView.findViewById(R.id.user_img);
            holder.authorTextView = (TextView) convertView.findViewById(R.id.author);
            holder.postTextView = (TextView) convertView.findViewById(R.id.post_content);
            holder.commentTextView = (TextView) convertView.findViewById(R.id.comments_likes);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        JSONObject jsonObject = (JSONObject) getItem(position);
        if (jsonObject.has("owner_profile_image")) {
            Picasso.with(mContext).load(jsonObject.optString("owner_profile_image")).into(holder.avatarView);
        } else {
            holder.avatarView.setVisibility(View.GONE);
        }
        String groupName;
        if (jsonObject.has("group_name")) {
            groupName = jsonObject.optString("owner_name") + " in " +
                    jsonObject.optString("group_name");
        } else {
            groupName= jsonObject.optString("owner_name");
        }
        String postContent = jsonObject.optString("text");
        if (jsonObject.has("like_count")) {
            String commentLike = "Comments: " + jsonObject.optString("comment_count")
                    + "            Likes: " + jsonObject.optString("like_count");
            holder.commentTextView.setText(commentLike);

        } else {
            holder.commentTextView.setVisibility(View.GONE);
        }
        holder.authorTextView.setText(groupName);
        holder.postTextView.setText(postContent);
        return convertView;

    }

    private static class ViewHolder {
        public ImageView avatarView;
        public TextView authorTextView;
        public TextView postTextView;
        public TextView commentTextView;
    }

    public void updateData(JSONArray jsonArray) {
        mJSONArray = jsonArray;
        notifyDataSetChanged();
    }
}
