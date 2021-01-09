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
import com.gridle.localdelivery.model.ChangePasswordData;
import com.gridle.localdelivery.utils.PrefUtils;
import com.gridle.localdelivery.utils.RetrofitInstance;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class ChangePasswordFragment extends Fragment {

    private EditText editTextCreatePassword;
    private EditText editTextConfirmPassword;
    private ImageView imageViewLogin;
    private View viewBlurr;
    private ProgressBar progressBar;

    private String userId;
    private String phoneNumber;

    private Context mContext;
    private Activity mActivity;
    private JsonApiHolder jsonApiHolder;
    private PrefUtils prefUtils;
    private CompositeDisposable disposable = new CompositeDisposable();

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    public ChangePasswordFragment(String userId, String phoneNumber) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
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
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        jsonApiHolder = RetrofitInstance.getRetrofitInstance(mContext).create(JsonApiHolder.class);
        prefUtils = new PrefUtils(mContext);

        setView(view);
        setClickListeners();
        return view;
    }

    private void setView(View view) {
        editTextCreatePassword = view.findViewById(R.id.editTextCreatePasswordChangePassword);
        editTextConfirmPassword = view.findViewById(R.id.editTextConfirmPasswordChangePassword);
        imageViewLogin = view.findViewById(R.id.buttonLoginChangePassword);
        viewBlurr = view.findViewById(R.id.view_blurr_change_password);
        progressBar = view.findViewById(R.id.progressBarChangePassword);
    }

    private void setClickListeners() {
        imageViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String createPassword = editTextCreatePassword.getText().toString().trim();
                String confirmPassword = editTextConfirmPassword.getText().toString().trim();

                if(!validatePassword(createPassword, confirmPassword)) {
                    return;
                }

                changePassword(createPassword);
            }
        });
    }

    private boolean validatePassword(String createPassword, String confirmPassword) {
        if(createPassword.length()<6) {
            Toast.makeText(mContext, "Password length too small !", Toast.LENGTH_LONG).show();
            return false;
        }
        if(!createPassword.equals(confirmPassword)) {
            Toast.makeText(mContext, "Passwords do not match !", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void changePassword(final String password) {

        viewBlurr.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        mActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        disposable.add(
                jsonApiHolder.changePassword(new ChangePasswordData(phoneNumber, password))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ResponseBody>() {
                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull ResponseBody responseBody) {

                        viewBlurr.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

//                        prefUtils.setContactNumber(phoneNumber);
//                        prefUtils.setPassword(password);
//                        prefUtils.setUserId(userId);

                        getParentFragmentManager().beginTransaction().replace(R.id.fragment_sign_up_login,
                                new LoginFragment()).commit();
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