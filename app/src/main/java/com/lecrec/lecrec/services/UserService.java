package com.lecrec.lecrec.services;
import com.lecrec.lecrec.models.User;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface UserService {
    @POST("login/")
    Call<User> login(
            @Query("user_id") String email,
            @Query("password") String password
    );
}