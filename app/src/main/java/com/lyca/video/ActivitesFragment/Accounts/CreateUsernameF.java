package com.lyca.video.ActivitesFragment.Accounts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.lyca.video.ActivitesFragment.SplashA;
import com.lyca.video.Constants;
import com.lyca.video.Models.UserModel;
import com.lyca.video.Models.UserRegisterModel;
import com.lyca.video.MainMenu.MainMenuActivity;
import com.lyca.video.R;
import com.lyca.video.ApiClasses.ApiLinks;
import com.lyca.video.ApiClasses.ApiRequest;
import com.lyca.video.Interfaces.Callback;
import com.lyca.video.SimpleClasses.DataParsing;
import com.lyca.video.SimpleClasses.Functions;
import com.lyca.video.SimpleClasses.Variables;

import org.json.JSONObject;

import io.paperdb.Paper;

// This fragment will get the username from the users
public class CreateUsernameF extends Fragment {
    View view;
    EditText usernameEdit;
    Button signUpBtn;
    UserRegisterModel userRegisterModel;
    SharedPreferences sharedPreferences;
    String fromWhere;
    TextView usernameCountTxt;

    public CreateUsernameF(String fromWhere) {
        this.fromWhere = fromWhere;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_name, container, false);

        Bundle bundle = getArguments();
        userRegisterModel = (UserRegisterModel) bundle.getSerializable("user_model");

        view.findViewById(R.id.goBack).setOnClickListener(v->{

                getActivity().onBackPressed();

        });

        sharedPreferences = Functions.getSharedPreference(getContext());
        usernameEdit = view.findViewById(R.id.username_edit);
        signUpBtn = view.findViewById(R.id.btn_sign_up);


        usernameCountTxt = view.findViewById(R.id.username_count_txt);

        InputFilter[] username_filters = new InputFilter[1];
        username_filters[0] = new InputFilter.LengthFilter(Constants.USERNAME_CHAR_LIMIT);
        usernameEdit.setFilters(username_filters);
        usernameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // check the username field length

                usernameCountTxt.setText(usernameEdit.getText().length() + "/" + Constants.USERNAME_CHAR_LIMIT);
                String txtName = usernameEdit.getText().toString();
                if (txtName.length() > 0) {
                    signUpBtn.setEnabled(true);
                    signUpBtn.setClickable(true);
                } else {
                    signUpBtn.setEnabled(false);
                    signUpBtn.setClickable(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        signUpBtn.setOnClickListener(v -> {
            // check validation and then call the signup api
                if (checkValidation()) {
                    call_api_for_sigup();
                }

        });

        return view;
    }

    private void call_api_for_sigup() {

        JSONObject parameters = new JSONObject();
        try {

            parameters.put("dob", "" + userRegisterModel.dateOfBirth);
            parameters.put("username", "" + usernameEdit.getText().toString());

            if (fromWhere.equals("fromEmail")) {
                parameters.put("email", "" + userRegisterModel.email);
                parameters.put("password", userRegisterModel.password);
            } else if (fromWhere.equals("fromPhone")) {
                parameters.put("phone", "" + userRegisterModel.phoneNo);
            } else if (fromWhere.equals("social")) {
                parameters.put("email", "" + userRegisterModel.email);
                parameters.put("social_id", "" + userRegisterModel.socailId);
                parameters.put("profile_pic", "" + userRegisterModel.picture);
                parameters.put("social", "" + userRegisterModel.socailType);
                parameters.put("first_name", "" + userRegisterModel.fname);
                parameters.put("last_name", "" + userRegisterModel.lname);
                parameters.put("auth_token", "" + userRegisterModel.authTokon);
                parameters.put("device_token", Variables.DEVICE);
            }

            Log.d(TAG, "call_api_for_sigup: email address is "+userRegisterModel.email);
            Log.d(TAG, "call_api_for_sigup: "+parameters.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        Functions.showLoader(getActivity(), false, false);
        ApiRequest.callApi(getActivity(), ApiLinks.registerUser, parameters, new Callback() {
            @Override
            public void onResponce(String resp) {
                Functions.cancelLoader();
                parseSignupData(resp);

            }

        });
    }


    private static final String TAG = "ROHITH";


    // if the signup successfull then this method will call and it store the user info in local
    public void parseSignupData(String loginData) {
        try {
            JSONObject jsonObject = new JSONObject(loginData);
            String code = jsonObject.optString("code");
            if (code.equals("200")) {
                JSONObject jsonObj = jsonObject.getJSONObject("msg");
                UserModel userDetailModel = DataParsing.getUserDataModel(jsonObj.optJSONObject("User"));
                if (fromWhere.equals("social")) {
                    userDetailModel.setAuthToken(userRegisterModel.authTokon);
                }
                Functions.storeUserLoginDataIntoDb(view.getContext(),userDetailModel);


                Functions.setUpMultipleAccount(view.getContext());

                getActivity().sendBroadcast(new Intent("newVideo"));


                Variables.reloadMyVideos = true;
                Variables.reloadMyVideosInner = true;
                Variables.reloadMyLikesInner = true;
                Variables.reloadMyNotification = true;

                if (Paper.book(Variables.MultiAccountKey).getAllKeys().size()>1)
                {
                    Intent intent=new Intent(view.getContext(), SplashA.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    view.getContext().startActivity(intent);
                }
                else
                {
                    getActivity().finish();
                    startActivity(new Intent(getActivity(), MainMenuActivity.class));
                }
            } else {
                Toast.makeText(getActivity(), "" + jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    // check the username validation here
    public boolean checkValidation() {

        String uname = usernameEdit.getText().toString();
        if (TextUtils.isEmpty(uname)) {
            usernameEdit.setError(view.getContext().getString(R.string.username_cant_empty));
            usernameEdit.setFocusable(true);
            return false;
        }
        if (uname.length() < 4 || uname.length() > 14) {
            usernameEdit.setError(view.getContext().getString(R.string.username_length_between_valid));
            usernameEdit.setFocusable(true);
            return false;
        }

        return true;
    }


}