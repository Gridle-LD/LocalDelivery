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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.localdelivery.R;
import com.example.localdelivery.activity.MainActivity;
import com.example.localdelivery.activity.ShopDetailActivity;
import com.example.localdelivery.activity.SignUpLoginActivity;
import com.example.localdelivery.utils.PrefUtils;

public class ProfileFragment extends Fragment {

    private EditText editTextUsername;
    private TextView textViewContactNumber;
    private EditText editTextAddress;
    private CardView cardViewFeedback;
    private CardView cardViewLogout;
    private PrefUtils prefUtils;
    private Context mContext;
    private Activity mActivity;

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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        prefUtils = new PrefUtils(mContext);
        setView(view);
        setClickListeners();
        return view;
    }

    private void setView(View view) {
        editTextUsername = view.findViewById(R.id.editTextNameProfile);
        textViewContactNumber = view.findViewById(R.id.textViewNumberProfile);
        editTextAddress = view.findViewById(R.id.editTextAddressProfile);
        cardViewFeedback = view.findViewById(R.id.card_view_feedback);
        cardViewLogout = view.findViewById(R.id.card_view_logout);

        editTextUsername.setText(prefUtils.getNAME());
        textViewContactNumber.setText(prefUtils.getContactNumber());
        editTextAddress.setText(prefUtils.getAddress());
    }

    private void setClickListeners() {
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
        AlertDialog.Builder builder =new AlertDialog.Builder(mContext);
        builder.setTitle("Logout");
        builder.setMessage("Logout from this device ?");
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
}