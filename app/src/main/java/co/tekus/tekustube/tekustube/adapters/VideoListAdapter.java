package co.tekus.tekustube.tekustube.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import co.tekus.tekustube.tekustube.R;
import co.tekus.tekustube.tekustube.models.Video;

/**
 * Created by wrmej on 27/01/2018.
 */

public class VideoListAdapter extends ArrayAdapter<Video> {
    private Context context;
    private List<Video> videosList;

    public VideoListAdapter(Context context, List<Video> videos) {
        super(context, R.layout.item_video_list, videos);
        this.context = context;
        videosList = videos;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View item = convertView;
        ViewHolder holder;

        if (item == null) {
            item = View.inflate(context, R.layout.item_video_list, null);

            holder = new ViewHolder();
            holder.nameVideo = item.findViewById(R.id.nameVideo);
            holder.durationVideo = item.findViewById(R.id.durationVideo);

            item.setTag(holder);
        } else {
            holder = (ViewHolder) item.getTag();
        }

        holder.nameVideo.setText(videosList.get(position).getNameVideo());
        holder.durationVideo.setText(videosList.get(position).getDuration());

        return (item);
    }

    static class ViewHolder {
        TextView nameVideo, durationVideo;
    }
}