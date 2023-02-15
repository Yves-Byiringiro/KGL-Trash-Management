package com.kgltrash.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kgltrash.R;
import com.kgltrash.model.NotificationView;

import java.util.ArrayList;

/**
 * Grace Tcheukounang
 */

public class NotificationViewAdapter extends ArrayAdapter<NotificationView> {

    public NotificationViewAdapter(@NonNull Context context, ArrayList<NotificationView> arrayList){
        super(context, 0, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View currentItemView = convertView;

        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.view_list_notification, parent, false);
        }
        // get the position of the view from the ArrayAdapter
        NotificationView currentNumberPosition = getItem(position);

        TextView title = currentItemView.findViewById(R.id.title);
        title.setText(currentNumberPosition.getTitle());

        TextView description = currentItemView.findViewById(R.id.description);
        description.setText(currentNumberPosition.getDescription());

        TextView date = currentItemView.findViewById(R.id.date);
        date.setText(currentNumberPosition.getDate());

        // then return the recyclable view
        return currentItemView;

    }

}
