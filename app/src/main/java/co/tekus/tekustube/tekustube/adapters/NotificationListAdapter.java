package co.tekus.tekustube.tekustube.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import co.tekus.tekustube.tekustube.R;
import co.tekus.tekustube.tekustube.models.Notification;

/**
 * Created by wrmej on 27/01/2018.
 */

public class NotificationListAdapter extends ArrayAdapter<Notification> {
    private Context context;
    private List<Notification> notificationList;

    public NotificationListAdapter(Context context, List<Notification> notifications) {
        super(context, R.layout.item_notification_list, notifications);
        this.context = context;
        notificationList = notifications;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View item = convertView;
        ViewHolder holder;

        if (item == null) {
            item = View.inflate(context, R.layout.item_notification_list, null);

            holder = new ViewHolder();
            holder.idNotification = item.findViewById(R.id.idNotification);
            holder.dateNotification = item.findViewById(R.id.dateNotification);
            holder.durationNotification = item.findViewById(R.id.durationNotification);

            item.setTag(holder);
        } else {
            holder = (ViewHolder) item.getTag();
        }

        holder.idNotification.setText(String.valueOf(notificationList.get(position).getNotificationId()));
        holder.dateNotification.setText(notificationList.get(position).getDate());
        holder.durationNotification.setText(String.valueOf(notificationList.get(position).getDuration()));

        return (item);
    }

    static class ViewHolder {
        TextView idNotification, dateNotification, durationNotification;
    }
}