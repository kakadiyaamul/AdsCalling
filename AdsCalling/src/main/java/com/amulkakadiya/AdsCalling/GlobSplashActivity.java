package com.amulkakadiya.AdsCalling;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;


public abstract class GlobSplashActivity extends AppCompatActivity {

    private CommonAdsUtility oCommonAdsUtility;

    protected abstract int setLayoutResourceId();
    protected abstract View setParentView();
    protected abstract String setParentUrl();
    protected abstract callingbacktointent setParentcallnext();

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        if (Build.VERSION.SDK_INT >= 24) {
            Window window = getWindow();
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(0);
            window.setNavigationBarColor(0);
        }
        setContentView(setLayoutResourceId());
        CommonAdsUtility.ADS_ID_URL = setParentUrl();

        oCommonAdsUtility = new CommonAdsUtility(GlobSplashActivity.this);
        LoadAdsdata();
        CommonAdsUtility.changeStatusBarColor(GlobSplashActivity.this, GlobSplashActivity.this.getWindow());


    }


    public void LoadAdsdata() {
        if (oCommonAdsUtility != null && oCommonAdsUtility.isNetworkAvailable(GlobSplashActivity.this)) {
            oCommonAdsUtility.getAdsIdData(GlobSplashActivity.this, new CommonAdsUtility.getadscalling() {
                @Override
                public void after_adsapi_call(boolean adsget) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            oCommonAdsUtility.openadsadmobcall(GlobSplashActivity.this, new CommonAdsUtility.getopenadsresponse() {
                                @Override
                                public void after_openadscall_call(boolean adsget) {
                                    setParentcallnext().callgetdataback();
                                }
                            });
                        }
                    }, 1000);
                }
            });

        } else {
            if (oCommonAdsUtility != null) {
                oCommonAdsUtility.snackbarwith_ActionView(GlobSplashActivity.this, setParentView(), "No Internet Connection!", "Try Again", new CommonAdsUtility.callsnackresponse() {
                    @Override
                    public void onsnackbarclick() {
                        LoadAdsdata();
                    }
                });
            }
        }
    }


    public interface callingbacktointent {
        public void callgetdataback();
    }
}
