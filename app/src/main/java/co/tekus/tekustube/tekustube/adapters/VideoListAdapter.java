package co.tekus.tekustube.tekustube.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import co.tekus.tekustube.tekustube.MainActivity;
import co.tekus.tekustube.tekustube.R;
import co.tekus.tekustube.tekustube.database.DatabaseHelper;
import co.tekus.tekustube.tekustube.database.querys.VideoQuery;
import co.tekus.tekustube.tekustube.models.Video;
import co.tekus.tekustube.tekustube.tekusdialogs.TekusDialogOnClick;
import co.tekus.tekustube.tekustube.tekusdialogs.TekusInfoDialog;

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

    public View getView(final int position, View convertView, ViewGroup parent) {
        View item = convertView;
        ViewHolder holder;

        if (item == null) {
            item = View.inflate(context, R.layout.item_video_list, null);

            holder = new ViewHolder();
            holder.nameVideo = item.findViewById(R.id.nameVideo);
            holder.durationVideo = item.findViewById(R.id.durationVideo);
            holder.optionDelete = item.findViewById(R.id.optionDelete);

            item.setTag(holder);
        } else {
            holder = (ViewHolder) item.getTag();
        }

        holder.nameVideo.setText(videosList.get(position).getNameVideo());
        holder.durationVideo.setText(videosList.get(position).getDuration());
        holder.optionDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmDialog(videosList.get(position).getDir());
            }
        });

        return (item);
    }

    static class ViewHolder {
        TextView nameVideo, durationVideo;
        ImageView optionDelete;
    }

    public void showDeleteConfirmDialog(final String localDirOfVideo){
        TekusInfoDialog tekusInfoDialog = new TekusInfoDialog(context);
        tekusInfoDialog.setTitle(context.getString(R.string.app_name));
        tekusInfoDialog.setMessage(context.getString(R.string.remove_video_from_device));
        tekusInfoDialog.setPositiveButton(context.getString(R.string.txt_yes), new TekusDialogOnClick() {
            @Override
            public void onClick(View view) {
                File file = new File(localDirOfVideo);
                if (file.exists())
                    file.delete();

                new VideoQuery().delete(new DatabaseHelper(context).getWritableDatabase(), localDirOfVideo);

                ((MainActivity)context).refreshLayout();
            }
        });
        tekusInfoDialog.setNegativeButton(context.getString(R.string.txt_no), null);
        tekusInfoDialog.show();
    }
}