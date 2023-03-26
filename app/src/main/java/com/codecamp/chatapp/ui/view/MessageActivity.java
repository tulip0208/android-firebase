package com.codecamp.chatapp.ui.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codecamp.chatapp.R;
import com.codecamp.chatapp.bindings.adapters.chats.ChatAdapter;
import com.codecamp.chatapp.bindings.models.Chat;
import com.codecamp.chatapp.bindings.models.GetTimesAgo;
import com.codecamp.chatapp.bindings.models.UserInfo;
import com.codecamp.chatapp.notifications.Client;
import com.codecamp.chatapp.notifications.Data;
import com.codecamp.chatapp.notifications.MyResponse;
import com.codecamp.chatapp.notifications.Sender;
import com.codecamp.chatapp.notifications.Token;
import com.codecamp.chatapp.remote.database.language_translator.FirebaseTranslateStrategy;
import com.codecamp.chatapp.remote.database.language_translator.TranslateHelper;
import com.codecamp.chatapp.remote.database.language_translator.TranslateListner;
import com.codecamp.chatapp.remote.database.language_translator.TranslateStrategy;
import com.codecamp.chatapp.remote.database.messages.FirebaseMessageStrategy;
import com.codecamp.chatapp.remote.database.messages.MessageHandler;
import com.codecamp.chatapp.remote.database.messages.MessageStrategy;
import com.codecamp.chatapp.remote.database.theme.FirebaseThemeStrategy;
import com.codecamp.chatapp.remote.database.theme.ThemeHandler;
import com.codecamp.chatapp.remote.database.theme.ThemeStrategy;
import com.codecamp.chatapp.remote.database.user.FirebaseUserRetriever;
import com.codecamp.chatapp.remote.database.user.current_user.CurrentUserPresenter;
import com.codecamp.chatapp.service.APIService;
import com.codecamp.chatapp.ui.dialog.language_dialog.LanguageDialog;
import com.codecamp.chatapp.ui.dialog.progress_dialog.ProgressDialogHandler;
import com.codecamp.chatapp.utility.animations.AnimationController;
import com.codecamp.chatapp.utility.background.Background;
import com.codecamp.chatapp.utility.background.BackgroundController;
import com.codecamp.chatapp.utility.constants.colors.Colors;
import com.codecamp.chatapp.utility.constants.languages.Languages;
import com.codecamp.chatapp.utility.get_time_ago_handler.GetTimeAgoHandler;
import com.codecamp.chatapp.utility.toast.ToastController;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {
    private CircleImageView userImage;
    private TextView userName,statusTextView;
    private ConstraintLayout mRelativeLayout,toolbarConstraintLayout;
    private Toolbar toolbar;
    private RecyclerView messageRecyclerview;
    private EditText messageEditText;
    private ImageButton sendButton,sendFileButton,speechToTextMessageButton;
    private ImageView backButton,translateIcon;
    private ChatAdapter chatAdapter;
    private String userID;
    private Colors colors;
    private BackgroundController backgroundController;
    private ThemeHandler themeHandler;
    private ThemeStrategy themeStrategy;
    private FirebaseUserRetriever firebaseUserRetriever;
    private CurrentUserPresenter currentUserPresenter;
    private String currentUserId;
    private MessageHandler messageHandler;
    private MessageStrategy messageStrategy;
    private GetTimeAgoHandler timeAgoHandler;
    private ProgressDialogHandler progressDialogHandler;
    private ToastController toastController;
    private AnimationController animationController;
    private TranslateHelper translateHelper;
    private TranslateStrategy translateStrategy;
    private Languages languages;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_message);

        userID = getIntent().getStringExtra("userID");

        themeStrategy = new FirebaseThemeStrategy();
        themeHandler = new ThemeHandler(themeStrategy);
        backgroundController = new BackgroundController();

        timeAgoHandler = new GetTimeAgoHandler(this);
        animationController = new AnimationController();
        colors = new Colors();
        languages = new Languages();

        currentUserPresenter = new CurrentUserPresenter();
        currentUserId = currentUserPresenter.getUserId();

        firebaseUserRetriever = new FirebaseUserRetriever();
        messageStrategy = new FirebaseMessageStrategy();
        messageHandler = new MessageHandler(messageStrategy);
        progressDialogHandler = new ProgressDialogHandler(this);
        toastController = new ToastController();

        translateStrategy = new FirebaseTranslateStrategy();
        translateHelper = new TranslateHelper(translateStrategy);

        userName = findViewById(R.id.user_nameID);
        backButton = findViewById(R.id.backButtonID);

        userImage = findViewById(R.id.user_imageID);
        messageEditText = findViewById(R.id.message_sendID);
        sendButton = findViewById(R.id.button_sendID);
        statusTextView = findViewById(R.id.statusID);
        messageRecyclerview = findViewById(R.id.message_recyclerviewID);
        sendFileButton = findViewById(R.id.sendFileButtonID);
        speechToTextMessageButton = findViewById(R.id.speechToTextMessageID);

        messageRecyclerview.setHasFixedSize(true);
        messageRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRelativeLayout = findViewById(R.id.relativeLayoutID);
        toolbarConstraintLayout = findViewById(R.id.TOOLBAR_CONSTRAINTLAYOUT);
        toolbar = findViewById(R.id.toolbarID);
        translateIcon = findViewById(R.id.TRANSLATE_ICON);

        translateIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationController.fadeInAnimation(MessageActivity.this,translateIcon);
                //languageDialog.showDialog(messageEditText.getText().toString());
                TextView englishLanguage,germanLanguage,frenchLanguage,spanishLanguage,arabicLanguage,chineseLanguage,hindiLanguage;
                Dialog dialog = new Dialog(MessageActivity.this);
                dialog.setContentView(R.layout.language_layout);
                englishLanguage = dialog.findViewById(R.id.ENGLISH);
                germanLanguage = dialog.findViewById(R.id.GERMAN);
                frenchLanguage = dialog.findViewById(R.id.FRENCH);
                spanishLanguage = dialog.findViewById(R.id.SPANISH);
                arabicLanguage = dialog.findViewById(R.id.ARABIC);
                chineseLanguage = dialog.findViewById(R.id.CHINESE);
                hindiLanguage = dialog.findViewById(R.id.HINDI);
                dialog.show();
                englishLanguage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        translate(messageEditText.getText().toString(),languages.ENGLISH,languages.ENGLISH);
                    }
                });

                germanLanguage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        translate(messageEditText.getText().toString(),languages.ENGLISH,languages.GERMAN);
                    }
                });

                frenchLanguage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        translate(messageEditText.getText().toString(),languages.ENGLISH,languages.FRENCH);
                    }
                });

                spanishLanguage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        translate(messageEditText.getText().toString(),languages.ENGLISH,languages.SPANISH);
                    }
                });

                arabicLanguage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        translate(messageEditText.getText().toString(),languages.ENGLISH,languages.ARABIC);
                    }
                });

                chineseLanguage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        translate(messageEditText.getText().toString(),languages.ENGLISH,languages.CHINESE);
                    }
                });

                hindiLanguage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        translate(messageEditText.getText().toString(),languages.ENGLISH,languages.HINDI);
                    }
                });

            }
        });

        themeHandler.getTheme(userID, new ThemeStrategy.OnTheme() {
            @Override
            public void onThemeChanged(String theme) {
                if(theme.equals("default")){
                    Background background = backgroundController.changeBackground(colors.DEFAULT,5);
                    sendButton.setBackground(background.drawable);
                    toolbar.setBackgroundColor(Color.parseColor(background.color));
                    toolbarConstraintLayout.setBackgroundColor(Color.parseColor(background.color));
                }else{
                    Background background = backgroundController.changeBackground(theme,5);
                    sendButton.setBackground(background.drawable);
                    toolbar.setBackgroundColor(Color.parseColor(background.color));
                    toolbarConstraintLayout.setBackgroundColor(Color.parseColor(background.color));
                }
            }
        });

        firebaseUserRetriever.retrieveUser(userID, new FirebaseUserRetriever.UserRetrievedListener() {
            @Override
            public void onUserRetrieved(UserInfo userInfo) {
                userName.setText(userInfo.getName());
                if(userInfo.getTypingStatus().equals(currentUserId))
                {
                    statusTextView.setText("typing...");
                }else{

                    if(userInfo.getStatus().equals("online"))
                    {
                        statusTextView.setText(userInfo.getStatus());
                    }else {
                        String lastSeenTime = timeAgoHandler.provideTimesAgo(userInfo);
                        statusTextView.setText(lastSeenTime);
                    }

                }

                if(userInfo.getPhoto().equals("default"))
                {
                    userImage.setImageResource(R.drawable.avatar);
                }else{
                    Glide.with(getApplicationContext()).load(userInfo.getPhoto()).into(userImage);
                }
                //readMessage(currentUserId,userID,userInfo.getPhoto());
                messageHandler.readMessage(currentUserId, userID, userInfo.getPhoto(), new FirebaseMessageStrategy.OnMessageTask() {
                    @Override
                    public void onMessageReadCompleted(String imageUrl,List<Chat> chatList) {
                        chatAdapter = new ChatAdapter(getApplicationContext(),chatList,imageUrl);
                        messageRecyclerview.scrollToPosition(chatList.size()-1);
                        messageRecyclerview.setAdapter(chatAdapter);
                    }
                });

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButton.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                Intent intent = new Intent(MessageActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationController.fadeInAnimation(MessageActivity.this,sendButton);
                //notify = true;
                String message = messageEditText.getText().toString();
                if(TextUtils.isEmpty(message))
                {
                    messageEditText.setError("empty message!");
                }else {
                    //sendMessage(currentUserId,userID,message);
                    messageHandler.sendMessage(currentUserId,userID,message);
                }

            }
        });
        sendFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationController.fadeInAnimation(MessageActivity.this,sendFileButton);
                sendImageMessage();
            }
        });
        speechToTextMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationController.fadeInAnimation(MessageActivity.this,speechToTextMessageButton);
                convertSpeechToText();
            }
        });
        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length() == 0)
                {
                    //startTyping("noOne");
                    messageHandler.startTyping(currentUserId,"noOne");
                }else{
                    //startTyping(userID);
                    messageHandler.startTyping(currentUserId,userID);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void sendImageMessage()
    {
       Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,21);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MessageActivity.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }



    @Override
    protected void onStart() {
        super.onStart();
        mRelativeLayout.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_left));
        messageHandler.seenMessage(currentUserId,userID);
        //seenMessage(userID);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //startTyping("noOne");
        messageHandler.startTyping(currentUserId,"noOne");
    }

    @Override
    protected void onStop() {
        super.onStop();
        //startTyping("noOne");
        messageHandler.startTyping(currentUserId,"noOne");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //startTyping("noOne");
        messageHandler.startTyping(currentUserId,"noOne");
    }

    private void convertSpeechToText()
    {
       Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
       intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
       intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

       if(intent.resolveActivity(getPackageManager()) != null)
       {
            startActivityForResult(intent,10);
       }
    }

    protected void onActivityResult(int requestCode,int resultCode,@Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10 && resultCode == RESULT_OK && data != null)
        {
          ArrayList<String> result =  data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
          messageEditText.setText(result.get(0));
          //messageHandler.sendMessage(currentUserId,userID,result.get(0));
          //sendMessage(sender,receiver,result.get(0));
        }

        if(requestCode ==21 && resultCode == RESULT_OK && data != null)
        {
            //sendImage(data,currentUserId,userID);
            messageHandler.sendImage(data, currentUserId, userID, new FirebaseMessageStrategy.OnUploadFileTask() {
                @Override
                public void onLoading() {
                    progressDialogHandler.showDialog();
                }

                @Override
                public void onLoaded(String message) {
                    progressDialogHandler.dismissDialog();
                    toastController.toast(MessageActivity.this,message);
                    //messageRecyclerview.scrollToPosition();
                }
            });
        }
    }

    private void translate(String text,String sourceLanguage,String targetLanguage){
        translateHelper.translate(text,sourceLanguage,targetLanguage, new TranslateListner() {
            @Override
            public void onTranslating(String message) {
                toastController.toast(MessageActivity.this,message);
            }

            @Override
            public void onTranslated(String translatedText) {
                toastController.toast(MessageActivity.this,translatedText);
                messageEditText.setText(translatedText);
            }

            @Override
            public void onTranslateFailed(String error) {
                toastController.toast(MessageActivity.this,error);
            }
        });
    }

}