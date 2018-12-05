package com.example.jiseongnam.ebsproj1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class splashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this,HomeActivity.class);
        intent.putExtra("state","launch");
        startActivity(intent);
        finish();
    }
}
