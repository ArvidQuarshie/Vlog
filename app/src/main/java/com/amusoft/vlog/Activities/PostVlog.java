package com.amusoft.vlog.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.afollestad.easyvideoplayer.EasyVideoCallback;
import com.afollestad.easyvideoplayer.EasyVideoPlayer;
import com.amusoft.vlog.Constants;
import com.amusoft.vlog.Objects.Vlog;
import com.amusoft.vlog.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class PostVlog extends AppCompatActivity implements EasyVideoCallback {

    SharedPreferences prefs ;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child(Constants.firebase_reference_video);

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl(Constants.firebase_storage);




    ImageView promptupload;
//    VideoView touploadvideo;
    EasyVideoPlayer touploadvideo;


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
//        touploadvideo=(VideoView)findViewById(R.id.postvideoView);


        touploadvideo=(EasyVideoPlayer) findViewById(R.id.postvideoView);

        // Sets the callback to this Activity, since it inherits EasyVideoCallback
        touploadvideo.setCallback(this);
        touploadvideo.setAutoPlay(true);
        touploadvideo.setCallback(this);
        touploadvideo.setAutoPlay(true);

        touploadvideo.showControls();





        VideoTitle=(TextView)findViewById(R.id.postvideotitle);
        btnpost=(Button)findViewById(R.id.post);
        promptupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Video"),REQUEST_TAKE_GALLERY_VIDEO);

            }
        });


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
                Map<String, Object> fillData =new HashMap<String, Object>();
                fillData.put(Constants.firebase_reference_video_title,VideoTitle.getText().toString());
                fillData.put(Constants.firebase_reference_video_path,
                        prefs.getString(Constants.firebase_reference_video_path,null).toString());
                fillData.put(Constants.firebase_reference_video_uploader,
                        user=prefs.getString(Constants.firebase_reference_user_username,null));
                fillData.put(Constants.firebase_reference_video_views,String.valueOf(0));

                myRef.push().setValue(fillData);

                Toast.makeText(getApplicationContext(),"Vlog Sucessfully Uploaded",Toast.LENGTH_SHORT).show();
                prefs.edit().remove(Constants.firebase_reference_video_path).commit();



                Intent i=new Intent(getApplicationContext(),ViewListVLogs.class);
                startActivity(i);
                finish();
            }
        });

    }

    private void getUser() {
        try{
            user=prefs.getString(Constants.firebase_reference_user_username,null);
        }catch (Exception IDGAF){

        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
                touploadvideo.setVisibility(View.VISIBLE);
                promptupload.setVisibility(View.GONE);
                Uri selectedImageUri = data.getData();

                // OI FILE Manager
                filemanagerstring = selectedImageUri.getPath();
                touploadvideo.setSource(Uri.parse(filemanagerstring));
                touploadvideo.seekTo(100);

                // Sets the source to the HTTP URL held in the TEST_URL variable.
                // To play files, you can use Uri.fromFile(new File("..."))
                touploadvideo.setSource(Uri.parse(filemanagerstring));




                StorageReference riversRef = storageRef.child(filemanagerstring);
                UploadTask uploadTask = riversRef.putFile(selectedImageUri);

// Register observers to listen for when the download is done or if it fails
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        prefs.edit().putString(Constants.firebase_reference_video_path,
                                downloadUrl.toString()).commit();
                    }
                });



                // MEDIA GALLERY
                selectedImagePath = getPath(selectedImageUri);
                if (selectedImagePath != null) {

//                    touploadvideo.setVideoURI(Uri.parse(selectedImagePath));
//                    touploadvideo.seekTo(100);
                    touploadvideo.setSource(Uri.parse(filemanagerstring));


                    riversRef = storageRef.child(selectedImagePath);
                    uploadTask = riversRef.putFile(selectedImageUri);

// Register observers to listen for when the download is done or if it fails
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            prefs.edit().putString(Constants.firebase_reference_video_path,
                                    downloadUrl.toString()).commit();
                        }
                    });



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
