package com.example.localdelivery;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpFragment extends Fragment {

    private TextView textViewLogin;
    private ImageView buttonSignUp;
    private EditText editTextNameSignUp;
    private EditText editTextMobileNumberSignUp;
    private EditText editTextCreatePasswordSignUp;
    private EditText editTextConfirmPasswordSignUp;
    private JsonApiHolder jsonApiHolder;

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        jsonApiHolder = RetrofitInstance.getRetrofitInstance().create(JsonApiHolder.class);
        textViewLogin = view.findViewById(R.id.textViewLoginSignUp);
        buttonSignUp = view.findViewById(R.id.buttonSignUp);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextNameSignUp = view.findViewById(R.id.editTextNameSignUp);
                editTextMobileNumberSignUp = view.findViewById(R.id.editTextMobileNumberSignUp);
                editTextCreatePasswordSignUp = view.findViewById(R.id.editTextCreatePasswordSignUp);
                editTextConfirmPasswordSignUp = view.findViewById(R.id.editTextConfirmPasswordSignUp);
                String name = editTextNameSignUp.getText().toString().trim();
                if(name.equals("")) {
                    Toast.makeText(getContext(), "Name cannot be empty!", Toast.LENGTH_LONG).show();
                    return ;
                }
                String mobileNumber = editTextMobileNumberSignUp.getText().toString().trim();
                if(mobileNumber.length()<10) {
                    Toast.makeText(getContext(), "Wrong Mobile Number entered!", Toast.LENGTH_LONG).show();
                    return;
                }
                String createPassword = editTextCreatePasswordSignUp.getText().toString().trim();
                String confirmPassword = editTextConfirmPasswordSignUp.getText().toString().trim();
                if(!createPassword.equals(confirmPassword)) {
                    Toast.makeText(getContext(), "Passwords do not match!", Toast.LENGTH_LONG).show();
                    return;
                }
                signUp(name, mobileNumber, confirmPassword);
            }
        });
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert getFragmentManager() != null;
                getFragmentManager().beginTransaction().replace(R.id.fragment_sign_up_login ,
                        new LoginFragment())
                        .commit();
            }
        });
        return view;
    }

    private void signUp(String name, String mobileNumber, String password) {
        SignUpData signUpData = new SignUpData(name, password, mobileNumber);
        Call<SignUpResponse> call = jsonApiHolder.signUp(signUpData);

        call.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(@NotNull Call<SignUpResponse> call,
                                   @NotNull Response<SignUpResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
                    return;
                }
                SignUpResponse signUpResponse = response.body();
                assert getFragmentManager() != null;
                assert signUpResponse != null;
                getFragmentManager().beginTransaction().replace(R.id.fragment_sign_up_login,
                        new OtpFragment(signUpResponse.getUserId())).commit();
            }

            @Override
            public void onFailure(@NotNull Call<SignUpResponse> call, @NotNull Throwable t) {
                Toast.makeText(getContext(), "An Error Occurred!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
