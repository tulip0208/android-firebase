package com.codecamp.chatapp;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.app.Dialog;
import android.content.Intent;
import android.media.session.MediaSession;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codecamp.chatapp.model.GetTimesAgo;
import com.codecamp.chatapp.notifications.Token;
import com.codecamp.chatapp.model.User;
import com.codecamp.chatapp.notifications.Token;
import com.codecamp.chatapp.viewholder.UserViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextView currentUserName,seeProfile,textMessage,close,yesView,noView;
    private RecyclerView recyclerView;
    private CircleImageView currentUserImage;
    private ConstraintLayout mRelativeLayout;
    private FloatingActionButton fab;
    private ProgressBar progressBar;
    private DatabaseReference myRef,chatRef;
    private FirebaseRecyclerOptions<User> options;
    private FirebaseRecyclerAdapter<User, UserViewHolder> adapter;
    private Query userQuery,searchQuery;
    private GetTimesAgo getTimeAgo;
    private long lastTime;
    private String lastSeenTime;
    //private String userID;
    private int page = 1;
    private int PAGE_LIMIT = 10;
    private Boolean isScrolling = false;
    private int currentItems,totalItems,scrollOutItems;
    private MenuItem menuItem;
    private SearchView searchView;
    private String user_id;
    private Dialog dialog;



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        currentUserName = findViewById(R.id.current_user_nameID);
        currentUserImage = findViewById(R.id.current_user_imageID);
        recyclerView = findViewById(R.id.recyclerviewID);
        fab = findViewById(R.id.fabID);
        progressBar = findViewById(R.id.scroolingProgressbarID);
        mRelativeLayout = findViewById(R.id.relativeLayoutID);
        currentUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUserImage.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                startActivity(new Intent(MainActivity.this,CurrentUserActivity.class));
                finish();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });





        myRef = FirebaseDatabase.getInstance().getReference().child("Users");

        //userQuery = myRef.orderByChild("status").equalTo("online"); //whose are online
        //userQuery = myRef.orderByChild("name").startAt("Aabc").endAt("Zz");// sequence A---Z
        userQuery = myRef.orderByChild("name").startAt("AaBbCcDd\uf8ff").endAt("WwXxYyZz\uf8ff");





        //scrollListner();


        try {

            myRef.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    try {
                        String current_userName = snapshot.child("name").getValue().toString();
                        String current_userImage = snapshot.child("image").getValue().toString();
                        if(current_userName!=null)
                        {
                            currentUserName.setText(current_userName);

                        }

                        if( current_userImage.equals("default"))
                        {
                            currentUserImage.setImageResource(R.drawable.avatar);
                        }else{
                            Glide.with(getApplicationContext()).load(current_userImage).into(currentUserImage);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }



                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(),"please wait",Toast.LENGTH_SHORT).show();
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }



    }

    private void logout()
    {

        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.fab_logout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.item_dialog_back));
        }
        dialog.getWindow().setLayout(WRAP_CONTENT,WRAP_CONTENT);
        noView = dialog.findViewById(R.id.noID);
        yesView = dialog.findViewById(R.id.yesID);
        dialog.setCancelable(false);
        dialog.show();

        noView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_animation));
                dialog.cancel();
            }
        });

        yesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yesView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_animation));
                myRef.child(mAuth.getCurrentUser().getUid()).child("status").setValue("offline");
                //myRef.child(mAuth.getCurrentUser().getUid()).child("time_stamp").onDisconnect().setValue(ServerValue.TIMESTAMP);
                startActivity(new Intent(MainActivity.this,SignUpActivity.class));
                finish();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onStart() {
        super.onStart();


        myRef.keepSynced(true);
        if(mAuth.getCurrentUser() == null)
        {
            startActivity(new Intent(MainActivity.this,SignUpActivity.class));
            finish();
        }else{

            getUserData(userQuery);
            // options
            updateToken(FirebaseInstanceId.getInstance().getToken());
        }

//        currentUserImage.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_left));
//        currentUserName.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_left));
        mRelativeLayout.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_left));


    }

    private void getUserData(Query queryText)
    {

        options = new FirebaseRecyclerOptions.Builder<User>().setQuery(queryText,User.class).build();
        adapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull User model) {

                holder.relativeLayout.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in_animation));
                holder.userName.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_left));
                holder.imageRelativeLayout.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_left));
                holder.onlineStatus.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_left));
                //holder.onlineIndicator.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_left));
                //holder.offlineIndicator.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_left));

               //String user_id = getRef(position).getKey();
               String userID = getRef(position).getKey();

                holder.userName.setText(model.getName());
                if(model.getStatus().equals("online"))
                {
                    holder.onlineIndicator.setVisibility(View.VISIBLE);
                    holder.offlineIndicator.setVisibility(View.INVISIBLE);
                }else{
                    holder.offlineIndicator.setVisibility(View.VISIBLE);
                    holder.onlineIndicator.setVisibility(View.INVISIBLE);
                }
                if(model.getBio().equals("default"))
                {
                    holder.onlineStatus.setText("Hi,i am using chat app");
                }else{
                    holder.onlineStatus.setText(model.getBio());
                }



                if(model.getImage().equals("default"))
                {
                    holder.userImage.setImageResource(R.drawable.avatar);
                }else {
                    Glide.with(getApplicationContext()).load(model.getImage()).into(holder.userImage);
                }
                holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View v) {
                        holder.relativeLayout.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                        dialog = new Dialog(MainActivity.this);
                        dialog.setContentView(R.layout.item_dialog);
                        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.item_dialog_back));
                        dialog.getWindow().setLayout(WRAP_CONTENT, WRAP_CONTENT);
                        seeProfile = dialog.findViewById(R.id.profileID);
                        textMessage = dialog.findViewById(R.id.textMessageID);
                        close = dialog.findViewById(R.id.closeID);
                        dialog.setCancelable(false);


                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                close.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                                dialog.cancel();
                            }
                        });

                        seeProfile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                seeProfile.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                                Intent intent = new Intent(MainActivity.this,Profileactivity.class);
                                intent.putExtra("userID",userID);
                                startActivity(intent);
                                finish();
                            }
                        });

                        textMessage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                textMessage.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                                Intent intent = new Intent(MainActivity.this,MessageActivity.class);
                                intent.putExtra("userID",userID);
                                startActivity(intent);
                                finish();
                            }
                        });

                        dialog.show();

                    }
                });

                myRef.child(mAuth.getCurrentUser().getUid()).child("time_stamp").onDisconnect().setValue(ServerValue.TIMESTAMP);
                myRef.child(mAuth.getCurrentUser().getUid()).child("status").onDisconnect().setValue("offline");

            }

            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new UserViewHolder(LayoutInflater.from(getApplicationContext()).inflate(R.layout.main_child_item,parent,false));

            }

        };
        // adapter
        adapter.startListening();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        scrollListner();
        //adapter.notifyDataSetChanged();
        //progressBar.setVisibility(View.VISIBLE);

    }


    private void scrollListner (){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //isScrolling = true;
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                currentItems = layoutManager.getChildCount();
                totalItems = layoutManager.getItemCount();
                scrollOutItems = layoutManager.findFirstVisibleItemPosition();
                //(currentItems + scrollOutItems == totalItems)
                if(isScrolling && !recyclerView.canScrollVertically(View.SCROLL_INDICATOR_BOTTOM))
                {
                    isScrolling = false;
                    progressBar.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                        }
                    }, 800);


                }else{
                    isScrolling = true;
                    progressBar.setVisibility(View.INVISIBLE);
                }


            }
        });

    }
    private void updateToken(String token)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Tokens");
        Token token1 = new Token(token);
        reference.child(mAuth.getCurrentUser().getUid()).setValue(token1);
    }



    @Override
    protected void onResume() {
        super.onResume();
        myRef.child(mAuth.getCurrentUser().getUid()).child("status").setValue("online");
        //myRef.child(mAuth.getCurrentUser().getUid()).child("time_stamp").onDisconnect().setValue(ServerValue.TIMESTAMP);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_item,menu);
       MenuItem menuItem = menu.findItem(R.id.searchID);
       SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {



                if(newText.length() != 0)
                {
                    //searchUser(searchText);
                   Query searchQuery = myRef.orderByChild("name").startAt(newText).endAt(newText+"\uf8ff");
                    getUserData(searchQuery);
                    Toast.makeText(getApplicationContext(),"Search here",Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(),"search here",Toast.LENGTH_SHORT).show();
                    return true;
                }else{
                    getUserData(userQuery);
                    return true;
                }

            }
        });


        return super.onCreateOptionsMenu(menu);
    }



    private void searchUser(String searchText){

    }
}