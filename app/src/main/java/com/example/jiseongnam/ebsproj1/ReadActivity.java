package com.example.jiseongnam.ebsproj1;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("adapter", getApplicationContext().MODE_PRIVATE);
        setContentView(R.layout.activity_read);

        tv_id = (TextView)findViewById(R.id.item_id);
        tv_title = (TextView)findViewById(R.id.item_title);
        tv_engtxt = (TextView)findViewById(R.id.txt_eng);
        tv_kortxt = (TextView)findViewById(R.id.txt_kor);

        speaking_id = pref.getString("id","");
        mp3link = pref.getString("mp3link","");
        tv_id.setText(pref.getString("id",""));
        tv_title.setText(pref.getString("title",""));
        tv_engtxt.setText(pref.getString("engtxt",""));
        tv_kortxt.setText(pref.getString("kortxt",""));

        Log.d(this.getClass().getName(),"오픈되나?????????????????????????????????????");

       //Query query = FirebaseDatabase.getInstance().getReference().child("speaking").orderByChild("id");
        System.out.println(speaking_id);


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
        MediaPlayer mediaPlayer = new MediaPlayer();
        try
        {
            Log.d(this.getClass().getName(),"음악되나?????????????????????????????????????");

            mediaPlayer.setDataSource(mp3link);
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

        removeAllPreferences(getApplicationContext());
    }

    private void removeAllPreferences(Context context) {
        SharedPreferences pref = context.getSharedPreferences("adapter", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }

}
