package com.example.localdelivery.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import com.example.localdelivery.utils.PrefUtils;
import com.example.localdelivery.R;
import com.example.localdelivery.utils.RetrofitInstance;
import com.example.localdelivery.activity.MainActivity;
import com.example.localdelivery.model.LoginData;
import com.example.localdelivery.model.LoginResponse;
import java.util.Objects;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

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
                assert getFragmentManager() != null;
                getFragmentManager().beginTransaction().replace(R.id.fragment_sign_up_login ,
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

    private void login(String mobileNumber, String password) {
        final LoginData loginData = new LoginData(mobileNumber, password);
        progressBar.setVisibility(View.VISIBLE);
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
                                progressBar.setVisibility(View.GONE);
                                mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                Intent intent = new Intent(mActivity , MainActivity.class);
                                startActivity(intent);
                                Objects.requireNonNull(mActivity).finish();
                            }

                            @Override
                            public void onError(Throwable e) {
                                progressBar.setVisibility(View.GONE);
                                mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                if(e.getMessage().equals("Wrong password!")) {
                                    Toast.makeText(mContext, "Wrong password!", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(mContext, "An Error Occurred !", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
