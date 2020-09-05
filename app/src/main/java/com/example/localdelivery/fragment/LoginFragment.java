package com.example.localdelivery.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.localdelivery.Interface.JsonApiHolder;
import com.example.localdelivery.repository.NearbyShopsRepository;
import com.example.localdelivery.utils.PrefUtils;
import com.example.localdelivery.R;
import com.example.localdelivery.utils.RetrofitInstance;
import com.example.localdelivery.activity.MainActivity;
import com.example.localdelivery.model.LoginData;
import com.example.localdelivery.model.LoginResponse;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Objects;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Converter;

public class LoginFragment extends Fragment {

    private EditText editTextMobileNumber;
    private EditText editTextPassword;
    private ImageView imageViewButtonLogin;
    private TextView textViewSignUp;
    private ProgressBar progressBar;
    private JsonApiHolder jsonApiHolder;
    private PrefUtils prefUtils;
    private Context mContext;
    private Activity mActivity;
    private CompositeDisposable disposable = new CompositeDisposable();
    private View viewBlurr;

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
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        prefUtils = new PrefUtils(mContext);
        jsonApiHolder = RetrofitInstance.getRetrofitInstance(mContext).create(JsonApiHolder.class);
        setView(view);
        setClickListeners();
        return view;
    }

    private void setView(View view) {
        editTextMobileNumber = view.findViewById(R.id.editTextMobileNumberLogin);
        editTextPassword = view.findViewById(R.id.editTextPasswordLogin);
        imageViewButtonLogin = view.findViewById(R.id.buttonLogin);
        textViewSignUp = view.findViewById(R.id.textViewSignUpLogin);
        progressBar = view.findViewById(R.id.progressBarLogin);
        viewBlurr = view.findViewById(R.id.blurr_screen_login);
    }

    private void alertBox(String title, String message) {
        AlertDialog.Builder builder =new AlertDialog.Builder(mContext);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void setClickListeners() {
        imageViewButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobileNumber = editTextMobileNumber.getText().toString().trim();
                if(!validateMobileNumber(mobileNumber)) {
                    return;
                }

                String password = editTextPassword.getText().toString().trim();
                if(!validatePassword(password)) {
                    return;
                }

                login(mobileNumber, password);
            }
        });

        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_sign_up_login ,
                        new SignUpFragment()).commit();
            }
        });
    }

    private boolean validateMobileNumber(String mobileNumber) {
        if(mobileNumber.length() == 0) {
            Toast.makeText(mContext, "Mobile Number can't be empty !", Toast.LENGTH_LONG).show();
            return false;
        }
        if(mobileNumber.length()<10) {
            Toast.makeText(mContext, "Wrong Mobile Number entered !", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean validatePassword(String password) {
        if(password.length()==0) {
            Toast.makeText(mContext, "Password field cannot be empty !", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void login(final String mobileNumber, final String password) {
        final LoginData loginData = new LoginData(mobileNumber, password);
        progressBar.setVisibility(View.VISIBLE);
        viewBlurr.setVisibility(View.VISIBLE);
        mActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        disposable.add(
                jsonApiHolder.login(loginData)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<LoginResponse>() {
                            @Override
                            public void onSuccess(LoginResponse loginResponse) {
                                    prefUtils.createLogin("JWT "+loginResponse.getToken());
                                    prefUtils.setUserId(loginResponse.getUserId());
                                    prefUtils.setName(loginResponse.getName());
                                    prefUtils.setContactNumber(mobileNumber);
                                    prefUtils.setPassword(password);
                                    progressBar.setVisibility(View.GONE);
                                    viewBlurr.setVisibility(View.GONE);
                                    mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    getParentFragmentManager().beginTransaction().replace(R.id.fragment_sign_up_login,
                                        new GpsFragment()).commit();
                            }

                            @Override
                            public void onError(Throwable e) {
                                progressBar.setVisibility(View.GONE);
                                viewBlurr.setVisibility(View.GONE);
                                mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                if (e instanceof HttpException) {
                                    Converter<ResponseBody, ErrorMessage> errorConverter =
                                            RetrofitInstance.getRetrofitInstance(mContext)
                                                    .responseBodyConverter(ErrorMessage.class, new Annotation[0]);
                                    ErrorMessage errorBody = null;
                                    try {
                                        assert ((HttpException) e).response().errorBody() != null;
                                        errorBody = errorConverter.convert(((HttpException) e).response().errorBody());
                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                    }
                                    Log.e("TAG", "onError: " + ((HttpException) e).code());
                                    if(errorBody.getError().equals("Wrong password!")) {
                                        alertBox("Validation Error", "Wrong Password Entered !");
                                    }
                                    else if(errorBody.getError().equals("A user with this phone number could not be found.")) {
                                        alertBox("Validation Error", "Please Sign Up, " +
                                                "this number does not exists !");
                                    }
                                    else if(errorBody.getError().equals("User is not verified")) {
                                        Log.e("TAG", "onError: " + errorBody.getUserId());
                                        getFragmentManager().beginTransaction().replace(R.id.fragment_sign_up_login,
                                                new OtpFragment("name", errorBody.getUserId(), mobileNumber, password)).commit();
                                    }
                                    else {
                                        Toast.makeText(mContext, "An Error Occurred !", Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                }
                                else {
                                    Toast.makeText(mContext, "An Error Occurred !", Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }
                            }));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }

    private static class ErrorMessage {
        private String error;
        private String userId;

        public ErrorMessage(String error, String userId) {
            this.error = error;
            this.userId = userId;
        }

        public ErrorMessage(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
