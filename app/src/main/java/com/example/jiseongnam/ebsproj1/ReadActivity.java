package com.example.jiseongnam.ebsproj1;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadActivity extends AppCompatActivity {

    private List<adapter> adapters = new ArrayList<>();
    private FirebaseDatabase database;

    TextView tv_id;
    TextView tv_title;
    TextView tv_engtxt;
    TextView tv_kortxt;
    public String speaking_id;
    public String mp3link;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("adapter", getApplicationContext().MODE_PRIVATE);
        setContentView(R.layout.activity_read);

        database = FirebaseDatabase.getInstance();

        tv_id = (TextView)findViewById(R.id.item_id);
        tv_title = (TextView)findViewById(R.id.item_title);
        tv_engtxt = (TextView)findViewById(R.id.txt_eng);
        tv_kortxt = (TextView)findViewById(R.id.txt_kor);

        speaking_id = pref.getString("id","");
        //mp3link = pref.getString("mp3link","");


        Query query = database.getReference().child("speaking").orderByChild("id").equalTo(speaking_id.toString());


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                adapters.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    adapter adapter = snapshot.getValue(adapter.class);

                    mp3link = adapter.mp3_link;
                    tv_id.setText(adapter.id);
                    tv_title.setText(adapter.title);
                    tv_engtxt.setText(adapter.txt1_A1_ENG);
                    tv_kortxt.setText(adapter.txt1_A1_KOR);

                    adapters.add(adapter);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        MediaPlayer mediaPlayer = new MediaPlayer();
        try
        {
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
