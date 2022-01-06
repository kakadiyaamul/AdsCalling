/*
 * Copyright 2014 - 2020 Henning Dodenhof
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.amulkakadiya.AdsCalling;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;

@SuppressWarnings("UnusedDeclaration")
public class BannerAdsView extends LinearLayout {

    CommonAdsUtility oCommonAdsUtility;

    public BannerAdsView(Context context) {
        super(context);
    }

    public BannerAdsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("CustomViewStyleable")
    public BannerAdsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        View.inflate(context, R.layout.ad_banner_show, this);
        LinearLayout ad_unit_ll = findViewById(R.id.bannerad_unit);
        oCommonAdsUtility = new CommonAdsUtility((Activity) context);
        oCommonAdsUtility.showbannerads(ad_unit_ll);
    }

}
