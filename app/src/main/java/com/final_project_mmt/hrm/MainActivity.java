package com.final_project_mmt.hrm;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EmployeeAdapter adapter;
    private List<Employee> employeeList = new ArrayList<>();
    private Button btnAddEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerViewEmployees);
        btnAddEmployee = findViewById(R.id.btnAddEmployee);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new EmployeeAdapter(employeeList, new EmployeeAdapter.OnEmployeeActionListener() {
            @Override
            public void onEdit(Employee employee) {
                showEmployeeDialog(employee);
            }

            @Override
            public void onDelete(Employee employee) {
                deleteEmployeeFromServer(employee.getId());
            }
        });
        recyclerView.setAdapter(adapter);

        btnAddEmployee.setOnClickListener(v -> showEmployeeDialog(null));

        loadEmployeesFromServer();
    }

    private void loadEmployeesFromServer() {
        RetrofitClient.getApiService().getAllEmployees().enqueue(new Callback<ApiResponse<List<Employee>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Employee>>> call, Response<ApiResponse<List<Employee>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    employeeList.clear();
                    employeeList.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "Từ chối truy cập hoặc lỗi dữ liệu mạng!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Employee>>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Lỗi kết nối mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showEmployeeDialog(final Employee employee) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText etName = new EditText(this);
        etName.setHint("Nhập tên nhân viên");
        final EditText etPosition = new EditText(this);
        etPosition.setHint("Nhập chức vụ");
        final EditText etSalary = new EditText(this);
        etSalary.setHint("Nhập mức lương");
        etSalary.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);

        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(44, 24, 44, 24);
        layout.addView(etName);
        layout.addView(etPosition);
        layout.addView(etSalary);
        builder.setView(layout);

        if (employee != null) {
            builder.setTitle("Sửa thông tin nhân viên");
            etName.setText(employee.getName());
            etPosition.setText(employee.getPosition());
            etSalary.setText(String.valueOf(employee.getSalary()));
        } else {
            builder.setTitle("Thêm nhân viên mới");
        }

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String name = etName.getText().toString().trim();
            String position = etPosition.getText().toString().trim();
            String salaryStr = etSalary.getText().toString().trim();
            int salary = Integer.parseInt(salaryStr.isEmpty() ? "0" : salaryStr);

            if (name.isEmpty() || position.isEmpty()) {
                Toast.makeText(MainActivity.this, "Vui lòng điền đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (employee == null) {
                Employee newEmp = new Employee(name, position, salary);
                RetrofitClient.getApiService().createEmployee(newEmp).enqueue(new Callback<ApiResponse<Employee>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Employee>> call, Response<ApiResponse<Employee>> response) {
                        loadEmployeesFromServer();
                        Toast.makeText(MainActivity.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onFailure(Call<ApiResponse<Employee>> call, Throwable t) {}
                });
            } else {
                employee.setName(name);
                employee.setPosition(position);
                employee.setSalary(salary);
                RetrofitClient.getApiService().updateEmployee(employee.getId(), employee).enqueue(new Callback<ApiResponse<Employee>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Employee>> call, Response<ApiResponse<Employee>> response) {
                        loadEmployeesFromServer();
                        Toast.makeText(MainActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onFailure(Call<ApiResponse<Employee>> call, Throwable t) {}
                });
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void deleteEmployeeFromServer(String id) {
        RetrofitClient.getApiService().deleteEmployee(id).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                loadEmployeesFromServer();
                Toast.makeText(MainActivity.this, "Xóa nhân viên thành công!", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {}
        });
    }
}