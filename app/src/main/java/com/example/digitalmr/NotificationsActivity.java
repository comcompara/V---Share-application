package com.example.digitalmr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class NotificationsActivity extends AppCompatActivity
{
    private RecyclerView notification_list;
    private DatabaseReference friendRequestRef, contactsRef, usersRef;
    private FirebaseAuth mAuth;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);



        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        friendRequestRef = FirebaseDatabase.getInstance().getReference().child("Friend Requests");
        contactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts");
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");


        notification_list = findViewById(R.id.notifications_list ;\
        notification_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        private FirebaseAuth mAuth;
        private String currentUserId;
    }


    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOption.Builder<Contacts()
                        .setQuery(friendRequestRef.child(), Contacts.class)
                        .build();

        FirebaseRecyclerAdapter<Contacts, NotificationsViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<Contacts, NotificationsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull NotificationsViewHolder holdert, int i, @NonNull Contacts contacts)
            {
                holder.acceptBtn.setVisiblity(View.VISIBLE);
                holder.cancelBtn.setVisiblity(View.VISIBLE);

                final String listUserId = getRef(i).getKey();

                DatabaseReference requestTypeRef = getRef(i).child("request_type").getRef();
                requestTypeRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        if (dataSnapshot.exists())
                        {
                            String type = dataSnapshot.getValue().toString();

                            if (type.equals(received))
                            {
                                holder.cardView.setVisibility(View.VISIBLE);

                                usersRef.child(listUserId)addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot)
                                    {
                                        if (dataSnapshot.hasChild("image"))
                                        {
                                            final String imageStr = dataSnapshot.child("image").getValue().toString();

                                            Picasso.get().load(imageStr.into(holder.profileImageView))
                                        }

                                        final String nameStr = dataSnapshot.child("name").getValue().toString();
                                        holder.userNameTxt.setText(nameStr);

                                        holder.acceptBtn.setOnClickListner(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View V)
                                            {
                                                contactsRef.child(currentUserId).child(listUserId)
                                                        .child("Contact").setValue("saved")
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task)
                                                            {
                                                                if (task.isSuccessful())
                                                                {
                                                                    contactsRef.child(listUserId).child(currentUserId)
                                                                            .child("Contact").setValue("saved")
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task)
                                                                                {
                                                                                    if (task.isSuccessful())
                                                                                    {
                                                                                        friendRequestRef.child(currentUserId).child(listUserID)
                                                                                                .removeValue()
                                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<Void> task)
                                                                                                    {
                                                                                                        if (task.isSuccessful())
                                                                                                        {
                                                                                                            friendRequestRef.child(listUserID).child(currentUserId)
                                                                                                                    .removeValue()
                                                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                        @Override
                                                                                                                        public void onComplete(@NonNull Task<Void> task)
                                                                                                                        {
                                                                                                                            if (task.isSuccessful())
                                                                                                                            {
                                                                                                                                Toast.makeText(context:NotificationActivity.this, text"New Contact Saved.", Toast.LENGTH_SHORT).show();
                                                                                                                            }
                                                                                                                        }
                                                                                                                    });
                                                                                                        }
                                                                                                    }
                                                                                                });
                                                                                    }

                                                                                }
                                                                            });
                                                                }

                                                            }
                                                        });
                                            }
                                        });

                                        holder.cancelBtn.setOnClickListner(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View V)
                                            {
                                                friendRequestRef.child(currentUserId).child(listUserID)
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task)
                                                            {
                                                                if (task.isSuccessful())
                                                                {
                                                                    friendRequestRef.child(currentUserID).child(listUserId)
                                                                            .removeValue()
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task)
                                                                                {
                                                                                    if (task.isSuccessful())
                                                                                    {
                                                                                        Toast.makeText( context,NotificationsActivity.this, text"Friend Request Cancelled.", Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        });
                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                            else
                            {
                                holder.cardView.setVisibility(View.GONE );
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

        }
    }
}       @NonNull
        @Override
        public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.find_friend_design, parent, false);
            NotificationsViewHolder viewHolder = new NotificationViewHolder(view);
            return viewHolder;
        }
    };
    notification_list.setAdapter(firebaseRecyclerAdapter);
    firebaseRecyclerAdapter.startListening();



public static class NotificationsViewHolder extends RecyclerView
{
    TextView userNameTxt;
    Button acceptBtn, cancelBtn;
    ImageView profileImageView;
    RelativeLayout cardview;


    public NotificationSViewHolder(@NonNull View itemView)
    {
        super(itemView);

        userNameTxt = itemView.findViewById(R.id.name_notification);
        acceptBtn = itemView.findViewById(R.id.request_accept_btn);
        cancelBtn = itemView.findViewById(R.id.request_decline_btn);
        profileImageView = itemView.findViewById(R.id.image_notification);
        cardview = itemView.findViewById(R.id.card_view);
    }
}





//        private void CancelFriendRequest()
//        {
//
//        }


//        private void AcceptFriendRequest()
//        {
//
//        }

}
