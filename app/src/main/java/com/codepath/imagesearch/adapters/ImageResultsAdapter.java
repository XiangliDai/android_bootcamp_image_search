package com.codepath.imagesearch.adapters;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.codepath.imagesearch.R;
import com.codepath.imagesearch.models.ImageResult;
import com.etsy.android.grid.util.DynamicHeightImageView;
import com.etsy.android.grid.util.DynamicHeightTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

public class ImageResultsAdapter extends ArrayAdapter<ImageResult> {

    private static class ViewHolder {
        public DynamicHeightImageView ivImage;
        public DynamicHeightTextView tvTitle;
        //public TextView tvAuthor;
    }
    // View lookup cache
    private final Random mRandom;
    private final ArrayList<Integer> mBackgroundColors;

    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();
    public ImageResultsAdapter(Context context, ArrayList<ImageResult> images) {
        super(context, R.layout.item_image_result, images);

        mRandom = new Random();
        mBackgroundColors = new ArrayList<Integer>();
        mBackgroundColors.add(R.color.orange);
        mBackgroundColors.add(R.color.green);
        mBackgroundColors.add(R.color.blue);
        mBackgroundColors.add(R.color.yellow);
        mBackgroundColors.add(R.color.grey);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ImageResult imageResult = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_image_result, parent, false);
            viewHolder.ivImage = (DynamicHeightImageView)convertView.findViewById(R.id.ivImage);
            viewHolder.tvTitle = (DynamicHeightTextView)convertView.findViewById(R.id.tvTitle);
            //viewHolder.tvAuthor = (TextView)convertView.findViewById(R.id.tvAuthor);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvTitle.setText(Html.fromHtml(imageResult.getTitle()));
        viewHolder.tvTitle.setVisibility(View.GONE);

        double positionHeight = getPositionRatio(position);
        int backgroundIndex = position >= mBackgroundColors.size() ?
                position % mBackgroundColors.size() : position;

        convertView.setBackgroundResource(mBackgroundColors.get(backgroundIndex));

        viewHolder.ivImage.setHeightRatio(positionHeight);
        //viewHolder.tvAuthor.setText(book.getAuthor());
        viewHolder.ivImage.setImageResource(0);

        Picasso.with(getContext())
                .load(Uri.parse(imageResult.getThumbUrl()))
                .fit().centerCrop()
                .into(viewHolder.ivImage);
        return convertView;
    }

    private double getPositionRatio(final int position) {
        double ratio = sPositionHeightRatios.get(position, 0.0);
        // if not yet done generate and stash the columns height
        // in our real world scenario this will be determined by
        // some match based on the known height and width of the image
        // and maybe a helpful way to get the column height!
        if (ratio == 0) {
            ratio = getRandomHeightRatio();
            sPositionHeightRatios.append(position, ratio);

        }
        return ratio;
    }

    private double getRandomHeightRatio() {
        return (mRandom.nextDouble() / 2.0) + 1.0; // height will be 1.0 - 1.5 the width
    }
}
