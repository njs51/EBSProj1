package com.example.jiseongnam.ebsproj1;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase database;

    Button mp3btn;
    Button homebtn;
    Button voicebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mp3btn = (Button)findViewById(R.id.btn_mp3);
        mp3btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //database = FirebaseDatabase.getInstance();
                Toast.makeText(getApplicationContext(),"mp3 loading..", Toast.LENGTH_SHORT).show();

                MediaPlayer mediaPlayer = new MediaPlayer();
                try
                {
                    mediaPlayer.setDataSource("https://firebasestorage.googleapis.com/v0/b/ebsproj1.appspot.com/o/07%20%EC%9E%85%EC%9D%B4%20%ED%8A%B8%EC%9D%B4%EB%8A%94%20%EC%98%81%EC%96%B4_07.mp3?alt=media&token=64adf3a6-c171-4535-bfbd-e588fe8eafa2");
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();
                        }
                    });
                    mediaPlayer.prepare();
                }catch (IOException e){
                    e.printStackTrace();
                }

            }
        });

        homebtn = (Button)findViewById(R.id.btn_home);
        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        voicebtn = (Button)findViewById(R.id.btn_voice);
        voicebtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, VoiceActivity.class);
                startActivity(intent);
            }
        });
    }
}
