package com.liam.shopifychallenge;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

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

    @Override
    public void onBackPressed(){
        if(getFragmentManager().getBackStackEntryCount() > 0){
            getFragmentManager().popBackStack();
        }else{
            super.onBackPressed();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
