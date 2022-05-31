package com.codecamp.chatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.number.NumberRangeFormatter;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codecamp.chatapp.dapter.ChatAdapter;
import com.codecamp.chatapp.model.Chat;
import com.codecamp.chatapp.model.GetTimesAgo;
import com.codecamp.chatapp.model.GetTimesAgo;
import com.codecamp.chatapp.model.User;
import com.codecamp.chatapp.notifications.Client;
import com.codecamp.chatapp.notifications.Data;
import com.codecamp.chatapp.notifications.MyResponse;
import com.codecamp.chatapp.notifications.Sender;
import com.codecamp.chatapp.notifications.Token;
import com.codecamp.chatapp.service.APIService;
import com.codecamp.chatapp.viewholder.ChatViewHolder;
import com.codecamp.chatapp.viewholder.UserViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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
import com.google.firebase.database.ServerValue;
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
import java.util.Objects;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {
    private CircleImageView userImage;
    private TextView userName,statusTextView;
    private ConstraintLayout mRelativeLayout;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef,chatRef;
    private RecyclerView messageRecyclerview;
    private EditText messageSend;
    private ImageButton sendButton,sendFileButton,speechToTextMessageButton;
    private ImageView backButton;
    private ChatAdapter chatAdapter;
    private List<Chat> chatList;
    private ValueEventListener seenListner;
    private  DatabaseReference mReference;
    APIService apiService;
    private String userID;
    private String receiver,sender;
    Boolean notify = false;
    private HashMap<String,Object> seenHashMap;
    private StorageReference mStorageRef;
    private Uri imageUri;
    private ProgressDialog progressDialog;
    private String user_name,user_image;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_message);

        userName = findViewById(R.id.user_nameID);
        backButton = findViewById(R.id.backButtonID);

        userImage = findViewById(R.id.user_imageID);
        messageSend = findViewById(R.id.message_sendID);
        sendButton = findViewById(R.id.button_sendID);
        statusTextView = findViewById(R.id.statusID);
        messageRecyclerview = findViewById(R.id.message_recyclerviewID);
        sendFileButton = findViewById(R.id.sendFileButtonID);
        speechToTextMessageButton = findViewById(R.id.speechToTextMessageID);

        messageRecyclerview.setHasFixedSize(true);
        messageRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRelativeLayout = findViewById(R.id.relativeLayoutID);

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

