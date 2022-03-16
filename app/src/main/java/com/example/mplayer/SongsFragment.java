package com.example.mplayer;

import static com.example.mplayer.MainActivity.musicFiles;
//import static com.example.mplayer.MainActivity.mysongs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SongsFragment extends Fragment {
    RecyclerView recyclerView;
    MusicAdapter musicAdapter;


    public SongsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_songs, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
//        recyclerView.setHasFixedSize(true);
//        musicAdapter = new MusicAdapter(getContext(),musicFiles);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),recyclerView.VERTICAL,
//                    false));
//        recyclerView.setAdapter(musicAdapter);
        if (!(musicFiles.size() < 1)){
            musicAdapter = new MusicAdapter(getContext(),musicFiles);
            recyclerView.setAdapter(musicAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),recyclerView.VERTICAL,
                    false));
        }
        return view;
    }
}