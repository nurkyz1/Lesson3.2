package com.geekcamp.lesson32.ui.post;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.geekcamp.lesson32.databinding.FragmentPostBinding;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PostFragment extends Fragment {

    private FragmentPostBinding binding;
    private NavController navController;
    private NavHostFragment navHostFragment;
    private PostAdapter adapter ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new PostAdapter();
        adapter.setOnClick(new PostAdapter.OnClick() {
            @Override
            public void Onclick(int pos) {
                Bundle bundle = new Bundle();
                Post post = adapter.getPost(pos);
                bundle.putSerializable("key", post);
                navController.navigate(R.id.action_postFragment_to_formFragment2, bundle);
            }

            @Override
            public void OnLongClickListener(int pos) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(requireContext());
                        String positive = "Да";
                        String negative = "Нет";
                        dialog.setMessage("Вы хотите удолить ?");
                        dialog.setPositiveButton(positive, (dialog1, which) -> App.api.deletePosts(adapter.getPost(pos).getId()).enqueue(new Callback<Post>() {
                            @Override
                            public void onResponse(Call<Post> call, Response<Post> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    adapter.deletePost(pos);
                                    dialog.show();
                                }
                            }
                            @Override
                            public void onFailure(Call<Post> call, Throwable t) {
                            }
                    }));
                            dialog.setNegativeButton(negative, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            dialog.show();
                        }

                });


    }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            binding = FragmentPostBinding.inflate(getLayoutInflater(), container, false);
                navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment);
                navController = navHostFragment.getNavController();
                return binding.getRoot();
            }

            @Override
            public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
                super.onViewCreated(view, savedInstanceState);

                binding.fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        navController.navigate(R.id.action_postFragment_to_formFragment2);
                    }
                });

                binding.recycler.setAdapter(adapter);

                App.api.getPosts().enqueue(new Callback<List<Post>>() {
                    @Override
                    public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            adapter.setPosts(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Post>> call, Throwable t) {
                        Log.e("TAG", "onFailure" + t.getLocalizedMessage());
                    }
                });
            }
        }
