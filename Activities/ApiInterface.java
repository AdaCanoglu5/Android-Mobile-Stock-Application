package com.sutock2;

import java.util.List;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @POST("user/login")
    Call<ResponseBody> loginUser(@Body User user);

    @POST("user/signup")
    Call<ResponseBody> signupUser(@Body User user);

    @GET("stocks/{stockSymbol}")
    Call<Stock> getStock(@Path("stockSymbol") String stockSymbol);

    @GET("stocks/getAll/symbols")
    Call<List<Stock>> getAllSymbols();
    @GET("stocks/getAll")
    Call<List<Stock>> getAllStock();

    @POST("user/{userId}/followStock")
    Call<User> followStock(@Path("userId") String userId, @Query("stockSymbol") String stockSymbol);

    @GET("news/byTicker/{tickerSymbol}")
    Call<List<News>> getNewsByTicker(@Path("tickerSymbol") String tickerSymbol);

    @POST("comments")
    Call<Comment> addComment(@Body CommentRequest commentRequest);

    @GET("comments/{stock_symbol}")
    Call<List<Comment>> getComments(@Path("stock_symbol") String stock_symbol);

    @GET("user/{userId}/followedStocks")
    Call<Set<Stock>> getFollowedStocks(@Path("userId") String userId);
    @FormUrlEncoded
    @POST("user/{userId}/unfollow")
    Call<User> unfollowStock(@Path("userId") String userId, @Field("stockSymbol") String stockSymbol);

    @GET("stocks/getIdBySymbol/{stockSymbol}")
    Call<String> getIdBySymbol(@Path("stockSymbol") String stockSymbol);

}
