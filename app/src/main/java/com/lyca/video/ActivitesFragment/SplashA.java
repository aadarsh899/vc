package com.lyca.video.ActivitesFragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;

import com.lyca.video.ActivitesFragment.Profile.Setting.NoInternetA;
import com.lyca.video.Interfaces.InternetCheckCallback;
import com.lyca.video.MainMenu.MainMenuActivity;
import com.lyca.video.Models.HomeModel;
import com.lyca.video.R;
import com.lyca.video.ApiClasses.ApiLinks;
import com.lyca.video.ApiClasses.ApiRequest;
import com.lyca.video.Interfaces.Callback;
import com.lyca.video.SimpleClasses.Functions;
import com.lyca.video.SimpleClasses.Variables;

import org.json.JSONObject;

import io.paperdb.Paper;

public class SplashA extends AppCompatActivity {

    CountDownTimer countDownTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        Functions.setLocale(Functions.getSharedPreference(SplashA.this).getString(Variables.APP_LANGUAGE_CODE,Variables.DEFAULT_LANGUAGE_CODE)
                , this, SplashA.class,false);
        setContentView(R.layout.activity_splash);


        apiCallHit();
    }

    private void apiCallHit() {
        callApiForGetad();
        if (Functions.getSharedPreference(this).getString(Variables.DEVICE_ID, "0").equals("0")) {
            callApiRegisterDevice();
        }
        else
            setTimer();
    }


    private void callApiForGetad() {

        JSONObject parameters = new JSONObject();
        ApiRequest.callApi(SplashA.this, ApiLinks.showVideoDetailAd, parameters, new Callback() {
            @Override
            public void onResponce(String resp) {
                try {
                    JSONObject jsonObject=new JSONObject(resp);
                    String code=jsonObject.optString("code");

                    if(code!=null && code.equals("200")){
                        JSONObject msg=jsonObject.optJSONObject("msg");
                        JSONObject video=msg.optJSONObject("Video");
                        JSONObject user=msg.optJSONObject("User");
                        JSONObject sound = msg.optJSONObject("Sound");
                        JSONObject pushNotification=user.optJSONObject("PushNotification");
                        JSONObject privacySetting=user.optJSONObject("PrivacySetting");
                        HomeModel item = Functions.parseVideoData(user, sound, video, privacySetting, pushNotification);
                        item.promote="1";
                        Paper.book(Variables.PromoAds).write(Variables.PromoAdsModel,item);
                    }
                    else
                    {
                        Paper.book(Variables.PromoAds).destroy();
                    }

                } catch (Exception e) {
                    Log.d(TAG, "onResponce: error is "+e.toString());
                    e.printStackTrace();
                }



            }
        });


    }

    private static final String TAG = "ROHITH";

    // show the splash for 3 sec
    public void setTimer() {
        countDownTimer = new CountDownTimer(2500, 500) {

            public void onTick(long millisUntilFinished) {
                // this will call on every 500 ms
            }

            public void onFinish() {
                Log.d(TAG, "onFinish: onfinish called");
                Intent intent = new Intent(SplashA.this, MainMenuActivity.class);

                if (getIntent().getExtras() != null) {

                    try {
                        // its for multiple account notification handling
                        String userId=getIntent().getStringExtra("receiver_id");
                        Functions.setUpSwitchOtherAccount(SplashA.this,userId);
                    }catch (Exception e){}

                    intent.putExtras(getIntent().getExtras());
                    setIntent(null);
                }

                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                finish();

            }
        }.start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(countDownTimer!=null)
        countDownTimer.cancel();
    }

    // register the device on server on application open
    public void callApiRegisterDevice() {
        Log.d(TAG, "callApiRegisterDevice: called registered device");
        String androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        JSONObject param = new JSONObject();
        try {
            param.put("key", androidId);

        } catch (Exception e) {
            e.printStackTrace();
        }

        ApiRequest.callApi(this, ApiLinks.registerDevice, param, new Callback() {
            @Override
            public void onResponce(String resp) {
                try {
                    Log.d(TAG, "onResponce: reponse code ");
//                    Log.d(TAG, "onResponce: reponse code is "+resp);
                    JSONObject jsonObject = new JSONObject(resp);
                    String code = jsonObject.optString("code");
//                    String resp2 = resp;
                    setTimer();

                    Log.d(TAG, "onResponce: called "+code);
                    if (code.equals("200")) {
                        setTimer();
                        JSONObject msg = jsonObject.optJSONObject("msg");
                        JSONObject Device = msg.optJSONObject("Device");
                        SharedPreferences.Editor editor2 = Functions.getSharedPreference(SplashA.this).edit();
                        editor2.putString(Variables.DEVICE_ID, Device.optString("id")).commit();
                    }
                    else
                    {
                        setTimer();
                    }

                } catch (Exception e) {
                    Log.d(TAG, "onResponce: redigitste rror is "+e.getLocalizedMessage());
                    e.printStackTrace();
                }


            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();


        Functions.RegisterConnectivity(this, new InternetCheckCallback() {
            @Override
            public void GetResponse(String requestType, String response) {
                if(response.equalsIgnoreCase("disconnected")) {
                    Log.d(TAG, "GetResponse: diconnected case");
                    connectionCallback.launch(new Intent(getApplicationContext(), NoInternetA.class));
                    overridePendingTransition(R.anim.in_from_bottom,R.anim.out_to_top);
                }
            }
        });
    }


    ActivityResultLauncher<Intent> connectionCallback = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data.getBooleanExtra("isShow",false))
                        {
                            apiCallHit();
                        }
                    }
                }
            });


    @Override
    protected void onPause() {
        super.onPause();
        Functions.unRegisterConnectivity(SplashA.this);
    }

}
