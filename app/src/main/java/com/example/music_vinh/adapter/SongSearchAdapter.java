package com.example.music_vinh.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.music_vinh.R;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.view.impl.MainActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SongSearchAdapter extends ArrayAdapter<Song> {
    private List<Song> songList;
    Context context;

    public SongSearchAdapter(@NonNull Context context, @NonNull List<Song> songs) {
        super(context, 0, songs);
        this.context = context;
        this.songList = songs;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return countryFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_song, parent, false
            );
        }

        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvNameArtist = convertView.findViewById(R.id.tvNameArtist);
        ImageView imgSong = convertView.findViewById(R.id.imgSong);

        Song songItem= getItem(position);

        if (songItem != null) {
            tvName.setText(songItem.getName());
            tvNameArtist.setText(songItem.getNameArtist());
            imgSong.setImageResource(R.drawable.ic_song);
        }

        return convertView;
    }

    private Filter countryFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<Song> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(songList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Song item : songList) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        suggestions.add(item);
                    }
                }
            }
            results.values = suggestions;
            results.count = suggestions.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Song) resultValue).getName();
        }
    };

    public void getFilte(List<Song> listItem){
        songList = new ArrayList<>();
        songList.addAll(listItem);
        notifyDataSetChanged();
    }

}
