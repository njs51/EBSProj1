package com.example.jiseongnam.ebsproj1;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

public class VoiceActivity extends AppCompatActivity {
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
    private Button btncontinue;

    private Button btnengscript;
    private Button btnkorscript;
    int engscrFlag = 0;
    int korscrFlag = 0;
    int voiceFlag=0;
    int scriptpage=0;

    private int onebutton=0;

    int flag=1;
    private adapter newadapter = new adapter();


    private static final String TAG = VoiceActivity.class.getSimpleName();
    private static final String CLIENT_ID = "aflve7zc0w";
    // 1. "내 애플리케이션"에서 Client ID를 확인해서 이곳에 적어주세요.
    // 2. build.gradle (Module:app)에서 패키지명을 실제 개발자센터 애플리케이션 설정의 '안드로이드 앱 패키지 이름'으로 바꿔 주세요

    private RecognitionHandler handler;
    private NaverRecognizer naverRecognizer;

    private TextView txtResult;
    private ImageView btnStart;
    private String mResult;

    private AudioWriterPCM writer;

    // Handle speech recognition Messages.
    private void handleMessage(Message msg) {
        switch (msg.what) {
            case R.id.clientReady:
                // Now an user can speak.
                txtResult.setText(". . .");
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
                    /////////////////////////////////////////////////////////////
                    //////////////////////////speech 결과부분 ////////////////////
                    /////////////////////////////////////////////////////////////

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
                //btnStart.setText(R.string.str_start);
                btnStart.setEnabled(true);
                break;

            case R.id.clientInactive:
                if (writer != null) {
                    writer.close();
                }

                //btnStart.setText(R.string.str_start);
                btnStart.setEnabled(true);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("adapter", getApplicationContext().MODE_PRIVATE);

        setContentView(R.layout.activity_voice);

        txtResult = (TextView) findViewById(R.id.txt_result);
        btnStart =(ImageView) findViewById(R.id.btn_start);

        btnengscript = (Button)findViewById(R.id.btn_engScr);
        btnkorscript = (Button)findViewById(R.id.btn_korScr);

        btncontinue = (Button)findViewById(R.id.btn_continue);

        handler = new RecognitionHandler(this);
        naverRecognizer = new NaverRecognizer(this, handler, CLIENT_ID);

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

        /////////////////////////////////////////////
        /////////////preference 받아옴 /////////////////
        //////////////////////////////////////////

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

        btnengscript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(engscrFlag==0){
                    engscrFlag = 1;
                    btnengscript.setTextColor(Color.parseColor("#138921"));
                    if(scriptpage==0){
                        tv_engtxt2.setText(newadapter.txt1_B1_ENG);
                    }
                    else if(scriptpage==1){
                        tv_engtxt2.setText(newadapter.txt1_B2_ENG);
                    }
                }
                else{
                    engscrFlag = 0;
                    btnengscript.setTextColor(Color.parseColor("#000000"));
                    tv_engtxt2.setText("_________________________");
                }
            }
        });
        btnkorscript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(korscrFlag==0){
                    korscrFlag = 1;
                    btnkorscript.setTextColor(Color.parseColor("#138921"));
                    if(scriptpage==0){
                        tv_kortxt2.setText(newadapter.txt1_B1_KOR);
                    }
                    else if(scriptpage==1){
                        tv_kortxt2.setText(newadapter.txt1_B2_KOR);
                    }
                }
                else{
                    korscrFlag = 0;
                    btnkorscript.setTextColor(Color.parseColor("#000000"));
                    tv_kortxt2.setText("_________________________");
                }
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!naverRecognizer.getSpeechRecognizer().isRunning()) {
                    // Start button is pushed when SpeechRecognizer's state is inactive.
                    // Run SpeechRecongizer by calling recognize().
                    mResult = "";
                    txtResult.setText(". . .");
                    //btnStart.setText(R.string.str_stop);
                    naverRecognizer.recognize();
                } else {
                    Log.d(TAG, "stop and wait Final Result");
                    btnStart.setEnabled(false);

                    naverRecognizer.getSpeechRecognizer().stop();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        // NOTE : initialize() must be called on start time.
        //A1.pause();
        //A2.pause();

        naverRecognizer.getSpeechRecognizer().initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mResult = "";
        txtResult.setText("");
        //btnStart.setText(R.string.str_start);
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
        private final WeakReference<VoiceActivity> mActivity;

        RecognitionHandler(VoiceActivity activity) {
            mActivity = new WeakReference<VoiceActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            VoiceActivity activity = mActivity.get();
            if (activity != null) {
                activity.handleMessage(msg);
            }
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

                    tv_engtxt.setTextColor(Color.parseColor("#000000"));
                    tv_kortxt.setTextColor(Color.parseColor("#000000"));
                    tv_engtxt2.setTextColor(Color.parseColor("#138921"));
                    tv_kortxt2.setTextColor(Color.parseColor("#138921"));

                    txtResult.setText("아래의 버튼을 누르고 말해보세요");

                    ////////////////////////////////////////////////////
                    ///////////////////여기에 B1 speaking///////////////////
                    ////////////////////////////////////////////////////


                    //////////////////////////////////////////////////////////
                    ///////////////////B1speaking 끝나면 A2 초록색/////////////
                    //////////////////////////////////////////////////////////

                    A1.release();

                }
            });
        } catch (IOException e) { e.printStackTrace(); }

        btncontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onebutton == 0) {
                    onebutton = 1;
                    scriptpage = 1;
                    if (engscrFlag == 1) {
                        tv_engtxt2.setText(newadapter.txt1_B2_ENG);
                    }
                    if (korscrFlag == 1) {
                        tv_kortxt2.setText(newadapter.txt1_B2_KOR);
                    }

                    tv_engtxt.setText(adapter.txt1_A2_ENG);
                    tv_kortxt.setText(adapter.txt1_A2_KOR);
                    tv_engtxt.setTextColor(Color.parseColor("#138921"));
                    tv_kortxt.setTextColor(Color.parseColor("#138921"));
                    tv_engtxt2.setTextColor(Color.parseColor("#000000"));
                    tv_kortxt2.setTextColor(Color.parseColor("#000000"));

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
                                tv_engtxt.setTextColor(Color.parseColor("#000000"));
                                tv_kortxt.setTextColor(Color.parseColor("#000000"));
                                tv_engtxt2.setTextColor(Color.parseColor("#138921"));
                                tv_kortxt2.setTextColor(Color.parseColor("#138921"));

                                ////////////////////////////////////////////////////
                                ///////////////////여기에 B2 speaking///////////////////
                                ////////////////////////////////////////////////////

                                txtResult.setText("아래의 버튼을 누르고 말해보세요");

                            }
                        });
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        });

    }

    public void onBackPressed() {
        stopmp();
        super.onBackPressed();
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
    @Override
    protected void onDestroy() {
        getApplicationContext().getSharedPreferences("adapter", 0).edit().clear().commit();
        getApplicationContext().getSharedPreferences("post", 0).edit().clear().commit();
        if(A1!=null){ A1.release(); }
        if(B1!=null){ B1.release(); }
        if(A2!=null){ A2.release(); }
        if(B2!=null){ B2.release(); }
        super.onDestroy();
    }

    private void stopmp(){
        onebutton=0;
        try{ A1.stop(); A1.release();}catch (Exception e){ e.printStackTrace(); }
        try{ A2.stop(); A2.release();}catch (Exception e){ e.printStackTrace(); }
        try{ B1.stop(); B1.release();}catch (Exception e){ e.printStackTrace(); }
        try{ B2.stop(); B2.release();}catch (Exception e){ e.printStackTrace(); }
    }
}