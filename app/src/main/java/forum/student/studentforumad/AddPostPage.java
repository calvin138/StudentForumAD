package forum.student.studentforumad;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddPostPage extends AppCompatActivity {
    private ImageButton addImage;
    private EditText addTitle;
    private EditText addBody;
    private Button addPost;

    private static final int Gallery_Pick = 1;
    private Uri ImageUri;

    public String title, body;
    private String saveCurrentDate, saveCurrentTime, postRandomName, downloadUrl, current_user_id;

    private StorageReference PostReference;
    private DatabaseReference userref, postref;
    private FirebaseAuth mAuth;

    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post_page);

        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();

        PostReference = FirebaseStorage.getInstance().getReference();
        userref = FirebaseDatabase.getInstance().getReference().child("Users");
        postref = FirebaseDatabase.getInstance().getReference().child("Post");

        addTitle = (EditText) findViewById(R.id.etPostTitle);
        addBody = (EditText) findViewById(R.id.etBody);
        addPost = (Button) findViewById(R.id.btnPost);
        addImage = (ImageButton) findViewById(R.id.addPicture);
        loadingbar = new ProgressDialog(this);

        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validatePostInfo();
            }
        });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    public void openGallery(){
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallery_Pick);
    }

    @Override
    protected void onActivityResult(int requestcode, int resultcode, Intent data){
        super.onActivityResult(requestcode, resultcode, data);

        if(requestcode==Gallery_Pick && resultcode==RESULT_OK && data!=null){
            ImageUri = data.getData();
            addImage.setImageURI(ImageUri);
        }
    }

    public void validatePostInfo(){
        title = addTitle.getText().toString();
        body = addBody.getText().toString();
        if(TextUtils.isEmpty(title)){
            Toast.makeText(this, "Please add the title",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(body)){
            Toast.makeText(this, "Please add body", Toast.LENGTH_SHORT).show();
        }
        else if(ImageUri!=null){
            loadingbar.setTitle("Add new Post");
            loadingbar.setMessage("Please wait for a moment");
            loadingbar.show();
            loadingbar.setCanceledOnTouchOutside(true);
            StoringImageToFirebaseStorage();
        }
        else
        {
            loadingbar.setTitle("Add new Post");
            loadingbar.setMessage("Please wait for a moment");
            loadingbar.show();
            loadingbar.setCanceledOnTouchOutside(true);
            savingPostWOimage();
        }
    }

    public void StoringImageToFirebaseStorage(){
        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calFordDate.getTime());

        Calendar calFordTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currentTime.format(calFordDate.getTime());

        postRandomName = saveCurrentDate + saveCurrentTime;


        final StorageReference filepath = PostReference.child("Post Images").child(ImageUri.getLastPathSegment() + postRandomName + ".jpg");

        filepath.putFile(ImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful())
                {
                    downloadUrl = task.getResult().getDownloadUrl().toString();
                    Toast.makeText(AddPostPage.this, "Image uploaded successfully",Toast.LENGTH_SHORT).show();

                    savingPost();
                }
                else
                {
                    String message = task.getException().getMessage();
                    Toast.makeText(AddPostPage.this,"Error" + message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void savingPost(){
        userref.child(current_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String userName = dataSnapshot.child("userName").getValue().toString();

                    HashMap postMap = new HashMap();
                    postMap.put("uid", current_user_id);
                    postMap.put("date", saveCurrentDate);
                    postMap.put("time", saveCurrentTime);
                    postMap.put("title", title);
                    postMap.put("image", downloadUrl);
                    postMap.put("name",  userName );
                    postMap.put("body", body);

                    postref.child(current_user_id + postRandomName).updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                sendToHomepage();
                                Toast.makeText(AddPostPage.this, "Post updated successfully", Toast.LENGTH_SHORT).show();
                                loadingbar.dismiss();
                            }
                            else{
                                Toast.makeText(AddPostPage.this, "Error", Toast.LENGTH_SHORT).show();
                                loadingbar.dismiss();
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void savingPostWOimage(){userref.child(current_user_id).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()){
                Calendar calFordDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
                saveCurrentDate = currentDate.format(calFordDate.getTime());

                Calendar calFordTime = Calendar.getInstance();
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
                saveCurrentTime = currentTime.format(calFordDate.getTime());

                postRandomName = saveCurrentDate + saveCurrentTime;

                String userName = dataSnapshot.child("userName").getValue().toString();

                HashMap postMap = new HashMap();

                postMap.put("uid", current_user_id);
                postMap.put("date", saveCurrentDate);
                postMap.put("time", saveCurrentTime);
                postMap.put("title", title);
                postMap.put("name",  userName );
                postMap.put("body", body);

                postref.child(current_user_id + postRandomName).updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            sendToHomepage();
                            Toast.makeText(AddPostPage.this, "Post updated successfully", Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();
                        }
                        else{
                            Toast.makeText(AddPostPage.this, "Error", Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();
                        }
                    }
                });
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });


    }
    public void sendToHomepage(){
        startActivity(new Intent(AddPostPage.this, HomePage.class));
    }

}
