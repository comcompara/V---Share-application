package com.example.digitalmr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class VideoChatActivity extends AppCompatActivity
{
    private static String API_Key = "";
    private static String SESSION_ID = "";
    private static String TOKEN = "";
    private static final String LOG_TAG = VideoChatActivity.class.getSimpleName();
    private static final int RC_VIDEO_APP_PERM = 124;

    private ImageView closeVideoChatBtn;
    private DatabaseReference userRef;
    private String userID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_chat);


        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userRef = FirebaseAuth.getInstance().getReference().child("users");

        closeVideoChatBtn = findViewById(R.id.close_video_chat_btn)
        closeVideoChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                userRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        if (dataSnapshot.child(userID).hasChild("Ringing"))
                        {
                            userRef.child(userID).child("Ringing").removeValue();
                            startActivity(new Intent(VideoChatActivity));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                })
            }
        });
    }
}
