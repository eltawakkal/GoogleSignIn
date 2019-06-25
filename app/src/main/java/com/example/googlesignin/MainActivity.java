package com.example.googlesignin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

//    variables
    private static int RC_SIGN_IN = 12; //value bebas

//    View
    private SignInButton signInButton;
    private TextView tvUserName;
    private TextView tvuserEmail;
    private FloatingActionButton fabLogout;
    private CircleImageView imgUser;

//    google login
    private GoogleSignInOptions mGso;
    private GoogleSignInAccount mGsa;
    private GoogleSignInClient mGsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        fabLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGsc.signOut();
            }
        });
    }

    private void signIn() {
        Intent signInInIntent = mGsc.getSignInIntent();
        startActivityForResult(signInInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            updateUI(task);

        }
    }

    private void updateUI(Task<GoogleSignInAccount> completedTask) {
        try {
            mGsa = completedTask.getResult(ApiException.class);

            setView(mGsa);
        } catch (ApiException e) {
            Log.d("kesalahan", "Error: " + e.toString());
        }
    }

    private void setView(GoogleSignInAccount mGsa) {

        String imgUrl = mGsa.getPhotoUrl().toString();
        String name = mGsa.getDisplayName();
        String email = mGsa.getEmail();

        Picasso.get().load(imgUrl).into(imgUser);
        tvUserName.setText(name);
        tvuserEmail.setText(email);
    }

    private void initView() {
        signInButton = findViewById(R.id.google_sign_in);
        tvUserName = findViewById(R.id.tv_user_name);
        tvuserEmail = findViewById(R.id.tv_user_email);
        fabLogout = findViewById(R.id.fab_logout);
        imgUser = findViewById(R.id.img_user);

        mGso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGsc = GoogleSignIn.getClient(this, mGso);
    }
}
