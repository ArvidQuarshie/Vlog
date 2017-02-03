package com.amusoft.vlog.Activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.afollestad.easyvideoplayer.EasyVideoCallback;
import com.afollestad.easyvideoplayer.EasyVideoPlayer;
import com.amusoft.vlog.Constants;
import com.amusoft.vlog.Objects.Comments;
import com.amusoft.vlog.Objects.Vlog;
import com.amusoft.vlog.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewSingleVlog extends AppCompatActivity implements EasyVideoCallback {
    SharedPreferences prefs ;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child(Constants.firebase_reference_video);

//    VideoView playVideo;
    EasyVideoPlayer playVideo;
    TextView titleTextView,videoviews;
    ListView listComments;
    EditText addComment;
    String FirebaseKey,username;
    Vlog SelectVideoObject;
    Comments commentobject;
    List <String> commentslazycount;

    ArrayAdapter<String> itemsAdapter;

    ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_single_vlog);
        prefs = getApplication().getSharedPreferences(Constants.shared_preference, 0);
        FirebaseKey= getIntent().getExtras().getString(Constants.extras_firekeyreference);
        username=prefs.getString(Constants.firebase_reference_user_username,null);

//        playVideo=(VideoView)findViewById(R.id.viewsingleVlog);

        playVideo = (EasyVideoPlayer) findViewById(R.id.viewsingleVlog);
        // Sets the callback to this Activity, since it inherits EasyVideoCallback
        playVideo.setCallback(this);
        playVideo.setAutoPlay(true);
        playVideo.showControls();



        titleTextView=(TextView)findViewById(R.id.viewsingleVlogTitle);
        videoviews=(TextView)findViewById(R.id.viewsingleVlogViews);
        listComments=(ListView)findViewById(R.id.comments_listView);
        addComment=(EditText)findViewById(R.id.chat_editText);
        commentslazycount=new ArrayList<String>();


     itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,commentslazycount);
        listComments.setAdapter(itemsAdapter);



        pDialog = new ProgressDialog(this);

        // Set progressbar message
        pDialog.setMessage("Buffering...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);


        fillFiredata();
        fillchatdata();



        addComment.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    String comment=addComment.getText().toString();
                    commentobject=new Comments(username,comment);

                    addComment.setText("");
                    myRef.child(FirebaseKey).child(Constants.firebase_reference_video_comments).push().setValue(commentobject);


//

                    return true;
                }

                return false;
            }
        });

    }
    private void fillFiredata() {
        FirebaseKey= getIntent().getExtras().getString(Constants.extras_firekeyreference);
        myRef.orderByKey().equalTo(FirebaseKey).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> newPost = (Map<String, Object>) dataSnapshot.getValue();
                if (newPost != null) {
                    SelectVideoObject= new Vlog(
                            newPost.get(Constants.firebase_reference_video_title).toString(),
                            newPost.get(Constants.firebase_reference_video_path).toString(),
                            newPost.get(Constants.firebase_reference_video_uploader).toString(),
                            newPost.get(Constants.firebase_reference_video_views).toString(),
                            dataSnapshot.getKey());

                    videoviews.setText(SelectVideoObject.getViews() + "Views");
                    titleTextView.setText(SelectVideoObject.getTitle());
                    playVideo.setSource(Uri.parse(SelectVideoObject.getPath()));


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

    private void fillchatdata() {
        FirebaseKey= getIntent().getExtras().getString(Constants.extras_firekeyreference);
        myRef.child(FirebaseKey).child(Constants.firebase_reference_video_comments).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> newPost = (Map<String, Object>) dataSnapshot.getValue();
                if (newPost != null) {
                    String whatineed=newPost.get(Constants.firebase_reference_video_comments_username).toString()
                            +":"+
                            newPost.get(Constants.firebase_reference_video_comments_comment).toString();
                    commentslazycount.add(whatineed);
                    itemsAdapter.notifyDataSetChanged();

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


    @Override
    public void onStarted(EasyVideoPlayer player) {

    }

    @Override
    public void onPaused(EasyVideoPlayer player) {

    }

    @Override
    public void onPreparing(EasyVideoPlayer player) {


    }

    @Override
    public void onPrepared(EasyVideoPlayer player) {

    }

    @Override
    public void onBuffering(int percent) {

    }

    @Override
    public void onError(EasyVideoPlayer player, Exception e) {

    }

    @Override
    public void onCompletion(EasyVideoPlayer player) {


    }

    @Override
    public void onRetry(EasyVideoPlayer player, Uri source) {

    }

    @Override
    public void onSubmit(EasyVideoPlayer player, Uri source) {

    }
}
