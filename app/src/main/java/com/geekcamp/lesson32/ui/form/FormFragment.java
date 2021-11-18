package com.geekcamp.lesson32.ui.form;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geekcamp.lesson32.App;
import com.geekcamp.lesson32.R;
import com.geekcamp.lesson32.data.models.Post;
import com.geekcamp.lesson32.databinding.FragmentFormBinding;
import com.geekcamp.lesson32.ui.post.PostAdapter;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FormFragment extends Fragment {
  private   FragmentFormBinding binding;
  private PostAdapter adapter;
    private NavController navController;
    private NavHostFragment navHostFragment;
    private  Post post;
    private boolean trueUpdate= false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFormBinding.inflate(getLayoutInflater(), container, false);
        adapter = new PostAdapter();
        navHostFragment  = (NavHostFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();


        binding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Post post = new Post(
                        binding.etTitle.getText().toString(),
                        binding.etDescription.getText().toString(),
                      Integer.parseInt(binding.etUserId.getText().toString()),
                        35);
                App.api.createPost(post).enqueue(new Callback<Post>() {
                    @Override
                    public void onResponse(Call<Post> call, Response<Post> response) {
                        if (response.isSuccessful()){
                            navController.popBackStack();
                        }
                    }

                    @Override
                    public void onFailure(Call<Post> call, Throwable t) {
                        Log.e("TAG","onFailure"+ t.getLocalizedMessage());
                    }
                });
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments()!= null){
            post = (Post) requireArguments().getSerializable("key");
            trueUpdate = true;
            SetPosts();
        }

        binding.btn.setOnClickListener(v -> {
            if (!trueUpdate){
                CreatePost();
            }else {
                UpdatePost();
            }
        });
    }

    private void CreatePost() {
        binding.btn.setOnClickListener(v -> App.api.createPost(getText()).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(@NotNull Call<Post> call, @NotNull Response<Post> response) {
                if (response.isSuccessful()){
                    navController.navigateUp();
                }
            }
            @Override
            public void onFailure(@NotNull Call<Post> call, @NotNull Throwable t) {
                Log.e("TAG", "onFailure: " + t.getLocalizedMessage() );
            }
        }));

    }

    private void UpdatePost() {
        App.api.updatePosts(getText().getUser(), getText()).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(@NotNull Call<Post> call, @NotNull Response<Post> response) {
                if (response.isSuccessful()){
                    navController.navigateUp();
                }
            }

            @Override
            public void onFailure(@NotNull Call<Post> call, @NotNull Throwable t) {
                Log.e("TAG", "onFailure: " + t.getLocalizedMessage() );
            }
        });
    }

    private Post getText() {
        return new Post(
                binding.etTitle.getText().toString(),
                binding.etDescription.getText().toString(),
                Integer.parseInt(binding.etUserId.getText().toString()),35);
    }




    private void SetPosts() {
        binding.etUserId.setText(String.valueOf(post.getUser()));
        binding.etTitle.setText(post.getTitle());
        binding.etDescription.setText(post.getContent());
    }
}