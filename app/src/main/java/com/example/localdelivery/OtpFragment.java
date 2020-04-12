package com.example.localdelivery;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpFragment extends Fragment {

    private String userId;
    private EditText editTextOtp;
    private Button buttonSubmitOtp;
    private JsonApiHolder jsonApiHolder;
    private PrefUtils prefUtils;

    public OtpFragment(String userId) {
        this.userId = userId;
    }

    public OtpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_otp, container, false);
        jsonApiHolder = RetrofitInstance.getRetrofitInstance().create(JsonApiHolder.class);
        prefUtils = new PrefUtils(getContext());
        editTextOtp = view.findViewById(R.id.editTextOtp);
        buttonSubmitOtp = view.findViewById(R.id.buttonSubmitOtp);
        buttonSubmitOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = editTextOtp.getText().toString().trim();
                verifyOtp(otp);
            }
        });
        return view;
    }

    private void verifyOtp(String otp) {
        OtpData otpData = new OtpData(otp);
        Call<OtpResponse> call = jsonApiHolder.verifyOtp(userId, otpData);

        call.enqueue(new Callback<OtpResponse>() {
            @Override
            public void onResponse(@NotNull Call<OtpResponse> call, @NotNull Response<OtpResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
                    return;
                }
                OtpResponse otpResponse = response.body();
                if(otpResponse!=null) {
                    prefUtils.createLogin(otpResponse.getToken());
                    Intent intent = new Intent(getActivity() , MainActivity.class);
                    startActivity(intent);
                    Objects.requireNonNull(getActivity()).finish();
                }
            }

            @Override
            public void onFailure(@NotNull Call<OtpResponse> call, @NotNull Throwable t) {
                Toast.makeText(getContext(), "An Error Occurred!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
