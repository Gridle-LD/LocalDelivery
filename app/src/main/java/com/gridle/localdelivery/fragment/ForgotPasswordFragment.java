package com.gridle.localdelivery.fragment;

import android.app.Activity;
import android.content.Context;
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
import android.widget.Toast;

import com.gridle.localdelivery.Interface.JsonApiHolder;
import com.gridle.localdelivery.R;
import com.gridle.localdelivery.model.ForgotPasswordData;
import com.gridle.localdelivery.model.SignUpResponse;
import com.gridle.localdelivery.utils.RetrofitInstance;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ForgotPasswordFragment extends Fragment {

    private EditText editTextNumber;
    private ImageView imageViewLogin;
    private ProgressBar progressBar;
    private View viewBlurr;

    private Context mContext;
    private Activity mActivity;
    private CompositeDisposable disposable = new CompositeDisposable();
    private JsonApiHolder jsonApiHolder;

    public ForgotPasswordFragment() {
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
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        jsonApiHolder = RetrofitInstance.getRetrofitInstance(mContext).create(JsonApiHolder.class);

        setView(view);
        setClickListeners();
        return view;
    }

    private void setView(View view) {
        editTextNumber = view.findViewById(R.id.editTextPhoneForgotPassword);
        imageViewLogin = view.findViewById(R.id.buttonLoginForgotPassword);
        viewBlurr = view.findViewById(R.id.view_blurr_forgot_password);
        progressBar = view.findViewById(R.id.progressBarForgotPassword);
    }

    private void setClickListeners() {
        imageViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = editTextNumber.getText().toString().trim();
                if(!validateMobileNumber(number)) {
                    return;
                }

                forgotPassword(number);
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

    private void forgotPassword(final String number) {
        ForgotPasswordData forgotPasswordData = new ForgotPasswordData(number);
        viewBlurr.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        mActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        disposable.add(
                jsonApiHolder.forgotPassword(forgotPasswordData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<SignUpResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull SignUpResponse signUpResponse) {
                        viewBlurr.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        getParentFragmentManager().beginTransaction().replace(R.id.fragment_sign_up_login,
                                new OtpFragment(signUpResponse.getUserId(), number)).commit();
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        viewBlurr.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(mContext, "An Error Occurred !", Toast.LENGTH_SHORT).show();
                    }
                })
        );
    }
}