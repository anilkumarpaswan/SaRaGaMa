package com.example.saregama;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gauravk.audiovisualizer.visualizer.BarVisualizer;

import java.io.File;
import java.util.ArrayList;

public class MusicPlayer extends AppCompatActivity {
    TextView txtSg, start, stop;
    ImageView Sound_Image;
    SeekBar SeekBar;

    Button Plat_btn, Next_btn, PrevBack_btn, Forward, PreviousBackAll;
    BarVisualizer barAnil;

    String SongNAme;

    public static final String EXTRA_NAME = "song_name";
    static MediaPlayer mediaPlayer;
    int position;
    ArrayList<File> mySong;
    Thread updateSeekBar;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
      if ( item.getItemId () == android.R.id.home){
          onBackPressed ();

      }
      return super.onOptionsItemSelected ( item );
    }

    @Override
    protected void onDestroy() {

        if (barAnil !=null){
            barAnil.release ();

        }
        super.onDestroy ();
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_music_player );

        getSupportActionBar ().setTitle ( "ANIL KUMAR" );
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );

        Sound_Image = findViewById ( R.id.Sound_Image );
        txtSg = findViewById ( R.id.txtSg );
        start = findViewById ( R.id.start );
        stop = findViewById ( R.id.stop );
        Sound_Image = findViewById ( R.id.Sound_Image );
        SeekBar = findViewById ( R.id.SeekBar );
        Plat_btn = findViewById ( R.id.Plat_btn );
        Next_btn = findViewById ( R.id.Next_btn );
        PrevBack_btn = findViewById ( R.id.PrevBack_btn );
        Forward = findViewById ( R.id.Forward );
        PreviousBackAll = findViewById ( R.id.PreviousBackAll );
        barAnil = findViewById ( R.id.barAnil );


        if (mediaPlayer != null) {
            mediaPlayer.start ();
            mediaPlayer.release ();

        }

        Intent intent = getIntent ();
        Bundle bundle = intent.getExtras ();
        mySong = (ArrayList) bundle.getParcelableArrayList ( "song" );
        String sName = intent.getStringExtra ( "songname" );
        position = bundle.getInt ( "pos", 0 );
        txtSg.setSelected ( true );
        Uri uri = Uri.parse ( mySong.get ( position ).toString () );
        SongNAme = mySong.get ( position ).getName ();
        txtSg.setText ( SongNAme );
        mediaPlayer = MediaPlayer.create ( getApplicationContext (), uri );
        mediaPlayer.start ();


        updateSeekBar = new Thread () {
            public void run() {
                int totalDuration = mediaPlayer.getDuration ();
                int currentPosition = 0;
                while (currentPosition < totalDuration) {
                    try {
                        sleep ( 500 );
                        currentPosition = mediaPlayer.getCurrentPosition ();
                        SeekBar.setProgress ( currentPosition );

                    } catch (InterruptedException | IllegalStateException e) {
                        e.printStackTrace ();


                    }


                }
               

            }

        };

        SeekBar.setMax ( mediaPlayer.getDuration () );
        updateSeekBar.start ();
        SeekBar.getProgressDrawable ().setColorFilter ( getResources ().getColor ( R.color.purple_700 ), PorterDuff.Mode.MULTIPLY );
        SeekBar.getThumb ().setColorFilter ( getResources ().getColor ( R.color.purple_700 ), PorterDuff.Mode.SRC_IN );


        SeekBar.setOnSeekBarChangeListener ( new SeekBar.OnSeekBarChangeListener () {
            @Override
            public void onProgressChanged(android.widget.SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(android.widget.SeekBar seekBar) {
                //mediaPlayer.seekTo ( SeekBar.getProgress () );
            }

            @Override
            public void onStopTrackingTouch(android.widget.SeekBar seekBar) {
                mediaPlayer.seekTo ( SeekBar.getProgress () );

            }
        } );


        String endTime = createTime ( mediaPlayer.getDuration () );
        stop.setText ( endTime );
        final Handler handler = new Handler ();
        final int delay = 1000;
        handler.postDelayed ( new Runnable () {
           @Override
           public void run() {
               String currentTime = createTime ( mediaPlayer.getCurrentPosition () );
               //txtSg.setText ( currentTime );
               start.setText (currentTime  );
               handler.postDelayed ( this,delay );


           }
       },delay );

           Plat_btn.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {

                if (mediaPlayer.isPlaying ()) {
                    Plat_btn.setBackgroundResource ( R.drawable.ic_anilplay );
                    mediaPlayer.pause ();

                } else {
                    Plat_btn.setBackgroundResource ( R.drawable.ic_anilpause );
                    mediaPlayer.start ();


                    TranslateAnimation animation = new TranslateAnimation ( -25, 25, -25, 25 );
                    animation.setInterpolator ( new AccelerateInterpolator () );
                    animation.setDuration ( 600 );
                    animation.setFillEnabled ( true );
                    animation.setFillAfter ( true );
                    animation.setRepeatCount ( Animation.REVERSE );
                    animation.setRepeatCount ( 1 );
                    Sound_Image.startAnimation ( animation );


                }

            }


        } );
        mediaPlayer.setOnCompletionListener ( new MediaPlayer.OnCompletionListener () {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Next_btn.performClick ();

            }
        } );

        int audioSessionId = mediaPlayer.getAudioSessionId ();
        if (audioSessionId != -1){
            barAnil.setAudioSessionId ( audioSessionId );

        }


        Next_btn.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop ();
                mediaPlayer.release ();
                position = ((position + 1)) % mySong.size ();
                Uri uri1 = Uri.parse ( mySong.get ( position ).toString () );
                mediaPlayer = MediaPlayer.create ( getApplicationContext (), uri1 );
                SongNAme = mySong.get ( position ).getName ();
                txtSg.setText ( SongNAme );
                mediaPlayer.start ();

                startAnimation ( Sound_Image, 360f );

            }
        } );
        PreviousBackAll.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop ();
                mediaPlayer.release ();
                position = ((position - 1) < 0) ? (mySong.size () - 1) : position - 1;
                Uri uri2 = Uri.parse ( mySong.get ( position ).toString () );
                mediaPlayer = MediaPlayer.create ( getApplicationContext (), uri2 );
                SongNAme = mySong.get ( position ).getName ();
                txtSg.setText ( SongNAme );
                mediaPlayer.start ();
                startAnimation ( Sound_Image, -360f );
            }
        } );
        Forward.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying ()){
                    mediaPlayer.seekTo ( mediaPlayer.getCurrentPosition ()-1000 );

                }


            }
        } );
        PrevBack_btn.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying ()){
                    mediaPlayer.seekTo ( mediaPlayer.getCurrentPosition ()-1000 );

                }

            }
        } );
    }


    private void startAnimation(View view,Float degree) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat ( Sound_Image,"rotation",0f ,degree );
        objectAnimator.setDuration ( 1000 );
        AnimatorSet animatorSet = new AnimatorSet ();
        animatorSet.playTogether ( objectAnimator );
        animatorSet.start ();
    }

    public String createTime(int duration){
        String time = "";
        int min = duration/1000/60;
        int sec = duration/1000%60;

        time = time+min+":";
        if (sec<10){
            time+=0;


        }
        time+=sec;
        return time;





    }
}