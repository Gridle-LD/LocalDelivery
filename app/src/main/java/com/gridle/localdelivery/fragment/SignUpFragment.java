package com.gridle.localdelivery.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.gridle.localdelivery.Interface.JsonApiHolder;
import com.gridle.localdelivery.R;
import com.gridle.localdelivery.utils.RetrofitInstance;
import com.gridle.localdelivery.model.SignUpData;
import com.gridle.localdelivery.model.SignUpResponse;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Converter;

public class SignUpFragment extends Fragment {

    private TextView textViewLogin;
    private ImageView buttonSignUp;
    private EditText editTextNameSignUp;
    private EditText editTextMobileNumberSignUp;
    private EditText editTextCreatePasswordSignUp;
    private EditText editTextConfirmPasswordSignUp;
    private ProgressBar progressBar;
    private View viewBlurr;
    private JsonApiHolder jsonApiHolder;
    private Context mContext;
    private Activity mActivity;
    private CompositeDisposable disposable = new CompositeDisposable();

    public SignUpFragment() {
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
        final View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        jsonApiHolder = RetrofitInstance.getRetrofitInstance(mContext).create(JsonApiHolder.class);
        setView(view);
        setClickListeners();
        return view;
    }

    private void setView(View view) {
        textViewLogin = view.findViewById(R.id.textViewLoginSignUp);
        buttonSignUp = view.findViewById(R.id.buttonSignUp);
        editTextNameSignUp = view.findViewById(R.id.editTextNameSignUp);
        editTextMobileNumberSignUp = view.findViewById(R.id.editTextMobileNumberSignUp);
        editTextCreatePasswordSignUp = view.findViewById(R.id.editTextCreatePasswordSignUp);
        editTextConfirmPasswordSignUp = view.findViewById(R.id.editTextConfirmPasswordSignUp);
        progressBar = view.findViewById(R.id.progressBarSignUp);
        viewBlurr = view.findViewById(R.id.view_blurr_sign_up);
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
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextNameSignUp.getText().toString().trim();
                if(!validateName(name)) {
                    return;
                }

                String mobileNumber = editTextMobileNumberSignUp.getText().toString().trim();
                if(!validateMobileNumber(mobileNumber)) {
                    return;
                }

                String createPassword = editTextCreatePasswordSignUp.getText().toString().trim();
                String confirmPassword = editTextConfirmPasswordSignUp.getText().toString().trim();
                if(!validatePassword(createPassword, confirmPassword)) {
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
    }

    private boolean validateName(String name) {
        if(name.equals("")) {
            Toast.makeText(mContext, "Name cannot be empty !", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean validateMobileNumber(String mobileNumber) {
        if(mobileNumber.length()<10) {
            Toast.makeText(mContext, "Wrong Mobile Number entered !", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
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

    private void signUp(final String name, final String mobileNumber, final String password) {
        SignUpData signUpData = new SignUpData(name, password, mobileNumber);

        progressBar.setVisibility(View.VISIBLE);
        viewBlurr.setVisibility(View.VISIBLE);
        mActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        disposable.add(
                jsonApiHolder.signUp(signUpData)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<SignUpResponse>() {
                            @Override
                            public void onSuccess(SignUpResponse signUpResponse) {
                                progressBar.setVisibility(View.GONE);
                                viewBlurr.setVisibility(View.GONE);
                                mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                assert getFragmentManager() != null;
                                assert signUpResponse != null;
                                getFragmentManager().beginTransaction().replace(R.id.fragment_sign_up_login,
                                        new OtpFragment(name, signUpResponse.getUserId(), mobileNumber, password)).commit();
                            }

                            @Override
                            public void onError(Throwable e) {
                                progressBar.setVisibility(View.GONE);
                                viewBlurr.setVisibility(View.GONE);
                                mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                Toast.makeText(mContext, "An Error Occurred !", Toast.LENGTH_SHORT).show();

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
                                    if(errorBody.getData().get(0).getMsg().equals("PhoneNumber already exists!")) {
                                        alertBox("Validation Error", "Please Login, " +
                                                "PhoneNumber already exists !");
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
        private List<DataModel> data;

        public ErrorMessage(String error, List<DataModel> data) {
            this.error = error;
            this.data = data;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public List<DataModel> getData() {
            return data;
        }

        public void setData(List<DataModel> data) {
            this.data = data;
        }

        public static class DataModel {
            private String value;
            private String msg;
            private String param;
            private String location;

            public DataModel(String value, String msg, String param, String location) {
                this.value = value;
                this.msg = msg;
                this.param = param;
                this.location = location;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public String getMsg() {
                return msg;
            }

            public void setMsg(String msg) {
                this.msg = msg;
            }

            public String getParam() {
                return param;
            }

            public void setParam(String param) {
                this.param = param;
            }

            public String getLocation() {
                return location;
            }

            public void setLocation(String location) {
                this.location = location;
            }
        }
    }
}
