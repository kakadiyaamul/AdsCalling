package com.amulkakadiya.AdsCalling;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.material.snackbar.Snackbar;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CommonAdsUtility {

    public static int call_back_from = 0;
    public static int CALL_FULLSCREEN_after = 1;
    public static int CALL_CURRENT_FULLSCREEN = 0;

    public static String ADS_ID_URL = "";


    private Activity activity;
    public boolean personalization_ad = false;
    AdLoader adLoader;
    private List<com.google.android.gms.ads.nativead.NativeAd> mNativeAds = new ArrayList<>();
    NativeAd nativeAd;
    public final static int NATIVE_NUMBER_OF_ADS = 1;

    public static String Facebookbanner_id = "";
    public static String Facebookinstrial_id = "";
    public static String Facebooknative_id = "";

    public static String admobbanner_id = "";
    public static String admobinstrial_id = "";
    public static String admobnative_id = "";
    public static String admob_openAds_ID = "";

    public static int current_mobbanner_pos = 0;
    public static int current_mobfullscreen_pos = 0;
    public static int current_mobnative_pos = 0;

    public static ArrayList<String> list_admobbanner = new ArrayList<>();
    public static ArrayList<String> list_admobinstrial = new ArrayList<>();
    public static ArrayList<String> list_admobnative = new ArrayList<>();

    public static String ads_display = "";
    public static String privacy_url = "";
    public static String get_App_version = "";
    public static boolean show_ads_current = true;

    public static final String Pref_Facebookbanner_id = "FB_BANNER";
    public static final String Pref_Facebookinstrial_id = "FB_INTERSTITIAL";
    public static final String Pref_Facebooknative_id = "FB_NATIVE";
    public static final String Pref_admobbanner_id = "ADMOB_BANNER";
    public static final String Pref_admobinstrial_id = "ADMOB_INTERSTITIAL";
    public static final String Pref_admobnative_id = "ADMOB_NATIVE";
    public static final String Pref_admob_openAds_ID = "ADMOB_ADsOPEN";
    public static final String Pref_ads_display = "AD_DISPLAY_TYPE";
    public static final String Pref_privacy_url = "PRIVACY_URL";

    public static String API_URL = "";

    public static final String NATIVE_REGULAR = "regular_native";


    @SuppressLint("CommitPrefEdits")
    public CommonAdsUtility(Activity activity) {
        this.activity = activity;
    }


    @SuppressLint("MissingPermission")
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public void snackbar_CommonView(Context mcontext, View commonlayout, String snackmsg) {
        Snackbar snackbar = Snackbar.make(commonlayout, snackmsg + "", Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(ContextCompat.getColor(mcontext, R.color.colorAccent));
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextSize(16);
        textView.setTextColor(Color.BLACK);
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }


    public void snackbarwith_ActionView(Context mcontext, View commonlayout, String snackmsg, String Actionname, final callsnackresponse callsnack) {
        Snackbar snackbar = Snackbar.make(commonlayout, snackmsg + "", Snackbar.LENGTH_INDEFINITE).setAction(Actionname, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callsnack.onsnackbarclick();
            }
        });
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(ContextCompat.getColor(mcontext, R.color.colorAccent));
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextSize(16);
        textView.setTextColor(Color.BLACK);
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }

    public interface callsnackresponse {
        void onsnackbarclick();
    }

    //---------------Interstitial Ad---------------//


    public void interstitialAdShow(AfterInterstitialCallBack interfullAdView) {
        if (!show_ads_current) {
            interfullAdView.position();
            return;
        }
        if (CALL_CURRENT_FULLSCREEN >= CALL_FULLSCREEN_after) {
            CALL_CURRENT_FULLSCREEN = 0;
            Log.e("callingrequestadmob", "call show ads ");
            String Ads_type = ads_display.toLowerCase();
            if (Ads_type != null && (Ads_type.toLowerCase().equals("admob") || Ads_type.toLowerCase().equals("Admob"))) {
                if (list_admobinstrial != null && list_admobinstrial.size() > 0) {
                    loadAdmobeinterstitialads(Ads_type.toLowerCase(), interfullAdView);
                } else {
                    if (Facebookinstrial_id != null && !Facebookinstrial_id.equals("")) {
                        loadfacebookinterstitialads(Ads_type.toLowerCase(), interfullAdView);
                    } else {
                        interfullAdView.position();
                    }
                }

            } else if (Ads_type != null && (Ads_type.toLowerCase().equals("facebook") || Ads_type.toLowerCase().equals("Facebook"))) {
                if (Facebookinstrial_id != null && !Facebookinstrial_id.equals("")) {
                    Log.e("getresponsedata", ":::     call facebook");
                    loadfacebookinterstitialads(Ads_type.toLowerCase(), interfullAdView);
                } else {
                    if (list_admobinstrial != null && list_admobinstrial.size() > 0) {
                        loadAdmobeinterstitialads(Ads_type.toLowerCase(), interfullAdView);
                    } else {
                        interfullAdView.position();
                    }
                }

            } else {
                interfullAdView.position();
            }
        } else {
            Log.e("callingrequestadmob", "call else pass direct");
            CALL_CURRENT_FULLSCREEN++;
            interfullAdView.position();
        }
        Log.e("callinterstitial", "start");
    }

    static InterstitialAd interstitialAd;
    static AfterInterstitialCallBack interfullAdViewpre;
    static SpinMastApp mainappclass;

    public static void admobinitilizepreload(SpinMastApp activity) {
        mainappclass = activity;
        if (current_mobfullscreen_pos >= list_admobinstrial.size()) {
            current_mobfullscreen_pos = 0;
        }
        AdRequest adRequest;
        Bundle extras = new Bundle();
        extras.putString("npa", "1");
        adRequest = new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, extras).build();
        InterstitialAd.load(
                activity,
                list_admobinstrial.get(current_mobfullscreen_pos),
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstAd) {
                        Log.e("callingrequestadmob", "call ad load: ");
                        interstitialAd = interstAd;
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        admobinitilizepreload(mainappclass);

                                        interfullAdViewpre.position();
                                        // interstitialAd = null;
                                        Log.d("TAG", "The ad was dismissed.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when fullscreen content is shown.
                                        Log.d("TAG", "The ad was shown.");
                                    }
                                });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.e("callingrequestadmob", "call error: " + loadAdError.getMessage());
                        if (list_admobinstrial != null && list_admobinstrial.size() > 0) {
                            admobinitilizepreload(mainappclass);
                        }
                        interstitialAd = null;
                        // Handle the error
                        /*Log.i(TAG, loadAdError.getMessage());

                         */
                    }
                });
        current_mobfullscreen_pos++;
    }

    public void loadAdmobeinterstitialads(final String diplay_type, AfterInterstitialCallBack interfullAdView) {
        interfullAdViewpre = interfullAdView;
        if (interstitialAd != null) {
            interstitialAd.show(activity);
        } else {
            admobinitilizepreload(mainappclass);
            if (diplay_type.equals("admob")) {
                if (Facebookinstrial_id != null && !Facebookinstrial_id.equals("")) {
                    loadfacebookinterstitialads(diplay_type, interfullAdView);
                    return;
                }
            }
            interfullAdView.position();
        }
    }

    public void loadfacebookinterstitialads(final String diplay_type, final AfterInterstitialCallBack interfullAdView) {
        final ProgressGIFDialog.Builder progressDialog = new ProgressGIFDialog.Builder(activity);
        progressDialog.setLoadingGif(0);
        progressDialog.setLoadingTitle("");
        progressDialog.setCancelable(false);
        progressDialog.build();


        final com.facebook.ads.InterstitialAd interstitialAd = new com.facebook.ads.InterstitialAd(activity, Facebookinstrial_id);
        interstitialAd.buildLoadAdConfig().withAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                if (progressDialog != null) {
                    progressDialog.clear();
                }
                interfullAdView.position();
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                if (diplay_type.equals("facebook")) {
                    if (list_admobinstrial != null && list_admobinstrial.size() > 0) {
                        if (progressDialog != null) {
                            progressDialog.clear();
                        }
                        loadAdmobeinterstitialads(diplay_type, interfullAdView);
                        return;
                    }
                }
                if (progressDialog != null) {
                    progressDialog.clear();
                }
                interfullAdView.position();
            }

            @Override
            public void onAdLoaded(Ad ad) {
                if (progressDialog != null) {
                    progressDialog.clear();
                }
                interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });
        interstitialAd.loadAd();
    }

    //---------------Interstitial ad---------------//

    //---------------Native Listing Ad---------------//

    public void callnativeadsload(Context context, LinearLayout mnativead_container, NativeAdLayout fbnative_container) {
        if (!show_ads_current) {
            return;
        }
        String Ads_type = ads_display != null ? ads_display : "Admob";
        if (Ads_type != null && (Ads_type.toLowerCase().equals("admob") || Ads_type.toLowerCase().equals("Admob"))) {
            if (list_admobnative != null && list_admobnative.size() > 0) {
                loadnativeads(context, Ads_type.toLowerCase(), mnativead_container, fbnative_container);
            } else {
                if (Facebooknative_id != null && !Facebooknative_id.equals("")) {
                    loadFbNativeAd(context, Ads_type.toLowerCase(), mnativead_container, fbnative_container);
                }
            }
        } else if (Ads_type != null && (Ads_type.toLowerCase().equals("facebook") || Ads_type.toLowerCase().equals("Facebook"))) {
            if (Facebooknative_id != null && !Facebooknative_id.equals("")) {
                loadFbNativeAd(context, Ads_type.toLowerCase(), mnativead_container, fbnative_container);
            } else {
                if (list_admobnative != null && list_admobnative.size() > 0) {
                    loadnativeads(context, Ads_type.toLowerCase(), mnativead_container, fbnative_container);
                }
            }
        }

    }

    @SuppressLint("MissingPermission")
    public void loadnativeads(final Context context, final String display_type, final LinearLayout mnativead_container, final NativeAdLayout fbnative_container) {
        if (current_mobnative_pos >= list_admobnative.size()) {
            current_mobnative_pos = 0;
        }
        mNativeAds = new ArrayList<>();
        AdLoader.Builder builder = new AdLoader.Builder(context, list_admobnative.get(current_mobnative_pos));
        current_mobnative_pos++;
        adLoader = builder.forNativeAd(new com.google.android.gms.ads.nativead.NativeAd.OnNativeAdLoadedListener() {
            @Override
            public void onNativeAdLoaded(@NonNull com.google.android.gms.ads.nativead.NativeAd nativeAd) {
                mNativeAds.add(nativeAd);
                if (!adLoader.isLoading()) {
                    PassNativeadsLoadview(context, mnativead_container, fbnative_container, display_type);
                    // nativeadinter.nativeloadads(true, "admob");
                }
            }
        }).withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                if (display_type.equals("admob")) {
                    if (Facebooknative_id != null && !Facebooknative_id.equals("")) {
                        loadFbNativeAd(context, display_type, mnativead_container, fbnative_container);
                        return;
                    }
                }
                PassNativeadsLoadview(context, mnativead_container, fbnative_container, display_type);
            }
        }).build();
        // Load the Native ads.
        adLoader.loadAds(new AdRequest.Builder().build(), NATIVE_NUMBER_OF_ADS);
    }


    public void loadFbNativeAd(final Context context, final String display_type, final LinearLayout mnativead_container, final NativeAdLayout fbnative_container) {
        nativeAd = new NativeAd(context, Facebooknative_id);
        nativeAd.buildLoadAdConfig().withAdListener(new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {
                if (display_type.equals("facebook")) {
                    if (list_admobnative != null && list_admobnative.size() > 0) {
                        loadnativeads(context, display_type, mnativead_container, fbnative_container);
                        return;
                    }
                }
                PassNativeadsLoadview(context, mnativead_container, fbnative_container, display_type);
                //  nativeadinter.nativeloadads(false, "facebook");
            }

            @Override
            public void onAdLoaded(Ad ad) {
                PassNativeadsLoadview(context, mnativead_container, fbnative_container, display_type);
                // nativeadinter.nativeloadads(false, "facebook");
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });
        nativeAd.loadAd();
    }


    public List<com.google.android.gms.ads.nativead.NativeAd> shownativeads() {
        return mNativeAds;
    }

    public NativeAd showfbnativead() {
        return nativeAd;
    }

    //---------------Native Listing Ad---------------//

    //---------------Banner Ad---------------//

    public void showbannerads(final LinearLayout linearLayout) {
        if (!show_ads_current) {
            return;
        }
        String Ads_type = ads_display != null ? ads_display : "Admob";
        if (Ads_type != null && (Ads_type.toLowerCase().equals("admob") || Ads_type.toLowerCase().equals("Admob"))) {
            if (list_admobbanner != null && list_admobbanner.size() > 0) {
                showPersonalizedAds(Ads_type.toLowerCase(), linearLayout);
            } else {
                if (Facebookbanner_id != null && !Facebookbanner_id.equals("")) {
                    facebookbannerads(Ads_type.toLowerCase(), linearLayout);
                }
            }
        } else if (Ads_type != null && (Ads_type.toLowerCase().equals("facebook") || Ads_type.toLowerCase().equals("Facebook"))) {
            if (Facebookbanner_id != null && !Facebookbanner_id.equals("")) {
                facebookbannerads(Ads_type.toLowerCase(), linearLayout);
            } else {
                if (list_admobbanner != null && list_admobbanner.size() > 0) {
                    showPersonalizedAds(Ads_type.toLowerCase(), linearLayout);
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    public void showPersonalizedAds(final String display_type, final LinearLayout linearLayout) {
        if (current_mobbanner_pos >= list_admobbanner.size()) {
            current_mobbanner_pos = 0;
        }
        AdView adView = new AdView(activity);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.setAdUnitId(list_admobbanner.get(current_mobbanner_pos));
        current_mobbanner_pos++;
        // adView.setAdSize(AdSize.SMART_BANNER);
        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);
        linearLayout.removeAllViews();
        linearLayout.addView(adView);
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                if (display_type.equals("admob")) {
                    if (Facebookbanner_id != null && !Facebookbanner_id.equals("")) {
                        facebookbannerads(display_type, linearLayout);
                    }
                }
            }

            @Override
            public void onAdOpened() {
            }

            @Override
            public void onAdClicked() {
            }


            @Override
            public void onAdClosed() {
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }
        });

    }

    public void facebookbannerads(final String display_type, final LinearLayout linearLayout) {
        com.facebook.ads.AdView adView = new com.facebook.ads.AdView(activity, Facebookbanner_id, com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        linearLayout.removeAllViews();
        linearLayout.addView(adView);
        adView.loadAd();
        adView.buildLoadAdConfig().withAdListener(new com.facebook.ads.AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                if (display_type.equals("facebook")) {
                    if (list_admobbanner != null && list_admobbanner.size() > 0) {
                        showPersonalizedAds(display_type, linearLayout);
                    }
                }
            }

            @Override
            public void onAdLoaded(Ad ad) {

            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });
    }

    private AdSize getAdSize() {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
    }

    //---------------Banner Ad---------------//


    public void populatePassNativeAdView(com.google.android.gms.ads.nativead.NativeAd nativeAd, NativeAdView adView) {

        com.google.android.gms.ads.nativead.MediaView mediaView = adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());

        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        adView.setNativeAd(nativeAd);
        /*VideoController vc = nativeAd.getVideoController();
        if (vc.hasVideoContent()) {
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    super.onVideoEnd();
                }
            });
        } else {
        }*/
    }


    com.google.android.gms.ads.nativead.NativeAd mAdmobnativeAd;


    public void PassNativeadsLoadview(Context context, LinearLayout mnativead_container, NativeAdLayout fbnative_container, String type) {
        if (type.equals("facebook")) {
            fbnative_container.setVisibility(View.VISIBLE);
            mnativead_container.setVisibility(View.GONE);
            ShowFbNativeAd(context, fbnative_container);
        } else if (type.equals("admob")) {

            if (shownativeads().size() > 0) {
                if (mAdmobnativeAd != null) {
                    mAdmobnativeAd.destroy();
                }
                fbnative_container.setVisibility(View.GONE);
                mnativead_container.setVisibility(View.VISIBLE);
                mAdmobnativeAd = shownativeads().get(0);
                NativeAdView adView = (NativeAdView) LayoutInflater.from(context).inflate(R.layout.admb_native_regularview, null);
                populatePassNativeAdView(mAdmobnativeAd, adView);
                mnativead_container.removeAllViews();
                mnativead_container.addView(adView);
            }
        }
    }

    public void ShowFbNativeAd(Context context, NativeAdLayout mnativead_container) {
        if (showfbnativead() != null) {
            nativeAd = showfbnativead();

            nativeAd.unregisterView();
            LinearLayout adView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.ad_native_fb, mnativead_container, false);
            mnativead_container.addView(adView);

            // Add the AdOptionsView
            LinearLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
            AdOptionsView adOptionsView = new AdOptionsView(context, nativeAd, mnativead_container);
            adChoicesContainer.removeAllViews();
            adChoicesContainer.addView(adOptionsView, 0);

            MediaView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
            TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
            MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
            TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
            TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
            TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
            Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

            nativeAdTitle.setText(nativeAd.getAdvertiserName());
            nativeAdBody.setText(nativeAd.getAdBodyText());
            nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
            nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
            nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
            sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

            List<View> clickableViews = new ArrayList<>();
            clickableViews.add(nativeAdTitle);
            clickableViews.add(nativeAdCallToAction);

            nativeAd.registerViewForInteraction(adView, nativeAdMedia, nativeAdIcon, clickableViews);
        }
    }


    public void getAdsIdData(Context context, getadscalling ads_face) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443); //true, 80, 443
        client.setTimeout(60000);
        params.put("pkg_name", context.getPackageName());
        client.post(ADS_ID_URL, params, new AsynchronouseData(context, ads_face));
    }

    class AsynchronouseData extends AsyncHttpResponseHandler {

        Context ctxt;
        getadscalling ads_inreface;

        AsynchronouseData(Context context, getadscalling inreface) {
            ctxt = context;
            this.ads_inreface = inreface;
        }

        @Override
        public void onStart() {
            super.onStart();
        }

        @Override
        public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
            try {
                JSONArray jsonArray = new JSONArray(new String(responseBody));
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                    admobbanner_id = jsonObject.getString("banner");
                    admobinstrial_id = jsonObject.getString("popUp");
                    admobnative_id = jsonObject.getString("native");
                    admob_openAds_ID = jsonObject.getString("openad");
                    Facebookbanner_id = jsonObject.getString("fbBanner");
                    Facebookinstrial_id = jsonObject.getString("fbPopUp");
                    Facebooknative_id = jsonObject.getString("fbnative");
                    ads_display = jsonObject.getString("displayAd");
                    privacy_url = jsonObject.getString("privacypolicy_url");

                    privacy_url = jsonObject.getString("privacypolicy_url");

                    String video_link = jsonObject.getString("video");
                    if (!video_link.equals("")) {
                        API_URL = jsonObject.getString("video");
                    }

                    if (jsonObject.has("show_intertitial_after")) {
                        String temp_inter = jsonObject.getString("show_intertitial_after");
                        if (temp_inter != null && !temp_inter.equals(""))
                            CALL_FULLSCREEN_after = Integer.parseInt(temp_inter);
                    }

                    Log.e("callingadsid", "inter  ::  " + admobinstrial_id + "    openad  :: " + admob_openAds_ID);

                    try {
                        String app_ver_link = jsonObject.getString("show_banner_enable");
                        if (!app_ver_link.equals("")) {
                            get_App_version = jsonObject.getString("show_banner_enable");
                            show_ads_current = get_version_code(ctxt) <= Integer.parseInt(get_App_version);
                        }
                    } catch (Exception e) {

                    }

                    PrefStorageManager pref = new PrefStorageManager(ctxt);
                    if (pref != null) {
                        pref.setStringprefrence(Pref_admobbanner_id, admobbanner_id);
                        pref.setStringprefrence(Pref_admobinstrial_id, admobinstrial_id);
                        pref.setStringprefrence(Pref_admobnative_id, admobnative_id);
                        pref.setStringprefrence(Pref_admob_openAds_ID, admob_openAds_ID);
                        pref.setStringprefrence(Pref_Facebookbanner_id, Facebookbanner_id);
                        pref.setStringprefrence(Pref_Facebookinstrial_id, Facebookinstrial_id);
                        pref.setStringprefrence(Pref_Facebooknative_id, Facebooknative_id);
                        pref.setStringprefrence(Pref_ads_display, ads_display);
                        pref.setStringprefrence(Pref_privacy_url, privacy_url);
                    }
                }
                callingsepratefunction(ads_inreface, true);
            } catch (Exception e) {
                on_responsecallerror(ctxt, ads_inreface);
            }
        }

        @Override
        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
            on_responsecallerror(ctxt, ads_inreface);
        }

    }

    public int get_version_code(Context contx) {
        PackageManager manager = contx.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(contx.getPackageName(), PackageManager.GET_ACTIVITIES);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public interface getadscalling {
        void after_adsapi_call(boolean adsget);
    }


    public void on_responsecallerror(Context context, getadscalling ads_inreface) {
        PrefStorageManager pref = new PrefStorageManager(context);
        Facebookbanner_id = pref.getStringvalue(Pref_Facebookbanner_id);
        Facebookinstrial_id = pref.getStringvalue(Pref_Facebookinstrial_id);
        Facebooknative_id = pref.getStringvalue(Pref_Facebooknative_id);
        admobbanner_id = pref.getStringvalue(Pref_admobbanner_id);
        admobinstrial_id = pref.getStringvalue(Pref_admobinstrial_id);
        admobnative_id = pref.getStringvalue(Pref_admobnative_id);
        admob_openAds_ID = pref.getStringvalue(Pref_admob_openAds_ID);
        ads_display = pref.getStringvalue(Pref_ads_display);
        privacy_url = pref.getStringvalue(Pref_privacy_url);
        callingsepratefunction(ads_inreface, false);
    }


    public void callingsepratefunction(final getadscalling ads_inreface, final boolean passsuccess) {

        list_admobbanner = new ArrayList<>();
        list_admobinstrial = new ArrayList<>();
        list_admobnative = new ArrayList<>();

        if (admobbanner_id != null && !admobbanner_id.equals("")) {
            String[] mobbanner_list = admobbanner_id.split("#");
            for (int i = 0; i < mobbanner_list.length; i++) {
                list_admobbanner.add(mobbanner_list[i]);
            }
        }

        if (admobinstrial_id != null && !admobinstrial_id.equals("")) {
            String[] mobfullscreen_list = admobinstrial_id.split("#");
            for (int i = 0; i < mobfullscreen_list.length; i++) {
                list_admobinstrial.add(mobfullscreen_list[i]);
            }
        }

        if (admobnative_id != null && !admobnative_id.equals("")) {
            String[] mobnative_list = admobnative_id.split("#");
            for (int i = 0; i < mobnative_list.length; i++) {
                list_admobnative.add(mobnative_list[i]);
            }
        }
        new Handler().postDelayed(new Runnable() {
            public void run() {
                ads_inreface.after_adsapi_call(passsuccess);
            }
        }, 1000);
    }


    AppOpenAd appOpenAd;

    public void openadsadmobcall(final Context context, final getopenadsresponse openads_call) {

        if (!show_ads_current) {
            openads_call.after_openadscall_call(false);
            return;
        }

        AppOpenAd.AppOpenAdLoadCallback loadCallback;
        loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull AppOpenAd appAd) {
                super.onAdLoaded(appOpenAd);
                appOpenAd = appAd;
                showAdIfAvailable(context, openads_call);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                openads_call.after_openadscall_call(false);
            }
        };
        AdRequest request = getAdRequest();
        AppOpenAd.load(context, admob_openAds_ID, request, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
    }


    public void showAdIfAvailable(Context context, final getopenadsresponse openads_call) {

        FullScreenContentCallback fullScreenContentCallback = new FullScreenContentCallback() {
            @Override
            public void onAdDismissedFullScreenContent() {
                appOpenAd = null;
                openads_call.after_openadscall_call(true);
            }

            @Override
            public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {
                openads_call.after_openadscall_call(false);
            }

            @Override
            public void onAdShowedFullScreenContent() {
            }
        };
        appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
        appOpenAd.show((Activity) context);
    }


    private AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }

    public interface getopenadsresponse {
        void after_openadscall_call(boolean adsget);
    }

    public static void changeStatusBarColor(Context mContenxt, Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(mContenxt.getResources().getColor(R.color.colorPrimary));
        }
    }
}
