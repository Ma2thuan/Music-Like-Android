package com.example.googleoauthapp;


import static com.google.android.gms.common.util.CollectionUtils.listOf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {
    ImageView imageView;
    TextView name, email, id;
    Button signOut;

    GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        imageView = findViewById(R.id.imageView);
        name = findViewById(R.id.textName);
        email = findViewById(R.id.textEmail);
        id = findViewById(R.id.textID);
        signOut = findViewById(R.id.button);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();
                if (id == R.id.button) {
                    signOut();
                }
            }
        });

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this );
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            name.setText(personName);
            email.setText(personEmail);
            id.setText(personId);
            Glide.with(this).load(String.valueOf(personPhoto)).into(imageView);
        }
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MainActivity2.this, "sign out successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

   /* private static final String TAG = "GoogleDriveActivity";
    ImageView imageView;
    TextView name, email, id;
    Button signOut;

    GoogleSignInClient mGoogleSignInClient;
    DriveResourceClient driveResourceClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        List<String> scopes = new ArrayList<>();
        scopes.add("https://www.googleapis.com/auth/drive");

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Get the Google account information from the Intent
        GoogleSignInAccount googleAccount = getIntent().getParcelableExtra("GoogleAccount");
        if (googleAccount != null) {
            setUpGoogleDrive(googleAccount);
        }

        // Initialize UI elements
        imageView = findViewById(R.id.imageView);
        name = findViewById(R.id.textName);
        email = findViewById(R.id.textEmail);
        id = findViewById(R.id.textID);
        signOut = findViewById(R.id.button);

        // Set the user's information
        setUserInfo(googleAccount);

        // Set the sign out button's click listener
        signOut.setOnClickListener(view -> {
            if (view.getId() == R.id.button) {
                signOut();
            }
        });
    }

    private void setUserInfo(GoogleSignInAccount acct) {
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            name.setText(personName);
            email.setText(personEmail);
            id.setText(personId);
            Glide.with(this).load(String.valueOf(personPhoto)).into(imageView);
        }
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, task -> {
                    Toast.makeText(MainActivity2.this, "Signed out successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                });
    }

    private void setUpGoogleDrive(GoogleSignInAccount googleAccount) {
        driveResourceClient = Drive.getDriveResourceClient(getApplicationContext(), googleAccount);
        listMp3Files();
    }

    private void listMp3Files() {
        Query query = new Query.Builder()
                .addFilter(Filters.eq(SearchableField.MIME_TYPE, "audio/mpeg"))
                .build();
        driveResourceClient.query(query).addOnSuccessListener(metadataBuffer -> {
            for (Metadata metadata : metadataBuffer) {
                Log.i(TAG, "Found mp3 file: " + metadata.getTitle());
            }
            metadataBuffer.release();
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Error listing mp3 files", e);
        });
    }*/
}