package com.codecamp.chatapp.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.codecamp.chatapp.R;
import com.codecamp.chatapp.bindings.adapters.users.OnNavigateToProfilePage;
import com.codecamp.chatapp.bindings.adapters.users.UsersAdapter;
import com.codecamp.chatapp.remote.auth.login.loginpresenter.FirebaseLoginPresenter;
import com.codecamp.chatapp.remote.auth.login.loginpresenter.LoginTask;
import com.codecamp.chatapp.remote.database.user.current_user.CurrentUser;
import com.codecamp.chatapp.remote.database.user.current_user.CurrentUserPresenter;
import com.codecamp.chatapp.remote.database.user.current_user.OnFetchCurrentUser;
import com.codecamp.chatapp.remote.database.user.token_handler.TokenHandler;
import com.codecamp.chatapp.remote.database.user.users_list.OnUsersList;
import com.codecamp.chatapp.remote.database.user.users_list.UserListRepository;
import com.codecamp.chatapp.remote.database.user.users_list.UserListRepositoryImpl;
import com.codecamp.chatapp.remote.database.user.users_list.UsersListPresenter;
import com.codecamp.chatapp.remote.error_handler.FirebaseErrorHandler;
import com.codecamp.chatapp.bindings.models.User;
import com.codecamp.chatapp.utility.animations.AnimationController;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements OnNavigateToProfilePage,OnUsersList {
    private TextView currentUserName;
    private RecyclerView recyclerView;
    private CircleImageView currentUserImage;
    private ConstraintLayout mRelativeLayout;
    private UsersAdapter usersAdapter;
    private CurrentUserPresenter currentUserPresenter;
    private FirebaseErrorHandler firebaseErrorHandler;
    private FirebaseLoginPresenter loginPresenter;
    private TokenHandler tokenHandler;
    private String userToken;
    private String currentUser;
    private AnimationController animationController;
    private UserListRepository userListRepository;
    private UsersListPresenter userListPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        animationController = new AnimationController();
        tokenHandler = new TokenHandler();
        userToken = tokenHandler.getUserToken();
        firebaseErrorHandler = new FirebaseErrorHandler();
        currentUserPresenter = new CurrentUserPresenter();
        currentUser = currentUserPresenter.getUserId();
        loginPresenter = new FirebaseLoginPresenter();
        userListRepository = new UserListRepositoryImpl();
        userListPresenter = new UsersListPresenter(this,userListRepository);
        userListPresenter.getUsersList();

        currentUserName = findViewById(R.id.current_user_nameID);
        currentUserImage = findViewById(R.id.current_user_imageID);
        mRelativeLayout = findViewById(R.id.relativeLayoutID);
        recyclerView = findViewById(R.id.recyclerviewID);
        if(currentUser != null){
            loginPresenter.getIsLoggedInStatus(new LoginTask() {
                @Override
                public void onLoggedIn(String loggedInStatus) {
                    if(loggedInStatus.equals("no")){
                        startActivity(new Intent(MainActivity.this,SignUpActivity.class));
                        finish();
                    }
                }
            });
        }

        if(currentUser != null){
            currentUserPresenter.fetchCurrentUser(new OnFetchCurrentUser() {
                @Override
                public void onFetched(CurrentUser currentPerson) {
                    Log.v("MainActivity","user:"+currentPerson.getName());
                    if(currentPerson.getName()!=null){
                        currentUserName.setText(currentPerson.getName());
                    }
                    if(currentPerson.getImage().equals("default"))
                    {
                        currentUserImage.setImageResource(R.drawable.avatar);
                    }else{
                        Glide.with(getApplicationContext()).load(currentPerson.getImage()).into(currentUserImage);
                    }
                }

                @Override
                public void onFetchFailed(String error) {
                    firebaseErrorHandler.currentUserFetchingError(error);
                }
            });
        }

        currentUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUserImage.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                startActivity(new Intent(MainActivity.this, CurrentUserActivity.class));
                finish();
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        if(currentUser == null)
        {
            startActivity(new Intent(MainActivity.this,SignUpActivity.class));
            finish();
        }else{
            tokenHandler.updateToken(userToken);
            currentUserPresenter.disconnectUser();
            currentUserPresenter.disconnectResult("offline");
        }
        animationController.slideInLeftAnim(MainActivity.this,mRelativeLayout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentUserPresenter.connectResult("online");
    }

    @Override
    public void onNavigate(String userId) {
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        intent.putExtra("userID",userId);
        startActivity(intent);
        finish();
    }

    @Override
    public void showUsersList(List<User> users) {
        usersAdapter = new UsersAdapter(this, users,this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(usersAdapter);
        usersAdapter.notifyDataSetChanged();
    }

}