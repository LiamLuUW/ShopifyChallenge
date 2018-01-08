package com.liam.shopifychallenge;

import android.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RetrofitManager.init();
        ThumbnailManager.init();

        ProductListFragment productListFragment = new ProductListFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, productListFragment);
        ft.commit();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        ThumbnailManager.clearCache();
    }


}
