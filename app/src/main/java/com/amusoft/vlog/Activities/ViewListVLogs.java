package com.amusoft.vlog.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.amusoft.vlog.Adapters.VlogItemRecyclerAdapter;
import com.amusoft.vlog.Constants;
import com.amusoft.vlog.Objects.Vlog;
import com.amusoft.vlog.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Map;

public class ViewListVLogs extends AppCompatActivity {
    SharedPreferences prefs ;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child(Constants.firebase_reference).child(Constants.firebase_reference_video);
    RecyclerView listVlogs;
    List<Vlog> listVideos;
   VlogItemRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_vlogs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listVlogs=(RecyclerView)findViewById(R.id.listVideos);
        listVlogs.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));

        adapter = new VlogItemRecyclerAdapter(getApplicationContext(),listVideos,myRef);
        getalldata();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),PostVlog.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void getalldata() {
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String s) {
                Map<String, Object> newPost = (Map<String, Object>) snapshot.getValue();
                if (newPost != null) {
                    listVideos.add(new Vlog(
                            newPost.get(Constants.firebase_reference_video_title).toString(),
                            newPost.get(Constants.firebase_reference_video_path).toString(),
                            newPost.get(Constants.firebase_reference_video_uploader).toString(),
                            newPost.get(Constants.firebase_reference_video_views).toString(),
                            snapshot.getKey()
                    ));

                    adapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
