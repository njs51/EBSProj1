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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    private void setText_mp3(adapter adapter){

        mediaPlayer = new MediaPlayer();

        try{
            mediaPlayer.setDataSource(adapter.mp3_A1);
            tv_engtxt.setText(adapter.txt1_A1_ENG);
            tv_kortxt.setText(adapter.txt1_A1_KOR);

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    while (mp.isPlaying()){

                    }
                    mp.stop();
                }

                //Toast.makeText(getApplicationContext(),"mp3 stop..", Toast.LENGTH_LONG).show();
            });
            mediaPlayer.prepare();

        }catch (IOException e){
            e.printStackTrace();
        }

    }


    private void removeAllPreferences(Context context) {
        SharedPreferences pref = context.getSharedPreferences("adapter", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }

}
