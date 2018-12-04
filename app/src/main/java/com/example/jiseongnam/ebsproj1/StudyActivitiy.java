package com.example.jiseongnam.ebsproj1;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.jiseongnam.ebsproj1.utils.AudioWriterPCM;
import com.google.firebase.database.FirebaseDatabase;
import com.naver.speech.clientapi.SpeechRecognitionResult;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

public class StudyActivitiy extends AppCompatActivity {
    private FirebaseDatabase database;

    TextView tv_id;
    TextView tv_title;
    TextView tv_engtxt;
    TextView tv_kortxt;
    TextView tv_engtxt2;
    TextView tv_kortxt2;
    public String speaking_id;
    public String mp3link;
    readmodel readmodel;
    MediaPlayer mediaPlayer;
    MediaPlayer A1, B1, A2, B2;
    ImageView imageView;

    int flag=1;
    private adapter newadapter = new adapter();

    /////////////////////////naver////////////////////////
    ////////////////////////////////////////////////////////
    private static final String TAG = StudyActivitiy.class.getSimpleName();
    private static final String CLIENT_ID = "aflve7zc0w";

    private RecognitionHandler handler;
    private NaverRecognizer naverRecognizer;

    private TextView txtResult;
    private Button btnStart;
    private Button btnengscript;
    private Button btnkorscript;
    int engscrFlag = 0;
    int engkorFlag = 0;

    private String mResult;

    private AudioWriterPCM writer;

