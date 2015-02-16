package com.codepath.imagesearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.imagesearch.R;
import com.codepath.imagesearch.utils.DeviceDimensionsHelper;
import com.squareup.picasso.Picasso;

public class ImageDisplayActivity extends ActionBarActivity {
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView tvText = (TextView) findViewById(R.id.tvText);
        url = getIntent().getStringExtra("url");
        ImageView ivImageResult = (ImageView) findViewById(R.id.ivImageResult);
        int deviceWidth = DeviceDimensionsHelper.getDisplayWidth(this);
        Picasso.with(this).load(url).resize(deviceWidth, 0).into(ivImageResult);
        tvText.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType(getString(R.string.email_type));
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.sample_email)});
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
            intent.putExtra(Intent.EXTRA_TEXT, url);

            startActivity(Intent.createChooser(intent, ""));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
