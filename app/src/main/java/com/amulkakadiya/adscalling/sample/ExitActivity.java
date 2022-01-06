package com.amulkakadiya.adscalling.sample;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.amulkakadiya.AdsCalling.CommonAdsUtility;
import com.amulkakadiya.adscalling.R;


public class ExitActivity extends AppCompatActivity {

    ImageView iv_yes, iv_no;
    CommonAdsUtility oCommonAdsUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_exit);

        oCommonAdsUtility = new CommonAdsUtility(ExitActivity.this);

        iv_no = findViewById(R.id.no);
        iv_yes = findViewById(R.id.yes);

        iv_no.setOnClickListener(view -> {
            if (CommonAdsUtility.call_back_from == 1){
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }else{
                Intent intent = new Intent(getApplicationContext(), AdStartActivity.class);
                startActivity(intent);
            }

        });
        iv_yes.setOnClickListener(view -> {
            finishAffinity();
        });
    }
}