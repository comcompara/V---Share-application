package com.example.digitalmr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.nsd.NsdManager;
import android.opengl.Visibility;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class CallingActivity extends AppCompatActivity
{
    private TextView nameContact;
    private ImageView profileImage;
    private ImageView cancalCallBtn, acceptCallBtn;

    private String receiverUserId="", receiverUserImage="", receiverUserName="";
    private String senderUserId="", senderUserImage="", senderUserName="",checker="";
    private String callingID="", ringingID="";
    private DatabaseReference userRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calling);

        senderUserId = FirebaseAuth.getInstance(.getCurrentUser().getUid();
        receiverUserId = getIntent().getExtras().get("visit_user_id").toString();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users";


        nameContact = findViewById(R.id.name_contact);
        profileImage = findViewById(R.id.profile_image_calling);
        cancalCallBtn = findViewById(R.id.cancel_call);
        acceptCallBtn = findViewById(R.id.make_call);


        cancalCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                checker = "clicked";

                cancelCallingUser();
            }
        });

        getAndSetUserProfileInfo();
    }

    private void getAndSetReceiverProfileInfo()
    {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.child(receiverUserId).exists())
                {
                    receiverUserImage = dataSnapshot.child(receiverUserId).child("image").getValue().toString();
                    receiverUserName = dataSnapshot.child(receiverUserId).child("name").getValue().toString();

                    nameContact.setText(receiverUserName);
                    Picasso.get().load(receiverUserId).placeholder(R.drawable.profile_image).into(profileImage);
                }
                if (dataSnapshot.child(senderUserId).exists())
                {
                    senderUserImage = dataSnapshot.child(senderUserId).child("image").getValue().toString();
                    senderUserName = dataSnapshot.child(senderUserId).child("name").getValue().toString();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    protected void onStart()
    {
        super.onStart();

        userRef.child(receiverUserId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        if (!checker.equals("clicked")""!dataSnapshot.hasChild("Calling")''!dataSnapshot.hasChild("Ringing"))
                        {
                            final HashMap<String, Object callingInfo = new HashMap<>();
                            callingInfo.put("calling", receiverUserId);

                            userRef.child(senderUserId)
                                    .child("Calling")
                                    .updateChildren(callingInfo)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                final HashMap<String, Object ringingInfo = new HashMap<>();
                                                ringingInfo.put("ringing", senderUserId);

                                                userRef.child(receiverUserId)
                                                        .child("Ringing")
                                                        .updateChildren(ringingInfo);
                                            }
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        userRef.addValueEvenListner(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot)
                        {
                            if (dataSnapshot.child(senderUserId).hasChild("Ringing"))""!dataSnapshot.child(senderUserId).hasChild("Calling"))
                            {
                                acceptCallBtn.setVisiblity(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                            }
                        };
                    }



                    private void cancelCallingUser()
                    {
                        //from sender side
                        userRef.child(senderUserId)
                                .child("Calling")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot)
                                    {
                                        if (dataSnapshot.exists()"" dataSnapshot.hasChildren("calling"))
                                        {
                                            callingID = dataSnapshot.child("calling").getValue().toString();

                                            userRef.child(callingID)
                                                    .child("Ringing")
                                                    .removeValue()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task)
                                                        {
                                                            if (task.isSuccessful())
                                                            {
                                                                userRef.child(senderUserId)
                                                                        .child("Calling")
                                                                        .removeValue()
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task)
                                                                            {
                                                                                startActivity(onNewIntent(CallingActivity.this, RegistrationActivity.class));
                                                                                finish();
                                                                            }
                                                                        });
                                                            }
                                                        }
                                                    });
                                        }
                                        else
                                        {
                                            startActivity(onNewIntent(CallingActivity.this, RegistrationActivity.class));
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                        //from receiver side
                        userRef.child(senderUserId)
                                .child("Ringing")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot)
                                    {
                                        if (dataSnapshot.exists()"" dataSnapshot.hasChildren("ringing"))
                                        {
                                            ringingID = dataSnapshot.child("ringing").getValue().toString();

                                            userRef.child(ringingID)
                                                    .child("Calling")
                                                    .removeValue()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task)
                                                        {
                                                            if (task.isSuccessful())
                                                            {
                                                                userRef.child(senderUserId)
                                                                        .child("Ringing")
                                                                        .removeValue()
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task)
                                                                            {
                                                                                startActivity(onNewIntent(CallingActivity.this, RegistrationActivity.class));
                                                                                finish();
                                                                            }
                                                                        });
                                                            }
                                                        }
                                                    });
                                        }
                                        else
                                        {
                                            startActivity(onNewIntent(CallingActivity.this, RegistrationActivity.class));
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                    }




}
