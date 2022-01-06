package com.amulkakadiya.adscalling.sample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amulkakadiya.AdsCalling.AfterInterstitialCallBack;
import com.amulkakadiya.AdsCalling.CommonAdsUtility;
import com.amulkakadiya.adscalling.R;


public class HomeActivity extends AppCompatActivity {


    CommonAdsUtility oCommonAdsUtility;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_drawer);

        oCommonAdsUtility = new CommonAdsUtility(HomeActivity.this);

        findViewById(R.id.call_intertitial).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //java code to show fullscreen ads
                oCommonAdsUtility.interstitialAdShow(new AfterInterstitialCallBack() {
                    @Override
                    public void position() {
                        Toast.makeText(HomeActivity.this, "FullScreen call done", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        CommonAdsUtility.call_back_from = 1;
        if (oCommonAdsUtility != null) {
            oCommonAdsUtility.interstitialAdShow(new AfterInterstitialCallBack() {
                @Override
                public void position() {
                    Intent backpress = new Intent(HomeActivity.this, ExitActivity.class);
                    backpress.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(backpress);
                    finish();
                }
            });
            return;
        }
        Intent backpress = new Intent(HomeActivity.this, ExitActivity.class);
        backpress.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(backpress);
        finish();
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

}
