package com.example.jiseongnam.ebsproj1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private List<post> posts = new ArrayList<>();
    private FirebaseDatabase database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        database = FirebaseDatabase.getInstance();

        recyclerView = (RecyclerView)findViewById(R.id.Home_RecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final BoardRecyclerViewAdapter boardRecyclerViewAdapter = new BoardRecyclerViewAdapter();
        recyclerView.setAdapter(boardRecyclerViewAdapter);

        Query query = database.getReference().child("post").orderByChild("id");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                posts.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Log.d(this.getClass().getName(),"query in HOMEACTIVITY");

                    post post = snapshot.getValue(post.class);
                    posts.add(post);
                }
                boardRecyclerViewAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    class BoardRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board, parent, false);

            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            ((CustomViewHolder)holder).id.setText(posts.get(position).id);
            ((CustomViewHolder)holder).title.setText(posts.get(position).title);

            Glide.with(getApplicationContext()).load(posts.get(position).img).into(((CustomViewHolder) holder).imageView);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(getApplicationContext(),"is it working?", Toast.LENGTH_LONG).show();
                    putPreferences(getApplicationContext(), "id", posts.get(position).id);
                    putPreferences(getApplicationContext(), "title", posts.get(position).title);
                    putPreferences(getApplicationContext(), "img", posts.get(position).img);

                    /***
                    putPreferences(getApplicationContext(), "id", adapters.get(position).id);
                    putPreferences(getApplicationContext(), "title", adapters.get(position).title);
                    putPreferences(getApplicationContext(), "mp3link", adapters.get(position).mp3_link);
                    putPreferences(getApplicationContext(), "txt1_A1_ENG", adapters.get(position).txt1_A1_ENG);
                    putPreferences(getApplicationContext(), "txt1_A1_KOR", adapters.get(position).txt1_A1_KOR);
                    putPreferences(getApplicationContext(), "mp3_A1", adapters.get(position).mp3_A1);
                    putPreferences(getApplicationContext(), "txt1_B1_ENG", adapters.get(position).txt1_B1_ENG);
                    putPreferences(getApplicationContext(), "txt1_B1_KOR", adapters.get(position).txt1_B1_KOR);
                    putPreferences(getApplicationContext(), "mp3_B1", adapters.get(position).mp3_B1);
                    putPreferences(getApplicationContext(), "txt1_A2_ENG", adapters.get(position).txt1_A2_ENG);
                    putPreferences(getApplicationContext(), "txt1_A2_KOR", adapters.get(position).txt1_A2_KOR);
                    putPreferences(getApplicationContext(), "mp3_A2", adapters.get(position).mp3_A2);
                    putPreferences(getApplicationContext(), "txt1_B2_ENG", adapters.get(position).txt1_B2_ENG);
                    putPreferences(getApplicationContext(), "txt1_B2_KOR", adapters.get(position).txt1_B2_KOR);
                    putPreferences(getApplicationContext(), "mp3_B2", adapters.get(position).mp3_B2);
                    Log.d(this.getClass().getName(),"다운되나?????????????????????????????????????");

                    ***/

                    Intent intent = new Intent(HomeActivity.this, ReadActivity.class);
                    startActivity(intent);
                }
            });
        }
        @Override
        public int getItemCount() {
            return posts.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder{
            TextView id;
            TextView title;
            ImageView imageView;

            public CustomViewHolder(View view){
                super(view);
                id  = (TextView)view.findViewById(R.id.item_id);
                title = (TextView)view.findViewById(R.id.item_title);
                imageView = (ImageView)view.findViewById(R.id.imageView);
            }
        }
    }

    private void putPreferences(Context context, String key, String value) {
        SharedPreferences pref = context.getSharedPreferences("post", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }
}

