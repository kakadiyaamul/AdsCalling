package com.amulkakadiya.adscalling.sample;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.amulkakadiya.AdsCalling.AfterInterstitialCallBack;
import com.amulkakadiya.AdsCalling.CommonAdsUtility;
import com.amulkakadiya.adscalling.R;

public class AdStartActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";
    public static final String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";

    CommonAdsUtility oCommonAdsUtility;
    public ImageView start_go_btn, share_btn;
    public String[] q_permission = {READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE};


    public boolean hasPermissions(Context context, String... strArr) {
        if (context == null || strArr == null) {
            return true;
        }
        for (String checkSelfPermission : strArr) {
            if (ContextCompat.checkSelfPermission(context, checkSelfPermission) != 0) {
                return false;
            }
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        CommonAdsUtility.call_back_from = 0;
        if (oCommonAdsUtility != null) {
            oCommonAdsUtility.interstitialAdShow(new AfterInterstitialCallBack() {
                @Override
                public void position() {
                    Intent backpress = new Intent(AdStartActivity.this, ExitActivity.class);
                    backpress.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(backpress);
                    finish();
                }
            });
            return;
        }
        Intent backpress = new Intent(AdStartActivity.this, ExitActivity.class);
        backpress.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(backpress);
        finish();
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.start_go_btn) {
            if (Build.VERSION.SDK_INT >= 23 && !hasPermissions(this, this.q_permission)) {
                callingpermission_class();
            } else {
                oCommonAdsUtility.interstitialAdShow(new AfterInterstitialCallBack() {
                    @Override
                    public void position() {
                        startActivity(new Intent(AdStartActivity.this, HomeActivity.class));
                    }
                });
            }
        }
    }

    public void callingpermission_class() {
        if (Build.VERSION.SDK_INT >= 23) {
            ActivityCompat.requestPermissions(this, q_permission, 1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_start);
        oCommonAdsUtility = new CommonAdsUtility(AdStartActivity.this);

        share_btn = findViewById(R.id.share_btn);
        start_go_btn = findViewById(R.id.start_go_btn);
        share_btn.setOnClickListener(this);
        start_go_btn.setOnClickListener(this);

        findViewById(R.id.share_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("image/*");
                    StringBuilder sb = new StringBuilder();
                    sb.append(getResources().getString(R.string.app_name));
                    sb.append(" Created By :");
                    sb.append("https://play.google.com/store/apps/details?id=" + getPackageName());
                    intent.putExtra("android.intent.extra.TEXT", sb.toString());
                    intent.putExtra("android.intent.extra.STREAM", Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), BitmapFactory.decodeResource(getResources(), R.drawable.banner), null, null)));
                    startActivity(Intent.createChooser(intent, "share Image using"));
                } catch (Exception e) {
                }
            }
        });

        findViewById(R.id.iv_reta).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingDialog(AdStartActivity.this);
            }
        });

        findViewById(R.id.iv_privecy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonAdsUtility.privacy_url != null && !CommonAdsUtility.privacy_url.equals("")) {
                    try {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(CommonAdsUtility.privacy_url));
                        startActivity(browserIntent);
                    } catch (ActivityNotFoundException unused) {
                        Toast.makeText(AdStartActivity.this, "Please Try Again!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(AdStartActivity.this, "Please Try Again!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public static void ratingDialog(Activity activity) {
        Intent i3 = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + activity.getPackageName()));
        activity.startActivity(i3);
    }
}
