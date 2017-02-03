package com.lecrec.lecrec.services;
import com.lecrec.lecrec.models.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface UserService {
    @FormUrlEncoded
    @POST("users")
    Call<User> getUserOrCreate(
            @Field("username") String userId,
            @Field("first_name") String userName
    );
}