package com.example.localdelivery.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.localdelivery.Interface.JsonApiHolder;
import com.example.localdelivery.R;
import com.example.localdelivery.activity.MainActivity;
import com.example.localdelivery.activity.MapsActivity;
import com.example.localdelivery.activity.ShopDetailActivity;
import com.example.localdelivery.activity.SignUpLoginActivity;
import com.example.localdelivery.model.LoginResponse;
import com.example.localdelivery.model.ProfileData;
import com.example.localdelivery.utils.PrefUtils;
import com.example.localdelivery.utils.RetrofitInstance;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class ProfileFragment extends Fragment {

    private EditText editTextUsername;
    private TextView textViewContactNumber;
    private TextView textViewAlphabetName;
    private EditText editTextAddress;
    private CardView cardViewFeedback;
    private CardView cardViewLogout;
    private PrefUtils prefUtils;
    private Context mContext;
    private Activity mActivity;
    private TextView textViewEdit;
    private TextView textViewDone;
    private View viewBlurr;
    private boolean isEditTextChanged = true;
    private CompositeDisposable disposable = new CompositeDisposable();
    private JsonApiHolder jsonApiHolder;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        prefUtils = new PrefUtils(mContext);
        jsonApiHolder = RetrofitInstance.getRetrofitInstance(mContext).create(JsonApiHolder.class);
        setView(view);
        setClickListeners();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //for not calling maps activity in edit text changed listener
        isEditTextChanged = false;

        editTextAddress.setText(prefUtils.getAddress());
    }

    private void setView(View view) {
        editTextUsername = view.findViewById(R.id.editTextNameProfile);
        textViewContactNumber = view.findViewById(R.id.textViewNumberProfile);
        textViewAlphabetName = view.findViewById(R.id.textViewNameAlphabetProfile);
        editTextAddress = view.findViewById(R.id.editTextAddressProfile);
        cardViewFeedback = view.findViewById(R.id.card_view_feedback);
        cardViewLogout = view.findViewById(R.id.card_view_logout);
        textViewEdit = view.findViewById(R.id.textViewEdit);
        textViewDone = view.findViewById(R.id.textViewDone);
        viewBlurr = view.findViewById(R.id.blurr_screen_profile);

        if (prefUtils.getNAME() != null) {
            String firstAlphabet = String.valueOf(prefUtils.getNAME().charAt(0));
            textViewAlphabetName.setText(firstAlphabet.toUpperCase());
            editTextUsername.setText(prefUtils.getNAME());
        }
        textViewContactNumber.setText(prefUtils.getContactNumber());

        editTextUsername.setEnabled(false);
        editTextAddress.setEnabled(false);
    }

    private void setClickListeners() {
        textViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextUsername.setEnabled(true);
                editTextAddress.setEnabled(true);
                textViewEdit.setVisibility(View.GONE);
                textViewDone.setVisibility(View.VISIBLE);
                viewBlurr.setVisibility(View.VISIBLE);
            }
        });

        textViewDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextUsername.setEnabled(false);
                editTextAddress.setEnabled(false);
                textViewEdit.setVisibility(View.VISIBLE);
                textViewDone.setVisibility(View.GONE);
                viewBlurr.setVisibility(View.GONE);
                editAddress();
            }
        });

        editTextAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isEditTextChanged) {

                    int originalLength = prefUtils.getAddress().length();
                    int newLength = s.toString().length();
                    //to not open maps more than once
                    if (originalLength == (newLength - 1) || (originalLength == (newLength + 1))) {
                        Intent intent = new Intent(mContext, MapsActivity.class);
                        startActivity(intent);
                    }
                }
                isEditTextChanged = true;
            }
        });

        cardViewFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.frame_layout_shops,
                        new ReviewFragment(true)).addToBackStack(null).commit();
            }
        });

        cardViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlertBox();
            }
        });
    }

    private void setAlertBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                prefUtils.logoutUser();
                Intent intent = new Intent(mContext, SignUpLoginActivity.class);
                startActivity(intent);
                mActivity.finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void editAddress() {

        final ProfileData profileData = new ProfileData(prefUtils.getAddress(), prefUtils.getLatitude(),
                prefUtils.getLongitude(), editTextUsername.getText().toString().trim());

        disposable.add(
                jsonApiHolder.editProfile(profileData)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ResponseBody>() {
                            @Override
                            public void onSuccess(ResponseBody responseBody) {
                                Toast.makeText(mContext, "Profile Updated Successfully !",
                                        Toast.LENGTH_SHORT).show();
                                prefUtils.setAddress(profileData.getAddress());
                                prefUtils.setLatitude(profileData.getLatitude());
                                prefUtils.setLongitude(profileData.getLongitude());
                                prefUtils.setName(profileData.getName());
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(mContext, "An Error Occurred !",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }));
    }
}