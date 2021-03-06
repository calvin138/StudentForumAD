package forum.student.studentforumad;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationPage extends AppCompatActivity {

    private EditText userName, userEmail, userPassword;
    private Button SignupBtn;
    private TextView Login;
    private FirebaseAuth firebaseAuth;
    String Email, name, Password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__page);
        setupUIViews();

        firebaseAuth = FirebaseAuth.getInstance();

        SignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(validate()){
                    //Update data to database
                   String user_email = userEmail.getText().toString().trim();
                   String user_password = userPassword.getText().toString().trim();
                   String user_name = userName.getText().toString().trim();
                   firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if(task.isSuccessful()){
                               sendUserData();
                               Toast.makeText(RegistrationPage.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                               finish();
                               firebaseAuth.signOut();
                               startActivity(new Intent (RegistrationPage.this, LoginPage.class));
                           }else{
                               Toast.makeText(RegistrationPage.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                           }
                       }
                   });
               }
            }
        });
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationPage.this, LoginPage.class));
            }
        });
    }

    private void setupUIViews(){
        userName = (EditText)findViewById(R.id.etUserName);
        userEmail = (EditText)findViewById(R.id.etEmail);
        userPassword = (EditText)findViewById(R.id.etPassword);
        SignupBtn = (Button)findViewById(R.id.btnSignup);
        Login = (TextView)findViewById(R.id.tvLogin);
    }

    private Boolean validate(){

        Boolean result = false;

        name = userName.getText().toString();
        Email = userEmail.getText().toString();
        Password = userPassword.getText().toString();

        if(name.isEmpty() || Email.isEmpty() || Password.isEmpty()) {
            Toast.makeText(this, "Please fill in all the details", Toast.LENGTH_SHORT).show();
        }else{
            return true;
        }
        return result;
    }

    private void sendUserData() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid());
        UserProfile userProfile = new UserProfile(name, Email);
        myRef.setValue(userProfile);

    }


}