    private void handleMessage(Message msg) {
        switch (msg.what) {
            case R.id.clientReady:
                // Now an user can speak.
                txtResult.setText("Connected");
                writer = new AudioWriterPCM(
                        Environment.getExternalStorageDirectory().getAbsolutePath() + "/NaverSpeechTest");
                writer.open("Test");
                break;

            case R.id.audioRecording:
                writer.write((short[]) msg.obj);
                break;

            case R.id.partialResult:
                // Extract obj property typed with String.
                mResult = (String) (msg.obj);
                txtResult.setText(mResult);
                break;

            case R.id.finalResult:
                // Extract obj property typed with String array.
                // The first element is recognition result for speech.
                SpeechRecognitionResult speechRecognitionResult = (SpeechRecognitionResult) msg.obj;
                List<String> results = speechRecognitionResult.getResults();
                StringBuilder strBuf = new StringBuilder();
                for(String result : results) {
                    strBuf.append(result);
                    strBuf.append("\n");
                }
                mResult = strBuf.toString();
                txtResult.setText(mResult);
                break;

            case R.id.recognitionError:
                if (writer != null) {
                    writer.close();
                }

                mResult = "Error code : " + msg.obj.toString();
                txtResult.setText(mResult);
                btnStart.setText(R.string.str_start);
                btnStart.setEnabled(true);
                break;

            case R.id.clientInactive:
                if (writer != null) {
                    writer.close();
                }

                btnStart.setText(R.string.str_start);
                btnStart.setEnabled(true);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // NOTE : initialize() must be called on start time.
        naverRecognizer.getSpeechRecognizer().initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mResult = "";
        txtResult.setText("");
        btnStart.setText(R.string.str_start);
        btnStart.setEnabled(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // NOTE : release() must be called on stop time.
        naverRecognizer.getSpeechRecognizer().release();
    }

    // Declare handler for handling SpeechRecognizer thread's Messages.
    static class RecognitionHandler extends Handler {
        private final WeakReference<StudyActivitiy> mActivity;

        RecognitionHandler(StudyActivitiy activity) {
            mActivity = new WeakReference<StudyActivitiy>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            StudyActivitiy activity = mActivity.get();
            if (activity != null) {
                activity.handleMessage(msg);
            }
        }
    }

    //////////////////////////////////////////////////////////
    ////////////////////naver_end//////////////////////////
    ////////////////////////////////////////////////////////

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        /////////////////////////////////////
        txtResult = (TextView) findViewById(R.id.txt_result);
        btnStart =(Button) findViewById(R.id.btn_start);
        btnengscript = (Button)findViewById(R.id.btn_engScr);
        btnkorscript = (Button)findViewById(R.id.btn_korScr);

        handler = new StudyActivitiy.RecognitionHandler(this);
        naverRecognizer = new NaverRecognizer(this, handler, CLIENT_ID);
        /////////////////////////////////////////////////////


        SharedPreferences pref = getApplicationContext().getSharedPreferences("adapter", getApplicationContext().MODE_PRIVATE);

        setContentView(R.layout.activity_study);

        tv_id = (TextView)findViewById(R.id.item_id);
        tv_title = (TextView)findViewById(R.id.item_title);
        tv_engtxt = (TextView)findViewById(R.id.txt_eng);
        tv_kortxt = (TextView)findViewById(R.id.txt_kor);
        tv_engtxt2 = (TextView)findViewById(R.id.txt_eng2);
        tv_kortxt2 = (TextView)findViewById(R.id.txt_kor2);
        imageView = (ImageView)findViewById(R.id.imageView);

        speaking_id = pref.getString("id","");

        tv_id.setText(pref.getString("id",""));
        tv_title.setText(pref.getString("title",""));
        Glide.with(imageView).load(pref.getString("img","")).into(imageView);

        newadapter.id = pref.getString("id","");
        newadapter.title = pref.getString("title","");
        newadapter.img = pref.getString("img","");
        newadapter.txt1_A1_ENG = pref.getString("txt1_A1_ENG","");
        newadapter.txt1_A1_KOR = pref.getString("txt1_A1_KOR","");
        newadapter.mp3_A1 = pref.getString("mp3_A1","");
        newadapter.txt1_A2_ENG = pref.getString("txt1_A2_ENG","");
        newadapter.txt1_A2_KOR = pref.getString("txt1_A2_KOR","");
        newadapter.mp3_A2 = pref.getString("mp3_A2","");
        newadapter.txt1_B1_ENG = pref.getString("txt1_B1_ENG","");
        newadapter.txt1_B1_KOR = pref.getString("txt1_B1_KOR","");
        newadapter.mp3_B1 = pref.getString("mp3_B1","");
        newadapter.txt1_B2_ENG = pref.getString("txt1_B2_ENG","");
        newadapter.txt1_B2_KOR = pref.getString("txt1_B2_KOR","");
        newadapter.mp3_B2 = pref.getString("mp3_B2","");

        setText_mp3(newadapter);

        /***
         setText_mp3(pref.getString("mp3_A1",""),pref.getString("txt1_A1_ENG",""),pref.getString("txt1_A1_KOR",""));
         setText_mp3(pref.getString("mp3_B1",""),pref.getString("txt1_B1_ENG",""),pref.getString("txt1_B1_KOR",""));
         setText_mp3(pref.getString("mp3_A2",""),pref.getString("txt1_A2_ENG",""),pref.getString("txt1_A2_KOR",""));
         setText_mp3(pref.getString("mp3_B2",""),pref.getString("txt1_B2_ENG",""),pref.getString("txt1_B2_KOR",""));

         ***/

        //removeAllPreferences(getApplicationContext());
        removeAllPreferences_adapter(getApplicationContext());
    }

    private void listen(){
        if(!naverRecognizer.getSpeechRecognizer().isRunning()) {
            // Start button is pushed when SpeechRecognizer's state is inactive.
            // Run SpeechRecongizer by calling recognize().
            mResult = "";
            txtResult.setText("Connecting...");
            btnStart.setText(R.string.str_stop);
            naverRecognizer.recognize();
        } else {
            Log.d(TAG, "stop and wait Final Result");
            naverRecognizer.getSpeechRecognizer().stop();
        }
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
                    ////////////////////////////////////////////////////
                    ///////////////////여기에 B1 speaking///////////////////
                    ////////////////////////////////////////////////////

                    tv_engtxt.setText(adapter.txt1_A2_ENG);
                    tv_kortxt.setText(adapter.txt1_A2_KOR);
                    A1.release();

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
                                ////////////////////////////////////////////////////
                                ///////////////////여기에 B2 speaking///////////////////
                                ////////////////////////////////////////////////////

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
    private void removeAllPreferences_adapter(Context context) {
        SharedPreferences pref = context.getSharedPreferences("adapter", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }
}
