package Interface;

import Model.NewUser;
import Model.User;
import ModelRequest.UserLogin;
import ModelRequest.UserProfile;
import ModelRequest.UserRegistration;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface IUser {
    @POST("login")
    Call<User> loginUser(@Body UserLogin userLogin);

    @POST("register")
    Call<NewUser> registerUser(@Body UserRegistration userRegistration);

    @GET("users/profile")
    Call<UserProfile> read_user_profile(@Header("Authorization") String token);
}