//        mAuth = FirebaseAuth.getInstance();
//        chatRef = FirebaseDatabase.getInstance().getReference().child("Chats").push();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButton.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                Intent intent = new Intent(MessageActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendButton.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                notify = true;
                String message = messageSend.getText().toString();
                if(TextUtils.isEmpty(message))
                {
                    messageSend.setError("empty message!");
                }else {
                    sendMessage(mAuth.getCurrentUser().getUid(),userID,message);
                }

            }
        });

        sendFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"File sent",Toast.LENGTH_SHORT).show();
                sendFileButton.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                sendImageMessage();


            }
        });

        speechToTextMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"speech to text message",Toast.LENGTH_SHORT).show();
                speechToTextMessageButton.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                convertSpeechToText();
            }
        });


        userID = getIntent().getStringExtra("userID");
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().child("Users");
        try {

            mRef.child(userID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        user_name = snapshot.child("name").getValue().toString();
                        user_image = snapshot.child("image").getValue().toString();
                        String status = snapshot.child("status").getValue().toString();
                        String typingStatus = snapshot.child("typingTo").getValue().toString();
                        String time_stamp = snapshot.child("time_stamp").getValue().toString();
                        userName.setText(user_name);

                        if(typingStatus.equals(mAuth.getCurrentUser().getUid()))
                        {
                            statusTextView.setText("typing...");
                        }else{

                            if(status.equals("online"))
                            {
                                statusTextView.setText(status);
                            }else {
                                //mRef.child(userID).child("status").onDisconnect().setValue("offline");
                                GetTimesAgo getTimeAgo = new GetTimesAgo();
                                long lastTime = Long.parseLong(time_stamp);
                                String lastSeenTime = getTimeAgo.getTimeAgo(lastTime,getApplicationContext());
                                statusTextView.setText(lastSeenTime);
                            }

                        }

                        if(user_image.equals("default"))
                        {
                            userImage.setImageResource(R.drawable.avatar);
                        }else{
                            Glide.with(getApplicationContext()).load(user_image).into(userImage);
                        }
                        readMessage(mAuth.getCurrentUser().getUid(),userID,user_image);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }





        messageSend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length() == 0)
                {
                    startTyping("noOne");
                }else{
                    startTyping(userID);
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


    private void seenMessage(String userID)
    {
        try {
            chatRef = FirebaseDatabase.getInstance().getReference().child("Chats");
            chatRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //chatList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        //String isseen = dataSnapshot.child("isseen").getValue().toString();
                        receiver = dataSnapshot.child("receiver").getValue().toString();
                        sender = dataSnapshot.child("sender").getValue().toString();
                        if(receiver.equals(mAuth.getCurrentUser().getUid()) && sender.equals(userID))
                        {
                            seenHashMap = new HashMap<>();
                            seenHashMap.put("isseen",true);
                            dataSnapshot.getRef().updateChildren(seenHashMap);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void sendMessage(String sender,String receiver,String message)
    {
        //mReference = FirebaseDatabase.getInstance().getReference().child("Chats");
        chatRef = FirebaseDatabase.getInstance().getReference().child("Chats");
        String date = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("type","message");
        hashMap.put("message",message);
        hashMap.put("date",date);
        hashMap.put("isseen",false);
        hashMap.put("imgisseen",false);


        chatRef.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    //mRef.child(mAuth.getCurrentUser().getUid()).child("message_date").setValue(date);
                    mRef.child(userID).child("message_sender").child(sender).child("message_date").setValue(date);
                    messageSend.getText().clear();
                    messageRecyclerview.scrollToPosition(chatList.size()-1);
                    Toast.makeText(getApplicationContext(),"message sent",Toast.LENGTH_SHORT).show();
                }
            }
        });

        final String msg = message;
        mReference = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue().toString();
                if(notify)
                {
                    sendNotificcation(receiver,name,msg);
                }

                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void sendNotificcation(String receiver, String name, String msg)
    {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference().child("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Token token = dataSnapshot.getValue(Token.class);
                    Data data = new Data(mAuth.getCurrentUser().getUid(),R.mipmap.ic_launcher,name+":"+msg,"New Message",userID);
                    Sender sender = new Sender(data,token.getToken());
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if(response.code() == 200)
                                    {
                                        if(response.body().success != 1)
                                        {
                                            Toast.makeText(getApplicationContext(),"Failed!!",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readMessage(String currentUserID,String userID,String imageUrl)
    {
        chatList = new ArrayList<>();
        chatRef = FirebaseDatabase.getInstance().getReference().child("Chats");
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    String sender = snapshot.child("sender").getValue().toString();
                    String receiver = snapshot.child("receiver").getValue().toString();
                    String type = snapshot.child("type").getValue().toString();
                    String message = snapshot.child("message").getValue().toString();
                    String date = snapshot.child("date").getValue().toString();
                    Boolean isseen = Boolean.valueOf(snapshot.child("isseen").getValue().toString());
                    Boolean imgIsSeen = Boolean.valueOf(snapshot.child("imgisseen").getValue().toString());

                    if(receiver.equals(currentUserID) && sender.equals(userID) || receiver.equals(userID) && sender.equals(currentUserID))
                    {
                        chatList.add(new Chat(sender,receiver,type,message,date,isseen,imgIsSeen));
                    }
                    chatAdapter = new ChatAdapter(getApplicationContext(),chatList,imageUrl);
                    messageRecyclerview.scrollToPosition(chatList.size()-1);
                    messageRecyclerview.setAdapter(chatAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

        //FirebaseDatabase.getInstance().getReference().child("Users").child(userID).child("time_stamp").onDisconnect().setValue(ServerValue.TIMESTAMP);
//        userName.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_left));
//        statusTextView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_left));
//        userImage.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_left));
//        backButton.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_left));
        mRelativeLayout.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_left));
        seenMessage(userID);

    }

    private void startTyping(String typingID)
    {
        mAuth = FirebaseAuth.getInstance();
          mRef.child(mAuth.getCurrentUser().getUid()).child("typingTo").setValue(typingID);
    }

    @Override
    protected void onPause() {
        super.onPause();
        startTyping("noOne");
    }

    @Override
    protected void onStop() {
        super.onStop();
        startTyping("noOne");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        startTyping("noOne");
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
          //messageSend.setTag(result.get(0));
          sendMessage(sender,receiver,result.get(0));

        }

        if(requestCode ==21 && resultCode == RESULT_OK && data != null)
        {
            //Toast.makeText(getApplicationContext(),"sent file",Toast.LENGTH_SHORT).show();
            sendImage(data,sender,receiver);
        }
    }



    private void sendImage(Intent data,String sender,String receiver)
    {
        progressDialog = new ProgressDialog(MessageActivity.this);
        progressDialog.setTitle("Sending image....");
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        //progressDialog.getWindow().setBackgroundDrawableResource(R.color.purple_700);
        progressDialog.show();
        mStorageRef = FirebaseStorage.getInstance().getReference().child("ChatsImage").child(mAuth.getCurrentUser().getUid()).child("images");
        imageUri = data.getData();
        if(imageUri != null)
        {
            StorageReference filePath = mStorageRef.child(imageUri.getLastPathSegment()+ UUID.randomUUID());
            final UploadTask uploadTask = filePath.putFile(imageUri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if(!task.isSuccessful())
                            {

                                throw task.getException();
                            }

                            // downloadImageUrl = filePath.getDownloadUrl().toString();
                            return filePath.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {

                            if(task.isSuccessful())
                            {
                                //progressDialog.cancel();
                                Uri downloadUri = task.getResult();
                                String downloadImageUrl=  downloadUri.toString();
                                String date = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

                                HashMap<String,Object> chatMap = new HashMap<>();
                                chatMap.put("sender",sender);
                                chatMap.put("receiver",receiver);
                                chatMap.put("type","image");
                                chatMap.put("message",downloadImageUrl);
                                chatMap.put("date",date);
                                chatMap.put("isseen",false);
                                chatMap.put("imgisseen",false);
                                chatRef = FirebaseDatabase.getInstance().getReference().child("Chats");
                                chatRef.push().setValue(chatMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            progressDialog.cancel();
                                            messageRecyclerview.scrollToPosition(chatList.size()-1);
                                            Toast.makeText(getApplicationContext(),"message sent",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.cancel();
                    Toast.makeText(getApplicationContext(),"Error:-"+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}