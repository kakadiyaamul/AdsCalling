package com.amulkakadiya.AdsCalling;

import android.content.Intent;
import android.widget.ImageView;

import androidx.annotation.ColorInt;

public class Utility{
    public static int myColor = 0;
    public static Intent newintent ;

    public void setColor (@ColorInt int colorId){
        myColor = colorId;
    }

    public void setIntent(Intent callintent){
        newintent = callintent;
    }

}
