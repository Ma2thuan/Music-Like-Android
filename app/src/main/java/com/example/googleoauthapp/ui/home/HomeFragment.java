package com.example.googleoauthapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.googleoauthapp.NgotBand;
import com.example.googleoauthapp.TheStroke;
import com.example.googleoauthapp.top50;
import com.example.googleoauthapp.top50VN;

import com.example.googleoauthapp.databinding.FragmentHomeBinding;

//import com.example.googleoauthapp.databinding.FragmentHomeBinding;
//import com.example.googleoauthapp.ui.playlist.top50;

public class HomeFragment extends Fragment {

     FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.imvTop50.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), top50.class);
            startActivity(intent);
        });

        binding.imvTop50VN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), top50VN.class);
                startActivity(intent);
            }
        });


        binding.NgotBand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NgotBand.class);
                startActivity(intent);
            }
        });


        binding.TheStroke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TheStroke.class);
                startActivity(intent);
            }
        });


        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}