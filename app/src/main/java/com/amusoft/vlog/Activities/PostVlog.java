package com.amusoft.vlog.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.amusoft.vlog.Constants;
import com.amusoft.vlog.Objects.Vlog;
import com.amusoft.vlog.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostVlog extends AppCompatActivity {

    SharedPreferences prefs ;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child(Constants.firebase_reference).child(Constants.firebase_reference_video);


    ImageView promptupload;
    VideoView touploadvideo;
    TextView VideoTitle;
    Button btnpost;
    public static final int REQUEST_TAKE_GALLERY_VIDEO = 0;

    String selectedImagePath;


    String filemanagerstring;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_vlog);
        getUser();

        prefs = getApplication().getSharedPreferences(Constants.shared_preference, 0);
         promptupload = (ImageView)findViewById(R.id.uploadprompt);
        touploadvideo=(VideoView)findViewById(R.id.postvideoView);
        VideoTitle=(TextView)findViewById(R.id.postvideotitle);
        btnpost=(Button)findViewById(R.id.post);


        touploadvideo.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Video"),REQUEST_TAKE_GALLERY_VIDEO);
            }
        });

        btnpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vlog topost=new Vlog(VideoTitle.getText().toString(),
                        prefs.getString(Constants.firebase_reference_video_path,null).toString(),
                        user,
                        String.valueOf(0)
                );
                myRef.push().setValue(topost);

                Toast.makeText(getApplicationContext(),"Vlog Sucessfully Uploaded",Toast.LENGTH_SHORT).show();


                Intent i=new Intent(getApplicationContext(),ViewListVLogs.class);
                startActivity(i);
                finish();
            }
        });

    }

    private void getUser() {
        user=prefs.getString(Constants.firebase_reference_user_username,null);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
                touploadvideo.setVisibility(View.VISIBLE);
                promptupload.setVisibility(View.GONE);
                Uri selectedImageUri = data.getData();

                // OI FILE Manager
                filemanagerstring = selectedImageUri.getPath();
                touploadvideo.setVideoURI(Uri.parse(filemanagerstring));
                touploadvideo.seekTo(100);

                prefs.edit().putString(Constants.firebase_reference_video_path,
                        filemanagerstring).commit();

                // MEDIA GALLERY
                selectedImagePath = getPath(selectedImageUri);
                if (selectedImagePath != null) {
                    prefs.edit().putString(Constants.firebase_reference_video_path,
                            filemanagerstring).commit();

                    touploadvideo.setVideoURI(Uri.parse(selectedImagePath));
                    touploadvideo.seekTo(100);




                }
            }
        }
    }

    // UPDATED!
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }
}
