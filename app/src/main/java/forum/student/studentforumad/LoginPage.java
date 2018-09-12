package forum.student.studentforumad;

import android.app.ProgressDialog;
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
import com.google.firebase.auth.FirebaseUser;

public class LoginPage extends AppCompatActivity {

    private EditText Email;
    private EditText Password;
    private TextView Info;
    private Button Sign_in;
    private TextView Sign_up;
    private int counter = 5;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Email = (EditText)findViewById(R.id.etEmail);
        Password = (EditText)findViewById(R.id.etPassword);
        Info = (TextView)findViewById(R.id.tvInfo);
        Sign_in = (Button)findViewById(R.id.btnSign_in);
        Sign_up = (TextView)findViewById(R.id.tvSign_Up);

        Info.setText("Number of attempts remaining : 5");
        firebaseAuth = firebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        progressDialog = new ProgressDialog(this);

        if(user != null){
            finish();
            startActivity(new Intent(LoginPage.this, HomePage.class));
        }

        Sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(Email.getText().toString(),Password.getText().toString());
            }
        });

        Sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginPage.this, RegistrationPage.class));
            }
        });

    }

    private void validate (String userEmail, String userPassword){

        progressDialog.setMessage("Working on it...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    Toast.makeText(LoginPage.this, "Login Success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginPage.this, HomePage.class));
                }else{
                    Toast.makeText(LoginPage.this, "Login Fail", Toast.LENGTH_SHORT).show();
                    counter--;
                    Info.setText("Number of attempts remaining : " + counter);
                    if(counter == 0){
                        Sign_in.setEnabled(false);
                    }
                }

            }
        });
    }
}
