package com.geekcamp.lesson32.ui.post;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geekcamp.lesson32.data.models.Post;
import com.geekcamp.lesson32.databinding.ItemPostBinding;

import java.util.ArrayList;
import java.util.List;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder>  {

    private List<Post> posts = new ArrayList();
    private OnClick onClick;

    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    public void deletePost(int position) {
        posts.remove(position);
        notifyItemRemoved(position);
    }

    public Post getPost(int position) {
        return posts.get(position);
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPostBinding binding = ItemPostBinding.inflate(LayoutInflater
                .from(parent.getContext()), parent, false);
        return new PostViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        holder.onBind(posts.get(position));
    }

    @Override
    public int getItemCount() {
        return posts.size();

    }

    public  class PostViewHolder extends RecyclerView.ViewHolder  {

        private final ItemPostBinding binding;

        public PostViewHolder(@NonNull ItemPostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

        public void onBind(Post post) {
            binding.userId.setText(String.valueOf(post.getUser()));
            binding.tvDescription.setText(post.getContent());
            binding.tvTitle.setText(post.getTitle());
            itemView.setOnClickListener(v -> onClick.Onclick(getAdapterPosition()));
            itemView.setOnLongClickListener(v -> {
                onClick.OnLongClickListener(getAdapterPosition());
                return true;
            });
        }
    }
    interface OnClick{
        void  Onclick (int pos);
        void OnLongClickListener(int pos);
    }
}
