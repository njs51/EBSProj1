package com.example.jiseongnam.ebsproj1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
    TextView tv_engtxt2;
    TextView tv_kortxt2;
    ImageView imageView;

    public String speaking_id;
    public String mp3link;
    readmodel readmodel;
    MediaPlayer mediaPlayer;
    MediaPlayer A1, B1, A2, B2;
    int flag=1;
    private adapter newadapter = new adapter();
    Button study_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("post", getApplicationContext().MODE_PRIVATE);
        setContentView(R.layout.activity_read);

        tv_id = (TextView)findViewById(R.id.item_id);
        tv_title = (TextView)findViewById(R.id.item_title);
        tv_engtxt = (TextView)findViewById(R.id.txt_eng);
        tv_kortxt = (TextView)findViewById(R.id.txt_kor);
        tv_engtxt2 = (TextView)findViewById(R.id.txt_eng2);
        tv_kortxt2 = (TextView)findViewById(R.id.txt_kor2);
        imageView = (ImageView)findViewById(R.id.imageView);

        study_btn = (Button)findViewById(R.id.btn_study);

        //////////btn to studyActivity



        speaking_id = pref.getString("id","");

        ////////////////////////////////////////////
        //////////////page setting/////////////////////
        ////////////////////////////////////////////////

        //mp3link = pref.getString("mp3link","");
        tv_id.setText(pref.getString("id",""));
        tv_title.setText(pref.getString("title",""));
        Glide.with(imageView).load(pref.getString("img","")).into(imageView);

        Log.d(this.getClass().getName(),"open?????????????????????????????????????");

        //Query query = FirebaseDatabase.getInstance().getReference().child("speaking").orderByChild("id");
        System.out.println(speaking_id);



        /////////////////////////////////////
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

        study_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(getApplicationContext(),"학습해봅시다",Toast.LENGTH_LONG).show();

                ///////////////////////////////////////////////////////////////
                //////////////////////////////pref 넘겨야됨/////////////////////////
                //////////////////////////////////////////////////////////////////
                putPreferences(getApplicationContext(), "id", newadapter.id);
                putPreferences(getApplicationContext(), "title", newadapter.title);
                putPreferences(getApplicationContext(), "img", newadapter.img);
                putPreferences(getApplicationContext(), "txt1_A1_ENG", newadapter.txt1_A1_ENG);
                putPreferences(getApplicationContext(), "txt1_A1_KOR", newadapter.txt1_A1_KOR);
                putPreferences(getApplicationContext(), "mp3_A1", newadapter.mp3_A1);
                putPreferences(getApplicationContext(), "txt1_B1_ENG", newadapter.txt1_B1_ENG);
                putPreferences(getApplicationContext(), "txt1_B1_KOR", newadapter.txt1_B1_KOR);
                putPreferences(getApplicationContext(), "mp3_B1", newadapter.mp3_B1);
                putPreferences(getApplicationContext(), "txt1_A2_ENG", newadapter.txt1_A2_ENG);
                putPreferences(getApplicationContext(), "txt1_A2_KOR", newadapter.txt1_A2_KOR);
                putPreferences(getApplicationContext(), "mp3_A2", newadapter.mp3_A2);
                putPreferences(getApplicationContext(), "txt1_B2_ENG", newadapter.txt1_B2_ENG);
                putPreferences(getApplicationContext(), "txt1_B2_KOR", newadapter.txt1_B2_KOR);
                putPreferences(getApplicationContext(), "mp3_B2", newadapter.mp3_B2);


                Intent intent = new Intent(ReadActivity.this, VoiceActivity.class);
                startActivity(intent);
            }
        });

        removeAllPreferences(getApplicationContext());
    }

    private void setText_mp3(final adapter adapter){

        A1 = new MediaPlayer();
        B1 = new MediaPlayer();
        A2 = new MediaPlayer();
        B2 = new MediaPlayer();

        //MediaPlayer mp = new MediaPlayer();

        //A1.setLooping(false);
        try {
            A1.setDataSource(adapter.mp3_A1);
            tv_engtxt.setText(adapter.txt1_A1_ENG);
            tv_kortxt.setText(adapter.txt1_A1_KOR);
            tv_engtxt2.setText(adapter.txt1_B1_ENG);
            tv_kortxt2.setText(adapter.txt1_B1_KOR);
            //////////////////////////A1색깔변경///////////////////
            /////////////////////////////////////////////////////

            //tv_engtxt.setTextColor(Color.parseColor("#000000"));
            //tv_kortxt.setTextColor(Color.parseColor("#000000"));
            tv_engtxt.setTextColor(Color.parseColor("#138921"));
            tv_kortxt.setTextColor(Color.parseColor("#138921"));

            A1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    //while (mp.isPlaying()) { }
                }
            });
            A1.prepareAsync();

            A1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    ///////////////////////////
                    ///////B1 색깔변경///////////////
                    ////////////////////////////
                    tv_engtxt.setTextColor(Color.parseColor("#000000"));
                    tv_kortxt.setTextColor(Color.parseColor("#000000"));
                    tv_engtxt2.setTextColor(Color.parseColor("#138921"));
                    tv_kortxt2.setTextColor(Color.parseColor("#138921"));
                    A1.release();

                    try {
                        B1.setDataSource(adapter.mp3_B1);

                        B1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mp.start();
                                //while (mp.isPlaying()) { }
                            }
                        });

                        B1.prepareAsync();

                        B1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                //////////////////////////////
                                //////////A2 색깔//////////////
                                //////////////////////////////
                                tv_engtxt.setText(adapter.txt1_A2_ENG);
                                tv_kortxt.setText(adapter.txt1_A2_KOR);
                                tv_engtxt2.setText(adapter.txt1_B2_ENG);
                                tv_kortxt2.setText(adapter.txt1_B2_KOR);
                                tv_engtxt.setTextColor(Color.parseColor("#138921"));
                                tv_kortxt.setTextColor(Color.parseColor("#138921"));
                                tv_engtxt2.setTextColor(Color.parseColor("#000000"));
                                tv_kortxt2.setTextColor(Color.parseColor("#000000"));

                                B1.release();

                                try {
                                    A2.setDataSource(adapter.mp3_A2);

                                    A2.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                        @Override
                                        public void onPrepared(MediaPlayer mp) {
                                            mp.start();
                                            //while (mp.isPlaying()) { }
                                        }
                                    });

                                    A2.prepareAsync();

                                    A2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                        @Override
                                        public void onCompletion(MediaPlayer mediaPlayer) {
                                            ////////////////////////////////////////////
                                            ////////////B2 색깔//////////////////////
                                            ////////////////////////////////

                                            tv_engtxt.setTextColor(Color.parseColor("#000000"));
                                            tv_kortxt.setTextColor(Color.parseColor("#000000"));
                                            tv_engtxt2.setTextColor(Color.parseColor("#138921"));
                                            tv_kortxt2.setTextColor(Color.parseColor("#138921"));

                                            A2.release();

                                            try {
                                                B2.setDataSource(adapter.mp3_B2);

                                                B2.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                                    @Override
                                                    public void onPrepared(MediaPlayer mp) {
                                                        mp.start();
                                                        //while (mp.isPlaying()) { }
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

    }


    private void removeAllPreferences(Context context) {
        SharedPreferences pref = context.getSharedPreferences("post", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }

    private void putPreferences(Context context, String key, String value) {
        SharedPreferences pref = context.getSharedPreferences("adapter", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

}
