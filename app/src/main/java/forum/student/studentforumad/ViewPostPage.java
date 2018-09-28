package forum.student.studentforumad;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class ViewPostPage extends AppCompatActivity {

    private String mPost_key = null;
    private DatabaseReference postref;

    private TextView viewPostTitle;
    private ImageView viewPostImage;
    private TextView viewPostBody;

    private FirebaseAuth firebaseAuth;

    private Button viewRemovePostBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post_page);

        postref = FirebaseDatabase.getInstance().getReference().child("Post");

        firebaseAuth = FirebaseAuth.getInstance();

        mPost_key = getIntent().getExtras().getString("Posts_id");

        viewPostBody = (TextView)findViewById(R.id.viewPostBody);
        viewPostImage = (ImageView)findViewById(R.id.viewPostImage);
        viewPostTitle = (TextView)findViewById(R.id.viewPostTitle);

        viewRemovePostBtn = (Button)findViewById(R.id.viewRemovePostBtn);

        postref.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String post_title = (String) dataSnapshot.child("title").getValue();
                String post_body = (String) dataSnapshot.child("body").getValue();
                String post_image = (String) dataSnapshot.child("image").getValue();
                String post_uid = (String) dataSnapshot.child("uid").getValue();

                viewPostBody.setText(post_body);
                viewPostTitle.setText(post_title);

                Picasso.get().load(post_image).into(viewPostImage);


                //Toast.makeText(ViewPostPage.this, firebaseAuth.getCurrentUser().getUid() , Toast.LENGTH_LONG).show();

                if(firebaseAuth.getCurrentUser().getUid().equals(post_uid)){


                    //Toast.makeText(ViewPostPage.this, post_uid , Toast.LENGTH_LONG).show();
                    viewRemovePostBtn.setVisibility(View.VISIBLE);
                }
                else{
                    viewRemovePostBtn.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        viewRemovePostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postref.child(mPost_key).removeValue();

                Intent homeIntent = new Intent(ViewPostPage.this, HomePage.class);
                startActivity(homeIntent);
            }
        });


    }
}
