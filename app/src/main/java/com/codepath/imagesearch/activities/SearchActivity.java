package com.codepath.imagesearch.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.imagesearch.EndlessScrollListener;
import com.codepath.googleimagesearch.R;
import com.codepath.imagesearch.adapters.ImageResultsAdapter;
import com.codepath.imagesearch.models.ImageResult;
import com.codepath.imagesearch.models.SearchFilter;
import com.codepath.imagesearch.net.SearchClient;
import com.etsy.android.grid.StaggeredGridView;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;


public class SearchActivity extends ActionBarActivity implements SearchFilterDialog.SearchFilterDialogListener{
    public static final String TAG = SearchActivity.class.getSimpleName();
    private ArrayList<ImageResult> imageResults;
    private ImageResultsAdapter imageResultsAdapter;
    private StaggeredGridView gvResult;
    private SearchFilter searchFilter;
    private String queryStr;
    private int currentPage;
    private SearchClient client;
    private TextView tvText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupViews();
        imageResults = new ArrayList<>();
        imageResultsAdapter = new ImageResultsAdapter(this, imageResults);
        gvResult.setAdapter(imageResultsAdapter);
        searchFilter = new SearchFilter("", "", "", "");
        queryStr = "";
        currentPage = 0;
        client = new SearchClient(this);
    }

    private void setupViews() {
        tvText = (TextView) findViewById(R.id.tvText);
        gvResult = (StaggeredGridView) findViewById(R.id.gvImages);
        
        gvResult.setOnItemClickListener(onItemClickListener);
        gvResult.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if(page < 8){
                    currentPage = page - 1;
                    fetchImages();
                }
            }
        });

    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(SearchActivity.this, ImageDisplayActivity.class);
            ImageResult imageResult = imageResults.get(position);
            intent.putExtra("url", imageResult.getFullUrl());
            startActivity(intent);
        }
    };

    private void fetchImages() {
        if(!isNetworkAvailable()){
            Log.e(TAG, getString(R.string.no_internet_message));
            Toast.makeText(SearchActivity.this, getString(R.string.no_internet_message), Toast.LENGTH_SHORT).show();
        }
       String url = queryStr + getFilterString() + (currentPage == 0 ? "" : "&start=" + (currentPage * getResources().getInteger(R.integer.page_size) + 1));
       Log.d(TAG, url);   
       Log.d(TAG, "current page " + currentPage);
       client.getImageSearchResults(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    tvText.setVisibility(View.GONE);
                    JSONArray imageResultsJson = null;
                    if (response != null) {
                        imageResultsJson = response.getJSONObject("responseData").getJSONArray("results");
                        if(currentPage == 0) {
                            imageResults.clear();
                        }
                        imageResults.addAll(ImageResult.fromJsonArray(imageResultsJson));
                        imageResultsAdapter.notifyDataSetChanged();
                        Log.d(TAG, "ImageResult " + imageResults.size());

                    }
                } catch (JSONException e) {
                    // Invalid JSON format, show appropriate error.
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                    Toast.makeText(SearchActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                try {
                    queryStr = URLEncoder.encode(s, "utf-8");
                    currentPage = 0;
                    tvText.setText(getResources().getString(R.string.loading));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                fetchImages();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String query) {
                return false;
            }

        });
        return super.onCreateOptionsMenu(menu);
    }

    private String getFilterString() {
        if(searchFilter != null) {
            StringBuilder filterStr = new StringBuilder();
            if (!searchFilter.colorFilter.isEmpty()) {
                filterStr.append("&imgcolor=").append(searchFilter.colorFilter);
            }
            if (!searchFilter.imageType.isEmpty()) {
                filterStr.append(" &as_filetype=").append(searchFilter.imageType);
            }
            if (!searchFilter.imageSize.isEmpty()) {
                filterStr.append("&imgsz=").append(searchFilter.imageSize);
            }
            if (!searchFilter.siteFilter.isEmpty()) {
                filterStr.append("&as_sitesearch=").append(searchFilter.siteFilter);
            }
            return filterStr.toString();
        }
        return "";
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            showDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialog() {
        FragmentManager fm = getSupportFragmentManager();
        SearchFilterDialog searchFilterDialog = SearchFilterDialog.newInstance(searchFilter);
        searchFilterDialog.show(fm, "fragment filter");
    }

    public void onFinishEditDialog(SearchFilter searchFilter) {
        this.searchFilter = searchFilter;
    }

}
