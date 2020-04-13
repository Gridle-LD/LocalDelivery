package com.example.localdelivery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpFragment extends Fragment implements TextWatcher, View.OnKeyListener, View.OnFocusChangeListener {

    private String userId;
    private String mobileNumber;
    private JsonApiHolder jsonApiHolder;
    private PrefUtils prefUtils;
    private TextView textViewMobileNumber;
    private EditText editText1, editText2, editText3, editText4;
    private int whoHasFocus;
    private char[] code = new char[4];
    private Context mContext;
    private Activity mActivity;
    private ImageView imageViewSubmitOtp;
    private ImageView resend_otp_button;
    private TextView timer_text_view;
    private CountDownTimer countDownTimer;
    private long timeLeft = 120000;
    private boolean timerRunning = false;
    private TextView resend_otp_text;

    public OtpFragment(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public OtpFragment(String userId, String mobileNumber) {
        this.userId = userId;
        this.mobileNumber = mobileNumber;
    }

    public OtpFragment() {
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
        View view = inflater.inflate(R.layout.fragment_otp, container, false);
        jsonApiHolder = RetrofitInstance.getRetrofitInstance().create(JsonApiHolder.class);
        prefUtils = new PrefUtils(mContext);
        editText1 = view.findViewById(R.id.editText);
        editText2 = view.findViewById(R.id.editText2);
        editText3 = view.findViewById(R.id.editText3);
        editText4 = view.findViewById(R.id.editText4);
        setListners();
        textViewMobileNumber = view.findViewById(R.id.textViewMobileNumberOtp);
        textViewMobileNumber.setText(mobileNumber);
        imageViewSubmitOtp = view.findViewById(R.id.imageView15);
        resend_otp_button = view.findViewById(R.id.imageView16);
        resend_otp_button.setVisibility(View.INVISIBLE);
        timer_text_view = view.findViewById(R.id.timer_text_view);
        resend_otp_text = view.findViewById(R.id.resend_otp_text);
        startTimer();

        imageViewSubmitOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getFragmentManager().beginTransaction().replace(R.id.fragment_sign_up_login,
//                        new SignUpFragment()).commit();
                String otp="";
                for(int i=0; i<code.length; i++) {
                    if(code[i]!='\u0000') {
                        otp = otp + code[i];
                    }
                }
                if(otp.length()<4) {
                    Toast.makeText(mContext, "Fill all the fields !", Toast.LENGTH_LONG).show();
                    return;
                }
                stopTimer();
                timer_text_view.setVisibility(View.INVISIBLE);
                resend_otp_text.setVisibility(View.INVISIBLE);
                verifyOtp(otp);
            }
        });

        resend_otp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendOtp();
            }
        });
        return view;
    }

    private void stopTimer() {

        countDownTimer.cancel();
        timerRunning = false;
    }

    private void startTimer() {

        countDownTimer = new CountDownTimer(timeLeft , 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                updateTimer();

            }

            @Override
            public void onFinish() {

            }
        }.start();
        timerRunning = true;
    }

    private void updateTimer() {

        int minutes = (int) timeLeft / 60000;
        int seconds = (int) timeLeft % 60000 / 1000;
        String timeLeftText  = "";

        timeLeftText += "" + minutes;
        timeLeftText += ":";
        if(seconds < 10 ){
            timeLeftText += "0";
        }
        timeLeftText += seconds;
        timer_text_view.setText(timeLeftText);
        if(timeLeftText.equals("0:00")){
            timer_text_view.setVisibility(View.INVISIBLE);
            resend_otp_text.setVisibility(View.INVISIBLE);
            resend_otp_button.setVisibility(View.VISIBLE);
            editText1.setVisibility(View.INVISIBLE);
            editText2.setVisibility(View.INVISIBLE);
            editText3.setVisibility(View.INVISIBLE);
            editText4.setVisibility(View.INVISIBLE);
        }

    }

    private void setListners()
    {
        editText1.addTextChangedListener(this);
        editText2.addTextChangedListener(this);
        editText3.addTextChangedListener(this);
        editText4.addTextChangedListener(this);

        editText1.setOnKeyListener(this);
        editText2.setOnKeyListener(this);
        editText3.setOnKeyListener(this);
        editText4.setOnKeyListener(this);

        editText1.setOnFocusChangeListener(this);
        editText2.setOnFocusChangeListener(this);
        editText3.setOnFocusChangeListener(this);
        editText4.setOnFocusChangeListener(this);

        editText1.requestFocus();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        switch (whoHasFocus)
        {
            case 1:
                if(!editText1.getText().toString().isEmpty())
                {
                    code[0]= editText1.getText().toString().charAt(0);
                    editText2.requestFocus();
                }
                break;

            case 2:
                if(!editText2.getText().toString().isEmpty())
                {
                    code[1]= editText2.getText().toString().charAt(0);
                    editText3.requestFocus();
                }
                break;

            case 3:
                if(!editText3.getText().toString().isEmpty())
                {
                    code[2]= editText3.getText().toString().charAt(0);
                    editText4.requestFocus();
                }
                break;

            case 4:
                if(!editText4.getText().toString().isEmpty())
                {
                    code[3]= editText4.getText().toString().charAt(0);
                }
                break;


            default:
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch(v.getId())
        {
            case R.id.editText:
                whoHasFocus=1;
                break;

            case R.id.editText2:
                whoHasFocus=2;
                break;

            case R.id.editText3:
                whoHasFocus=3;
                break;

            case R.id.editText4:
                whoHasFocus=4;
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN)
        {
            if (keyCode == KeyEvent.KEYCODE_DEL)
            {
                switch(v.getId())
                {
                    case R.id.editText2:
                        if (editText2.getText().toString().isEmpty())
                            editText1.requestFocus();
                        break;

                    case R.id.editText3:
                        if (editText3.getText().toString().isEmpty())
                            editText2.requestFocus();
                        break;

                    case R.id.editText4:
                        if (editText4.getText().toString().isEmpty())
                            editText3.requestFocus();
                        break;

                    default:
                        break;
                }
            }
        }
        return false;
    }

    private void verifyOtp(String otp) {
        OtpData otpData = new OtpData(otp);
        Call<OtpResponse> call = jsonApiHolder.verifyOtp(userId, otpData);

        call.enqueue(new Callback<OtpResponse>() {
            @Override
            public void onResponse(@NotNull Call<OtpResponse> call, @NotNull Response<OtpResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Something went wrong !", Toast.LENGTH_LONG).show();
                    return;
                }
                OtpResponse otpResponse = response.body();
                if(otpResponse!=null) {
                    prefUtils.createLogin(otpResponse.getToken());
                    Intent intent = new Intent(mActivity , MainActivity.class);
                    startActivity(intent);
                    Objects.requireNonNull(mActivity).finish();
                }
            }

            @Override
            public void onFailure(@NotNull Call<OtpResponse> call, @NotNull Throwable t) {
                Toast.makeText(mContext, "An Error Occurred !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resendOtp() {
        Call<ResendOtpResponse> call = jsonApiHolder.resendOtp(userId);
        call.enqueue(new Callback<ResendOtpResponse>() {
            @Override
            public void onResponse(@NotNull Call<ResendOtpResponse> call,
                                   @NotNull Response<ResendOtpResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Something went wrong !", Toast.LENGTH_LONG).show();
                    return;
                }
                resend_otp_button.setVisibility(View.INVISIBLE);
                resend_otp_text.setVisibility(View.VISIBLE);
                timer_text_view.setVisibility(View.VISIBLE);
                editText1.setVisibility(View.VISIBLE);
                editText2.setVisibility(View.VISIBLE);
                editText3.setVisibility(View.VISIBLE);
                editText4.setVisibility(View.VISIBLE);
                timeLeft = 120000;
                startTimer();
            }

            @Override
            public void onFailure(@NotNull Call<ResendOtpResponse> call, @NotNull Throwable t) {
                Toast.makeText(mContext, "An Error Occurred !", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
