package com.qhubb.qhubb;

import android.app.Fragment;
import android.content.Intent;


/**
 * Created by jonathanalinovi on 5/17/15.
 */

public class NativeFragmentWrapper extends android.support.v4.app.Fragment {
    private final Fragment nativeFragment;

    public NativeFragmentWrapper(Fragment nativeFragment) {
        this.nativeFragment = nativeFragment;
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        nativeFragment.startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        nativeFragment.onActivityResult(requestCode, resultCode, data);
    }
}