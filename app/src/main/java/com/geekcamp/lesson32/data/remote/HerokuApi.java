package com.geekcamp.lesson32.data.remote;

import android.widget.TextView;

import com.geekcamp.lesson32.data.models.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface HerokuApi {
    @GET("/posts")
    Call<List<Post>> getPosts();

    @POST("/posts")
    Call<Post> createPost(
            @Body Post post
    );

    @DELETE("/posts/{id}")
    Call<Post> deletePosts(
            @Path("id") int id
    );
    @PUT("/posts/{id}")
    Call<Post> updatePosts(
            @Path("id") int id, @Body Post post
    );



}
