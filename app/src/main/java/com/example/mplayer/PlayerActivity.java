package com.example.mplayer;

import static com.example.mplayer.MainActivity.musicFiles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

 //   @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mediaPlayer.start();
//        mediaPlayer.stop();
//        updateseek.interrupt();
//    }

    ImageView cover_art,next,previous,pause,play,loop,menu;
    TextView songsname,artistname,duration_played,duratation_total;
    SeekBar seekBar;
    String textContent;
    int position = -1;
    ArrayList<MusicFiles> listsongs = new ArrayList<>();
    static Uri uri;
    //Thread updateseek;
    MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private Thread playThread,prevThread,nextThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        getIntenMethod();
        metdata(uri);

        songsname.setText(listsongs.get(position).getTitle());
        artistname.setText(listsongs.get(position).getArtist());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress * 1000);
                }

            }


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                mediaPlayer.seekTo(seekBar.getProgress());

            }
        });
        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                   // int fullduration = mediaPlayer.getDuration()/1000;
                    seekBar.setProgress(mCurrentPosition);
                    duration_played.setText(formattedTime(mCurrentPosition));
                   // duratation_total.setText(formattedTime(fullduration));
                }
                handler.postDelayed(this,1000);
            }
        });
//        pause.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(mediaPlayer.isPlaying()){
//                    mediaPlayer.pause();
//                    pause.setImageResource(R.drawable.play);
//                }
//                else {
//                    mediaPlayer.start();
//                    play.setImageResource(R.drawable.pause);
//                }
//            }
//        });
    }
            private String formattedTime(int mCurrentPosition) {
                String totalout = "";
                String totalNew = "";
                String seconds = String.valueOf(mCurrentPosition % 60);
                String minutes = String.valueOf(mCurrentPosition / 60);
                totalout = minutes + ":" + seconds;
                totalNew = minutes + ":" + "0" + seconds;
                if (seconds.length() == 1) {
                    return totalNew;
                } else {
                    return totalout;
                }
            }


    private void initView() {
        setContentView(R.layout.activity_player);
        cover_art = findViewById(R.id.imageView);
        pause = findViewById(R.id.pause);
        next = findViewById(R.id.next);
        previous = findViewById(R.id.previous);
        loop = findViewById(R.id.loop);
        menu = findViewById(R.id.menu);
        songsname = findViewById(R.id.song_name);
        artistname = findViewById(R.id.artist_name);
        duration_played = findViewById(R.id.duration_played);
        duratation_total = findViewById(R.id.duration_total);
        seekBar = findViewById(R.id.seekBar);
        position = getIntent().getIntExtra("position",0);
    }


    private void getIntenMethod() {
        position = getIntent().getIntExtra("position", -1);
        listsongs = musicFiles;
        if (listsongs != null) {
            // pause.setImageResource(R.drawable.pause);
            uri = Uri.parse(listsongs.get(position).getPath());
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = mediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
        } else {
            mediaPlayer = mediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
        }
        seekBar.setMax(mediaPlayer.getDuration()/1000);
    }

    @Override
    protected void onResume() {
        playThreadbtn();
        prevThreadbtn();
        nextThreadbtn();
        super.onResume();
    }

    private void nextThreadbtn() {
        nextThread = new Thread(){
            @Override
            public void run() {
                super.run();
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();
                            mediaPlayer.release();
                            position = ((position + 1) % listsongs.size());
                            uri = Uri.parse(listsongs.get(position).getPath());
                            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                            metdata(uri);
                            songsname.setText(listsongs.get(position).getTitle());
                            artistname.setText(listsongs.get(position).getArtist());
                            seekBar.setMax(mediaPlayer.getDuration() / 1000);
                            PlayerActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (mediaPlayer != null) {
                                        int mCurrentposition = mediaPlayer.getCurrentPosition()/1000;
                                        seekBar.setProgress(mCurrentposition);
                                    }
                                    else {

                                    }

                                    }
                            }
                        }
                    }
                });

            }
        };
        nextThread.start();

    }

    private void prevThreadbtn() {
        prevThread = new Thread(){
            @Override
            public void run() {
                super.run();
                previous.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

            }
        };
        prevThread.start();
    }

    private void playThreadbtn() {
        playThread = new Thread(){
            @Override
            public void run() {
                super.run();
                pause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mediaPlayer.isPlaying()){
                            mediaPlayer.pause();
                            pause.setImageResource(R.drawable.play);
                        }
                        else {
                            mediaPlayer.start();
                            pause.setImageResource(R.drawable.pause);
                        }
                    }
                });
                loop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mediaPlayer.isLooping()){
                            loop.setImageResource(R.drawable.loop);
                            mediaPlayer.setLooping(false);
                        }
                        else {
                            mediaPlayer.setLooping(true);
                            loop.setImageResource(R.drawable.shuffle);
                        }
                    }
                });
                menu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mediaPlayer.stop();
                        Intent i = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(i);
                    }
                });
            }
        };
        playThread.start();
    }

    private void metdata(Uri uri){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        int durationtotal = Integer.parseInt(listsongs.get(position).getDuration())/1000;
        duratation_total.setText(formattedTime(durationtotal));
        byte [] art = retriever.getEmbeddedPicture();
        if (art!= null){
            Glide.with(this)
                    .asBitmap()
                    .load(art)
                    .into(cover_art);
        }
        else{
            Glide.with(this)
                    .asBitmap()
                    .load(R.drawable.muu)
                    .into(cover_art);

        }


    }

}

//    private void initView(){

        //Bundle bundle = intent.getExtras();
        //musicFiles= (ArrayList) bundle.getParcelableArrayList("songlist");
//        textContent = intent.getStringExtra("currentsong");
//        songsname.setText(textContent);
//        songsname.setSelected(true);


//

