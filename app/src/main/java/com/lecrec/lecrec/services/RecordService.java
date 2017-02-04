package com.lecrec.lecrec.services;

import com.lecrec.lecrec.models.Record;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;


public interface RecordService {
    @GET("records/{record_id}")
    Call<Record> getRecord(@Header("Authorization") String token, @Path("record_id") String id);

    @GET("records")
    Call<List<Record>> getRecords(@Header("Authorization") String token);

    @Multipart
    @POST("records")
    Call<Record> createRecord(
            @Header("Authorization") String token,
            @Part("title") String title,
            @Part("duration") String duration,
            @Part("filename") String filename,
            @Part("is_korean") Boolean isKorean,
            @Part("description") RequestBody description,
            @Part MultipartBody.Part file
    );

    @PUT("records/{record_id}")
    Call<Record> updateRecord(@Header("Authorization") String token, @Path("record_id") String id, @Field("title") String title, @Field("text") String text);

    @DELETE("records/{record_id}")
    Call<Void> deleteRecord(@Header("Authorization") String token, @Path("record_id") String id);
}