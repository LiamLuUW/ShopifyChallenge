package com.liam.shopifychallenge;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hideBackButton();

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

    @Override
    public void onBackPressed(){
        if(getFragmentManager().getBackStackEntryCount() > 0){
            getFragmentManager().popBackStack();
        }else{
            super.onBackPressed();
        }

    }

    public void hideBackButton() {

        if(getSupportActionBar() != null)
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

    }

}
