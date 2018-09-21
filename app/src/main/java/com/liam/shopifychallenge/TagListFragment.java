package com.liam.shopifychallenge;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.liam.shopifychallenge.util.TagListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * TagList Fragment
 */

public class TagListFragment extends Fragment {
    private final static String TAG = TagListFragment.class.getSimpleName();
    private final static int REQUEST_PAGE_NUMBER = 1;
    private List<String> tags;
    private HashMap<String, ArrayList<Product>> hashedProducts;
    private SwipeRefreshLayout swipeLayout;
    private TagListAdapter tagListAdapter;
    private TagList tagListView;
    private boolean needUpdate;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        this.needUpdate = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup viewGroup,
                             @Nullable Bundle savedInstance) {
        final View view = inflater.inflate(R.layout.tag_list_view, viewGroup, false);
        swipeLayout = view.findViewById(R.id.tag_list_swipe_container);

        swipeLayout.setOnRefreshListener(() -> refreshData());

        OnTagClickListener listener = pos -> navigateToProductList(tags.get(pos));
        tagListAdapter = new TagListAdapter(listener);
        tagListView = view.findViewById(R.id.tag_list_view);
        tagListView.setAdapter(tagListAdapter);

        //enable back button
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (needUpdate || tags == null) {
            refreshData();
        } else {
            tagListAdapter.setTagList(tags);
            tagListAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Load product list data from server
     */
    private void refreshData() {
        Log.v(TAG, "Load products data from server");
        ShopifyApi mApi = RetrofitManager.getShopifyApi();
        Call<ProductListResponse> call = mApi.getProductList(REQUEST_PAGE_NUMBER);

        call.enqueue(new Callback<ProductListResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProductListResponse> call,
                                   @NonNull Response<ProductListResponse> response) {
                ProductListResponse mResponse = response.body();
                if (mResponse == null) {
                    Log.e(TAG, "Error: empty response body received, just ignore");
                    return;
                }
                Log.v(TAG, "Products response received from server with list size of "
                        + mResponse.products.size());
                mResponse.setTagList();
                tags = new ArrayList<>(mResponse.getTagList());
                hashedProducts = mResponse.getHashedProductList();

                final LayoutAnimationController controller =
                        AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_animation);
                tagListView.setLayoutAnimation(controller);
                tagListAdapter.setTagList(tags);
                tagListAdapter.notifyDataSetChanged();
                tagListView.scheduleLayoutAnimation();
                swipeLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<ProductListResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "Error: Products response onFailure " + t.getMessage());
            }
        });
    }

    /**
     * navigate into product list page associated with this tag
     * @param tagKey tagName
     */
    private void navigateToProductList(String tagKey) {
        UpdateTaskResultListener taskResultListener = result -> needUpdate = result;
        ProductListFragment productListFragment = ProductListFragment.newInstance(hashedProducts.get(tagKey));
        ProductListFragment.setTaskCompleteListener(taskResultListener);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.fragment_slide_in, R.animator.fragment_slide_out);
        ft.replace(R.id.fragment_container, productListFragment).addToBackStack(null);
        ft.commit();
    }
}
