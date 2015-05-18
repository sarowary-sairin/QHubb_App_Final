package com.qhubb.qhubb;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by jonathanalinovi on 5/17/15.
 * for facebook hashkey
 */
public class myapplication extends Application {

    public class MyApplication extends Application {
        @Override
        public void onCreate() {
            super.onCreate();
            printHashKey();
        }

        public void printHashKey() {
            // Add code to print out the key hash
            try {
                PackageInfo info = getPackageManager().getPackageInfo(
                        "com.qhubb.qhubb",
                        PackageManager.GET_SIGNATURES);
                for (Signature signature : info.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    Log.d("qhubbhashkey", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                }
            } catch (PackageManager.NameNotFoundException e) {

            } catch (NoSuchAlgorithmException e) {

            }
        }
    }

}
