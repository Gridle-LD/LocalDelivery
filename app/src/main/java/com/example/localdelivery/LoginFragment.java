package com.example.localdelivery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private EditText editTextMobileNumber;
    private EditText editTextPassword;
    private ImageView imageViewButtonLogin;
    private JsonApiHolder jsonApiHolder;
    private PrefUtils prefUtils;
    private Context mContext;
    private Activity mActivity;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        if(context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        prefUtils = new PrefUtils(mContext);
        jsonApiHolder = RetrofitInstance.getRetrofitInstance().create(JsonApiHolder.class);
        editTextMobileNumber = view.findViewById(R.id.editTextMobileNumberLogin);
        editTextPassword = view.findViewById(R.id.editTextPasswordLogin);
        imageViewButtonLogin = view.findViewById(R.id.buttonLogin);

        imageViewButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobileNumber = editTextMobileNumber.getText().toString().trim();
                if(mobileNumber.length()<10) {
                    Toast.makeText(mContext, "Wrong Mobile Number entered !", Toast.LENGTH_LONG).show();
                    return;
                }
                String password = editTextPassword.getText().toString().trim();
                if(password.length()==0) {
                    Toast.makeText(mContext, "Password field cannot be empty !", Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                login(mobileNumber, password);
            }
        });

        TextView textViewSignUp = view.findViewById(R.id.textViewSignUpLogin);
        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert getFragmentManager() != null;
                getFragmentManager().beginTransaction().replace(R.id.fragment_sign_up_login ,
                        new SignUpFragment())
                        .commit();
            }
        });
        return view;
    }

    private void login(String mobileNumber, String password) {
        LoginData loginData = new LoginData(mobileNumber, password);
        Call<LoginResponse> call = jsonApiHolder.login(loginData);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NotNull Call<LoginResponse> call,
                                   @NotNull Response<LoginResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Something went wrong !", Toast.LENGTH_LONG).show();
                    return;
                }
                LoginResponse loginResponse = response.body();
                if(loginResponse!=null) {
                    prefUtils.createLogin(loginResponse.getToken());
                    Intent intent = new Intent(mActivity , MainActivity.class);
                    startActivity(intent);
                    Objects.requireNonNull(mActivity).finish();
                }
            }

            @Override
            public void onFailure(@NotNull Call<LoginResponse> call, @NotNull Throwable t) {
                Toast.makeText(mContext, "An Error Occurred !", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
