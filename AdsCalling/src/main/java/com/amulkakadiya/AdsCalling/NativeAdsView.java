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
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.facebook.ads.NativeAdLayout;

@SuppressWarnings("UnusedDeclaration")
public class NativeAdsView extends LinearLayout {

    CommonAdsUtility oCommonAdsUtility;
   /* private static final int DEFAULT_PARENT_BACKGROUND = 0;
    private static final int DEFAULT_PRIMARY_COLOR = Color.BLACK;
    private static final int DEFAULT_PRIMARY_DARK_COLOR = Color.BLACK;

    private int mParent_back = DEFAULT_PARENT_BACKGROUND;
    private int mPrimary = DEFAULT_PRIMARY_COLOR;
    private int mPrimary_dark = DEFAULT_PRIMARY_DARK_COLOR;*/

    public NativeAdsView(Context context) {
        super(context);
    }

    public NativeAdsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("CustomViewStyleable")
    public NativeAdsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        View.inflate(context, R.layout.custom_native_layout, this);

        /*TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomAdsNative, defStyle, 0);

        mParent_back = a.getColor(R.styleable.CustomAdsNative_parent_background, DEFAULT_PARENT_BACKGROUND);
        mPrimary = a.getColor(R.styleable.CustomAdsNative_color_primary, DEFAULT_PRIMARY_COLOR);
        mPrimary_dark = a.getColor(R.styleable.CustomAdsNative_color_primary_dark, DEFAULT_PRIMARY_DARK_COLOR);
*/
        LinearLayout nativead_sub_container = findViewById(R.id.nativead_sub_container);
        NativeAdLayout native_fb_ad_container = findViewById(R.id.native_fb_ad_container);

        oCommonAdsUtility = new CommonAdsUtility((Activity) context);
        oCommonAdsUtility.callnativeadsload(context, nativead_sub_container, native_fb_ad_container);
    }

}
