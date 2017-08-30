package com.example.android.javadevelopers;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.List;

/**
 * Created by onyekachukwu on 8/29/2017.
 */

public class DevelopersAdapter extends ArrayAdapter<Developers> {

    //Public Constructor
    public DevelopersAdapter(Context context, List<Developers> developers) {
        super(context, 0, developers);
    }

    //Overwrite the getview method and assign appropriate data to the TextViews
    //Returns a list view that displays info about the developer at the given position
    // in the list of developers
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //Check if there is  an existing list item view (called contentView) that we can reuse,
        //otherwise, if contentView is null, inflate a new list item layout
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.developer_item, parent, false);
        }

        //Find the earthquake at the given position in the list of earthquakes
        Developers currentDeveloper = getItem(position);

        // Find the individual textviews and link them

        //For the Username
        TextView usernameView = (TextView) listItemView.findViewById(R.id.developers_name);
        usernameView.setText(currentDeveloper.getUsername());

        //For the activity_profile Image
        //ImageView imageView = (ImageView) listItemView.findViewById(R.id.developers_image);
        Uri uri = Uri.parse(currentDeveloper.getImageUrl());
        SimpleDraweeView draweeView = (SimpleDraweeView) listItemView.findViewById(R.id.developers_image);
        draweeView.setImageURI(uri);

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor =  ContextCompat.getColor(getContext(), R.color.pix_bg);

        return listItemView;
    }
}
