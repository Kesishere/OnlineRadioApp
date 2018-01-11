package com.kes.radio4you;


import java.io.IOException;

        import android.app.Activity;
        import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
        import android.media.MediaPlayer;
        import android.media.MediaPlayer.OnCompletionListener;
        import android.media.MediaPlayer.OnPreparedListener;
        import android.net.Uri;
        import android.os.Bundle;
        import android.os.Environment;
        import android.util.Log;
        import android.view.View;
        import android.widget.CheckBox;
        import android.widget.CompoundButton;
        import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends Activity implements View.OnClickListener, OnPreparedListener {
    AudioManager am;
    MediaPlayer mediaPlayer;
    String DATA_STREAM = " ";
    TextView tx;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Log.wtf("TAG","Created");
        am = (AudioManager) getSystemService(AUDIO_SERVICE);
        TextView tx = findViewById(R.id.txView);
    }

    public void onClick(View view) {
        switch (view.getId()){
            case(R.id.playButton):
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                }
                else {mediaPlayer.start();
                Log.wtf("TAG",DATA_STREAM);}
                break;
            case(R.id.stations):
                Log.wtf("TAG","ACTIVITY START");
                Intent intent = new Intent(this,RadioStations.class);
                startActivityForResult(intent,1);
            }
        }


    private void releaseMP() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void CreatePlayer(){
        releaseMP();
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(DATA_STREAM);
            Log.wtf("TAG",DATA_STREAM);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("myLogs", "requestCode = " + requestCode + ", resultCode = " + resultCode);
        if (resultCode == RESULT_OK) {
            String name = data.getStringExtra("station");
            Log.wtf("TAG",name);
            Toast.makeText(this, "HI", Toast.LENGTH_SHORT).show();
            DATA_STREAM=name;
            CreatePlayer();
            Log.wtf("TAG","AA " + DATA_STREAM);
        }
        else{
            Toast.makeText(this, "NOPE", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMP();
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }
}
