package Interface;

import ModelRequest.TaskCreate;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

public interface ITask {
    @POST("/tasks/")
    Call<TaskCreate> create_task(@Body TaskCreate task);

    @GET("/tasks/")
    Call<List<TaskCreate>> read_tasks();

    @DELETE("/tasks/{task_id}")
    Call<TaskCreate> delete_task(@Path("task_id") int taskId);
}
