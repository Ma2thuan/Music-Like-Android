package com.example.googleoauthapp.ui.notifications;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.googleoauthapp.ActDrive;
import com.example.googleoauthapp.GlobalVars;
import com.example.googleoauthapp.MainActivity2;
import com.example.googleoauthapp.R;
import com.example.googleoauthapp.databinding.FragmentNotificationsBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 0;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String userId;
    private String userEmail;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Cấu hình Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        // Thêm nút đăng nhập Google vào layout của bạn và xử lý sự kiện click
        SignInButton signInButton = binding.signInButton; // Giả sử bạn đã thêm SignInButton vào layout của NotificationsFragment
        signInButton.setOnClickListener(v -> signIn());

        Button updriveButton = root.findViewById(R.id.updrive);
        updriveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến Activity mới để hiển thị danh sách bài hát
                Intent intent = new Intent(getActivity(), ActDrive.class);
                /*intent.putExtra("GoogleAccount", account);*/
                startActivity(intent);
            }
        });

        return root;
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }



    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener(getActivity(), task -> {
                        if (task.isSuccessful()) {

                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Intent intent = new Intent(getActivity(), MainActivity2.class);
                           /* intent.putExtra("GoogleAccount", account);*/
                            startActivity(intent);

                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            String uid = user.getUid();
                            String email = user.getEmail();
                            /*String idToken = account.getIdToken();*/

                            GlobalVars.setUserId(user.getUid());
                            GlobalVars.setUserEmail(user.getEmail());
                          /*  GlobalVars.setToken(idToken);*/


                            // Kiểm tra trùng lặp UID
                            db.collection("users").whereEqualTo("UID", uid).get()
                                    .addOnCompleteListener(uidTask -> {
                                        if (uidTask.isSuccessful() && uidTask.getResult().isEmpty()) {
                                            // Kiểm tra trùng lặp Email
                                            db.collection("users").whereEqualTo("email", email).get()
                                                    .addOnCompleteListener(emailTask -> {
                                                        if (emailTask.isSuccessful() && emailTask.getResult().isEmpty()) {
                                                            // Thêm người dùng mới vào Firestore
                                                            Map<String, Object> userData = new HashMap<>();
                                                            userData.put("UID", uid);
                                                            userData.put("email", email);
                                                            userData.put("name", user.getDisplayName());


                                                            db.collection("users")
                                                                    .document(email)
                                                                    .set(userData)
                                                                    .addOnSuccessListener(aVoid -> Log.d("Firestore", "DocumentSnapshot successfully written!"))
                                                                    .addOnFailureListener(e -> Log.w("Firestore", "Error adding document", e));
                                                        }
                                                    });
                                        }
                                    });

                            createStorageDirectory(email);
                        } else {
                            Log.w("error", "signInWithCredential:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (ApiException e) {
            Log.w("error", "signInResult:failed code=" + e.getStatusCode());
        }
    }


    private void createStorageDirectory(String email) {
        if (email == null || email.isEmpty()) {
            Log.w("Storage", "Email is null or empty, cannot create directory");
            return;
        }

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference userFolder = storageRef.child("users/" + email.replace('.', '_'));

        // Tạo một file tạm thời để upload và tạo thư mục
        File tempFile;
        try {
            tempFile = File.createTempFile("temp", "file");
            userFolder.child("temp_file").putFile(Uri.fromFile(tempFile))
                    .addOnSuccessListener(taskSnapshot -> {
                        // Xóa file tạm sau khi tạo thư mục thành công
                        tempFile.delete();
                        Log.d("Storage", "Directory created successfully");
                    })
                    .addOnFailureListener(e -> {
                        Log.w("Storage", "Failed to create directory", e);
                    });
        } catch (IOException e) {
            Log.w("Storage", "Failed to create temp file", e);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
