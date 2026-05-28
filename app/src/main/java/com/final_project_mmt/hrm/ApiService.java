package com.final_project_mmt.hrm;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    @GET("api/v1/employees")
    Call<ApiResponse<List<Employee>>> getAllEmployees();

    @POST("api/v1/employees")
    Call<ApiResponse<Employee>> createEmployee(@Body Employee employee);

    @PUT("api/v1/employees/{id}")
    Call<ApiResponse<Employee>> updateEmployee(@Path("id") String id, @Body Employee employee);

    @DELETE("api/v1/employees/{id}")
    Call<ApiResponse<Void>> deleteEmployee(@Path("id") String id);
}