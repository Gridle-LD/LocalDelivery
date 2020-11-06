package com.example.localdelivery.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.localdelivery.Interface.JsonApiHolder;
import com.example.localdelivery.utils.OtpReceiver;
import com.example.localdelivery.utils.PrefUtils;
import com.example.localdelivery.R;
import com.example.localdelivery.utils.RetrofitInstance;
import com.example.localdelivery.activity.MainActivity;
import com.example.localdelivery.model.OtpData;
import com.example.localdelivery.model.OtpResponse;
import com.example.localdelivery.model.ResendOtpResponse;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class OtpFragment extends Fragment implements TextWatcher, View.OnKeyListener, View.OnFocusChangeListener{

    private String username;
    private String userId;
    private String mobileNumber;
    private String password;
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
    private CompositeDisposable disposable = new CompositeDisposable();
    private ImageView imageViewOtpVerifyScreen;
    private int otpRequestCode = 10;
    private static OtpFragment instance;

    public OtpFragment(String username, String userId, String mobileNumber, String password) {
        this.username = username;
        this.userId = userId;
        this.mobileNumber = mobileNumber;
        this.password = password;
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
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        jsonApiHolder = RetrofitInstance.getRetrofitInstance(mContext).create(JsonApiHolder.class);
        prefUtils = new PrefUtils(mContext);
        instance = this;

        setView(view);
        setListeners();
        startTimer();
        setClickListeners();

        return view;
    }

    public static OtpFragment getInstance() {
        return instance;
    }

    private void setView(View view) {
        editText1 = view.findViewById(R.id.editText);
        editText2 = view.findViewById(R.id.editText2);
        editText3 = view.findViewById(R.id.editText3);
        editText4 = view.findViewById(R.id.editText4);

        textViewMobileNumber = view.findViewById(R.id.textViewMobileNumberOtp);
        imageViewSubmitOtp = view.findViewById(R.id.imageView15);
        resend_otp_button = view.findViewById(R.id.imageView16);
        timer_text_view = view.findViewById(R.id.timer_text_view);
        resend_otp_text = view.findViewById(R.id.resend_otp_text);
        imageViewOtpVerifyScreen = view.findViewById(R.id.otp_verify_screen);

        //displaying mobile number
        textViewMobileNumber.setText(mobileNumber);

        //initially hiding the resend otp button
        resend_otp_button.setVisibility(View.INVISIBLE);

        requestSmsPermission();
    }

    private void setClickListeners() {
        imageViewSubmitOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    private void setListeners()
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

    private void requestSmsPermission() {
        if(ContextCompat.checkSelfPermission(mContext, Manifest.permission.RECEIVE_SMS) !=
                PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS},
                    otpRequestCode);
            return;
        }

        new OtpReceiver().setEditText(editText1, editText2, editText3, editText4);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == otpRequestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new OtpReceiver().setEditText(editText1, editText2, editText3, editText4);
            }
        }
    }

    public void verifyOtp(String otp) {
        OtpData otpData = new OtpData(otp);

        disposable.add(
                jsonApiHolder.verifyOtp(userId, otpData)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<OtpResponse>() {

                            @Override
                            public void onSuccess(OtpResponse otpResponse) {
                                prefUtils.createLogin("JWT "+otpResponse.getToken());
                                prefUtils.setName(username);
                                prefUtils.setUserId(userId);
                                prefUtils.setContactNumber(mobileNumber);
                                prefUtils.setPassword(password);
                                imageViewOtpVerifyScreen.setVisibility(View.GONE);
                                getParentFragmentManager().beginTransaction().replace(R.id.fragment_sign_up_login,
                                        new GpsFragment()).commit();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(mContext, "An Error Occurred !", Toast.LENGTH_SHORT).show();
                            }
                        }));
    }

    private void resendOtp() {

        disposable.add(
                jsonApiHolder.resendOtp(userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ResendOtpResponse>() {
                            @Override
                            public void onSuccess(ResendOtpResponse resendOtpResponse) {
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
                            public void onError(Throwable e) {
                                Toast.makeText(mContext, "An Error Occurred !", Toast.LENGTH_SHORT).show();
                            }
                        }));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
