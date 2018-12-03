package com.example.jiseongnam.ebsproj1;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

public class ReadActivity extends AppCompatActivity {

    private FirebaseDatabase database;

    TextView tv_id;
    TextView tv_title;
    TextView tv_engtxt;
    TextView tv_kortxt;
    public String speaking_id;
    public String mp3link;
    readmodel readmodel;
    MediaPlayer mediaPlayer;
    MediaPlayer A1, B1, A2, B2;
    int flag=1;
    private adapter newadapter = new adapter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("post", getApplicationContext().MODE_PRIVATE);
        setContentView(R.layout.activity_read);

        tv_id = (TextView)findViewById(R.id.item_id);
        tv_title = (TextView)findViewById(R.id.item_title);
        tv_engtxt = (TextView)findViewById(R.id.txt_eng);
        tv_kortxt = (TextView)findViewById(R.id.txt_kor);

        speaking_id = pref.getString("id","");

        //mp3link = pref.getString("mp3link","");
        tv_id.setText(pref.getString("id",""));
        tv_title.setText(pref.getString("title",""));

        Log.d(this.getClass().getName(),"open?????????????????????????????????????");

        //Query query = FirebaseDatabase.getInstance().getReference().child("speaking").orderByChild("id");
        System.out.println(speaking_id);

        /////////////////////////////////////
        /**
         FirebaseDatabase.getInstance().getReference().child("speaking").orderByChild("id").addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

        for(DataSnapshot snapshot :dataSnapshot.getChildren()) {

        Log.d(this.getClass().getName(), "쿼리되나?????????????????????????????????????");
        readmodel = snapshot.getValue(readmodel.class);
        }
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
        });
         **/

        Log.d(this.getClass().getName(),"mediaplayer?????????????????????????????????????");

        FirebaseDatabase.getInstance().getReference().child("speaking").orderByChild("id").equalTo(speaking_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    newadapter = snapshot.getValue(adapter.class);
                    setText_mp3(newadapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /***
         setText_mp3(pref.getString("mp3_A1",""),pref.getString("txt1_A1_ENG",""),pref.getString("txt1_A1_KOR",""));
         setText_mp3(pref.getString("mp3_B1",""),pref.getString("txt1_B1_ENG",""),pref.getString("txt1_B1_KOR",""));
         setText_mp3(pref.getString("mp3_A2",""),pref.getString("txt1_A2_ENG",""),pref.getString("txt1_A2_KOR",""));
         setText_mp3(pref.getString("mp3_B2",""),pref.getString("txt1_B2_ENG",""),pref.getString("txt1_B2_KOR",""));

         ***/

        removeAllPreferences(getApplicationContext());
    }

    private void setText_mp3(final adapter adapter){

        A1 = new MediaPlayer();
        B1 = new MediaPlayer();
        A2 = new MediaPlayer();
        B2 = new MediaPlayer();

        //MediaPlayer mp = new MediaPlayer();

        /**
         //mp.setLooping(false);
         mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
        mediaPlayer.reset();
        try{
        mediaPlayer.setDataSource(adapter.mp3_A1);
        }catch (IOException e){
        e.printStackTrace();
        }
        try{
        mediaPlayer.prepareAsync();
        }catch (IllegalStateException e){
        e.printStackTrace();
        }
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        }
        });

        }
        });
         **/


        //A1.setLooping(false);
        try {
            A1.setDataSource(adapter.mp3_A1);
            tv_engtxt.setText(adapter.txt1_A1_ENG);
            tv_kortxt.setText(adapter.txt1_A1_KOR);

            A1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    while (mp.isPlaying()) { }
                }
            });
            A1.prepareAsync();

            A1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    tv_engtxt.setText(adapter.txt1_B1_ENG);
                    tv_kortxt.setText(adapter.txt1_B1_KOR);
                    A1.release();

                    try {
                        B1.setDataSource(adapter.mp3_B1);

                        B1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mp.start();
                                while (mp.isPlaying()) { }
                            }
                        });

                        B1.prepareAsync();

                        B1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                tv_engtxt.setText(adapter.txt1_A2_ENG);
                                tv_kortxt.setText(adapter.txt1_A2_KOR);
                                B1.release();

                                try {
                                    A2.setDataSource(adapter.mp3_A2);

                                    A2.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                        @Override
                                        public void onPrepared(MediaPlayer mp) {
                                            mp.start();
                                            while (mp.isPlaying()) { }
                                        }
                                    });

                                    A2.prepareAsync();

                                    A2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                        @Override
                                        public void onCompletion(MediaPlayer mediaPlayer) {
                                            tv_engtxt.setText(adapter.txt1_B2_ENG);
                                            tv_kortxt.setText(adapter.txt1_B2_KOR);
                                            A2.release();

                                            try {
                                                B2.setDataSource(adapter.mp3_B2);

                                                B2.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                                    @Override
                                                    public void onPrepared(MediaPlayer mp) {
                                                        mp.start();
                                                        while (mp.isPlaying()) { }
                                                    }
                                                });

                                                B2.prepareAsync();

                                                B2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                                    @Override
                                                    public void onCompletion(MediaPlayer mediaPlayer) {
                                                        B2.release();
                                                    }
                                                });
                                            } catch (IOException e4) { e4.printStackTrace(); }
                                        }
                                    });
                                } catch (IOException e3) { e3.printStackTrace(); }
                            }
                        });
                    } catch (IOException e2) { e2.printStackTrace(); }
                }
            });
        } catch (IOException e) { e.printStackTrace(); }

        /***
         //B1.setLooping(false);
         try{
         B1.setDataSource(adapter.mp3_B1);

         B1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
        while(flag!=2) {

        }
        mp.start();
        while(mp.isPlaying()){

        }
        //flag=3;
        }
        //Toast.makeText(getApplicationContext(),"mp3 stop..", Toast.LENGTH_LONG).show();
        });
         B1.prepareAsync();

         B1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
        tv_engtxt.setText(adapter.txt1_A2_ENG);
        tv_kortxt.setText(adapter.txt1_A2_KOR);
        }
        });

         }catch (IOException e){
         e.printStackTrace();
         }

         ***/
    }


    private void removeAllPreferences(Context context) {
        SharedPreferences pref = context.getSharedPreferences("post", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }

}
