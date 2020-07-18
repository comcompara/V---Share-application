package com.example.digitalmr;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.futuremind.recyclerviewfastscroll.RecyclerViewScrollListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertController;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class ContactsActivity extends AppCompatActivity
{
    BottomNavigationView navView;
    RecyclerView myContactsList;
    ImageView findPeopleBtn;
    private DatabaseReference contactsRef, usersRef;
    private FirebaseAuth mAuth;
    private String currentUserId;
    private String userName="", profileImage="";
    private String CalledBy="";


    @Override
    protected void onCreate(Bundle savedInstanceState)
     {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);



        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        contactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts");
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");


         navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(navigationItemReselectedListener);

        findPeopleBtn =findViewById(R.id.find_people_btn);
        myContactsList =findViewById(R.id.contact_list);
        myContactsList.serLayoutManager(new LinearLayoutManager(getApplicationContext()));


        findPeopleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent findPeopleIntent = new Intent(ContactsActivity.this, FindPeopleActivity .class);
                startActivity(findPeopleIntent);
            }
        });
     }



    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemReselectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public void onNavigationItemReselected(@NonNull MenuItem menuItem)
        {
            switch (menuItem.getItemId())
            (
                case R.id.navigation_home
                    Intent mainIntent = new Intent(ContactsActivity.this, ContactsActivity.class);
                    startActivity(mainIntent);
                    break;

                case R.id.navigation_settings ;
                    Intent settingsIntent = new Intent(ContactsActivity.this, SettingActivity .class);
                    startActivity(settingsIntent);
                    break;

                case R.id.navigation_notifications;
                    Intent notificationsIntent = new Intent(ContactsActivity.this, NotificationsActivity.class);
                    startActivity(notificationsIntent);
                    break

                case R.id.navigation_logout:
                    FirebaseAuth.getInstance().signOut();
                    Intent logoutIntent = new Intent(ContactsActivity.this, Registrationactivity.class>.class);
                    startActivity(logoutIntent);
                    finish();
                    break




            )
            return true;
        }
    };


    @Override
    protected void onStart()
    {
        super.onStart();

        checkforReceivingCall();

        validateUser();




         FirebaseRecyclerOptions<Contacts> options
                 = new FirebaseRecycleroptions.Builder<Contacts>(
                 .setQuery(contactsRef.child(currrentUserId), Contacts.Class)
                 .build();



         FirebaseRecyclerAdapter<Contacts, ContactsViewHolder> firebaseRecyclerAdapter
               = new FirebaseRecyclerAdapter<Contacts, ContactsViewHolder>(options) {
             @Override
             protected void onBindViewHolder(@NonNull ContactsViewHolder holder, int i, @NonNull Contacts contacts)
             {
                 final String listUserId = getRef(i).getKey();

                 usersRef.child(listUserId).addValueEventListener(new ValueEventListener() {
                     @Override
                     public void onDataChange(DataSnapshot dataSnapshot)
                     {
                         if (dataSnapshot.exists())
                         {
                             userName = dataSnapshot.child("name".getValue().toString();
                             profileImage = dataSnapshot.child("image".getValue().toString();

                             holder.userNameTxt.srtText(userName);
                             Picasso.get().load(profileImage).into(holder.profileImageView);
                         }

                         holder.callBtn.setOnClickListener(new View.OnClickListener()) {
                             @Override
                             public void onClick(View v)
                             {
                                 Intent callingIntent = new Intent(ContactsActivity.this, CallingActivity.class);
                                 callingIntent.putExtra("visit_user_id", listUserId);
                                 startActivity(callingIntent);

                             }
                         });
                     }

                     @Override
                     public void onCancelled(DatabaseError databaseError) {

                     }
                 });
             }

             @NonNull
             @Override
             public ContactsViewHolder contactsViewHolder{@NonNull ViewGroup parent, int viewType)
             {
                 View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_design, parent, attachToRoot: false);
                 ContactsViewHolder viewHolder = new ContactsViewHolder(view);
                 return viewHolder;
             }
         };
         myContactList.setAdapter(firebaseRecyclerAdapter);
         firebaseRecyclerAdapter.startListening();
    }

    public static class ContactsViewHolder extends RecyclerView.ViewHolder
    {
        TextView userNameTxt;
        Button callBtn;
        ImageView profileImageView;



        public ContactsViewHolder(@NonNull View itemView)
        {
            super(itemView);

            userNameTxt = itemView.findViewById(R.id.name_notification);
            callBtn = itemView.findViewById(R.id.call_btn);
            profileImageView = itemView.findViewById(R.id.image_contact);
        }
    }


    private void validateUser()
        {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

            reference.child("Users").child(currentUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    if(!dataSnapshot.exists())
                    {
                        Intent setttingIntent = new Intent(ContactsActivity.this, SettingActivity.class);
                        startActivity(setttingIntent);
                        finish();

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }


    private void checkforReceivingCall()
    {
        usersRef.child(currentUserId)
                .child("Ringing")
                .addValueEvenListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        if (dataSnapshot.hasChild("ringing"))
                        {
                            calledBy= dataSnapshot.child("ringing").getValue().toString();

                            Intent callingIntent = new Intent(ContactsActivity.this, CallingActivity.class);
                            callingIntent.putExtra("visit_user_id", calledBy);
                            startActivity(callingIntent);
                        }
                    }

                    @Override
                    public void onDataChange(DatabaseError databaseError) {

                    }
                });


    }
}
