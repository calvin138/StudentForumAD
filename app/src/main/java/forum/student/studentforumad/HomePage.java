package forum.student.studentforumad;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class HomePage extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private Button Sign_out;
    private ImageButton addPost;
    private RecyclerView postList;
    private DatabaseReference postref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        postref = FirebaseDatabase.getInstance().getReference().child("Post");

        firebaseAuth = FirebaseAuth.getInstance();

        addPost = (ImageButton)findViewById(R.id.btnAddPost);

        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPost();
            }
        });

        Sign_out = (Button)findViewById(R.id.btnSign_out);

        Sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sign_out();
            }
        });

        postList = (RecyclerView)findViewById(R.id.all_post);
        postList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postList.setLayoutManager(linearLayoutManager);

        DisplayUserPost();
    }

    private void DisplayUserPost() {
        FirebaseRecyclerAdapter<Post, PostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>
                (
                        Post.class,
                        R.layout.all_post_layout,
                        PostViewHolder.class,
                        postref
                )
        {
            @Override
            protected void populateViewHolder(PostViewHolder viewHolder, Post model, int position)
            {
                final String post_key = getRef(position).getKey();

                viewHolder.setName(model.getName());
                viewHolder.setTime(model.getTime());
                viewHolder.setDate(model.getDate());
                viewHolder.setImage(getApplicationContext(), model.getImage());
                viewHolder.setTitle(model.getTitle());
                viewHolder.setBody(model.getBody());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent viewPageIntent = new Intent(HomePage.this, ViewPostPage.class);
                        viewPageIntent.putExtra("Posts_id", post_key);
                        startActivity(viewPageIntent);
                    }
                });

            }
        };
        postList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public PostViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setName(String name){
            TextView username = (TextView) mView.findViewById(R.id.post_profile_user_name);
            username.setText(name);
        }
        public void setTitle(String title){
            TextView postTitle = (TextView) mView.findViewById(R.id.post_title);
            postTitle.setText(title);
        }
        public void setImage(Context ctx, String image){
            ImageView Image = (ImageView) mView.findViewById(R.id.post_image);
            Picasso.get().load(image).into(Image);
        }
        public void setTime(String time){
            TextView postTime = (TextView) mView.findViewById(R.id.post_time);
            postTime.setText("   "  + time);
        }
        public void setDate(String date){
            TextView postDate = (TextView)mView.findViewById(R.id.post_date);
            postDate.setText("   " + date);
        }
        public void setBody(String body){
            TextView postbody = (TextView) mView.findViewById(R.id.post_body);
            postbody.setText(body);
        }
    }

    private void sign_out(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent (HomePage.this, LoginPage.class));
    }

    private void addPost(){
        startActivity(new Intent(HomePage.this, AddPostPage.class));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.homepagemenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.LogoutMenu:{
                sign_out();
                break;
            }
            case R.id.EditProfile: {
                startActivity(new Intent(HomePage.this, ViewProfilePage.class));
                break;
            }
            case R.id.SearchMenu:{

            }
        }
        return super.onOptionsItemSelected(item);
    }
}
