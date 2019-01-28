package com.example.music_vinh.SearchView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.music_vinh.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyAppAdapter extends BaseAdapter {

    public class ViewHolder {
        TextView txtTitle, txtSubTitle;
    }

    public List<Post> parkingList;

    public Context context;
    ArrayList<Post> arraylist;

    public MyAppAdapter(List<Post> apps, Context context) {
        this.parkingList = apps;
        this.context = context;
        arraylist = new ArrayList<Post>();
        arraylist.addAll(parkingList);

    }

    @Override
    public int getCount() {
        return parkingList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        ViewHolder viewHolder;

        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            rowView = inflater.inflate(R.layout.item_single, null);
            // configure view holder
            viewHolder = new ViewHolder();
            viewHolder.txtTitle = (TextView) rowView.findViewById(R.id.title);
            viewHolder.txtSubTitle = (TextView) rowView.findViewById(R.id.subtitle);
            rowView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtTitle.setText(parkingList.get(position).getPostTitle() + "");
        viewHolder.txtSubTitle.setText(parkingList.get(position).getPostSubTitle() + "");
        return rowView;


    }
    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        parkingList.clear();
        if (charText.length() == 0) {
            parkingList.addAll(arraylist);

        } else {
            for (Post postDetail : arraylist) {
                if (charText.length() != 0 && postDetail.getPostTitle().toLowerCase(Locale.getDefault()).contains(charText)) {
                    parkingList.add(postDetail);
                } else if (charText.length() != 0 && postDetail.getPostSubTitle().toLowerCase(Locale.getDefault()).contains(charText)) {
                    parkingList.add(postDetail);
                }
            }
        }
        notifyDataSetChanged();
    }
}

