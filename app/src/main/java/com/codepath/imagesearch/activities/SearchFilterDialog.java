package com.codepath.imagesearch.activities;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.codepath.imagesearch.R;
import com.codepath.imagesearch.models.SearchFilter;

public class SearchFilterDialog extends DialogFragment {
    private EditText etSiteFilter;
    Spinner spImageSize;
    Spinner spColorFilter;
    Spinner spImageType;
    Button btnSave;
    Button btnCancel;
    SearchFilter searchFilter;

    public interface SearchFilterDialogListener {
        void onFinishEditDialog(SearchFilter searchFilter);
        
    }
    
    public SearchFilterDialog() {
        // Empty constructor required for DialogFragment
    }

    public static SearchFilterDialog newInstance(SearchFilter searchFilter) {
        SearchFilterDialog frag = new SearchFilterDialog();
        Bundle args = new Bundle();
        args.putParcelable("filter", searchFilter);
        frag.setArguments(args);

        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_filter_dialog, container);
        getDialog().setTitle(R.string.advanced_filters);
        searchFilter = getArguments().getParcelable("filter");
        etSiteFilter = (EditText) view.findViewById(R.id.etSiteFilter);
        
        spImageSize = (Spinner) view.findViewById(R.id.spImageSize);
        spColorFilter = (Spinner) view.findViewById(R.id.spColorFilter);
        spImageType = (Spinner) view.findViewById(R.id.spImageType);
        if(searchFilter != null){
            if(!searchFilter.siteFilter.isEmpty()){
                    etSiteFilter.setText(searchFilter.siteFilter);}
            if(!searchFilter.imageSize.isEmpty()){
                selectSpinnerItemByValue(spImageSize, searchFilter.imageSize);
            }
            else
                selectSpinnerItemByValue(spImageSize, "any");
            if(!searchFilter.colorFilter.isEmpty()){
                selectSpinnerItemByValue(spColorFilter, searchFilter.colorFilter);
            }
            else
                selectSpinnerItemByValue(spColorFilter, "any");
            if(!searchFilter.imageType.isEmpty()){
                selectSpinnerItemByValue(spImageType, searchFilter.imageType);
            }
            else
                selectSpinnerItemByValue(spImageType, "any");


        }
        
        btnSave = (Button) view.findViewById(R.id.btnSave);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnSave.setOnClickListener(new View.OnClickListener() {
           
            @Override
            public void onClick(View view) {
                SearchFilterDialogListener listener = (SearchFilterDialogListener) getActivity(); 

                 searchFilter = new SearchFilter(
                         spImageSize.getSelectedItem().toString().equals("any") ? "" : spImageSize.getSelectedItem().toString(),
                         spImageType.getSelectedItem().toString().equals("any") ? "" : spImageType.getSelectedItem().toString(),
                         spColorFilter.getSelectedItem().toString().equals("any") ? "" : spColorFilter.getSelectedItem().toString(),
                        etSiteFilter.getText().toString());
                listener.onFinishEditDialog(searchFilter);
                dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchFilterDialogListener listener = (SearchFilterDialogListener) getActivity();
                listener.onFinishEditDialog(searchFilter);
                dismiss();
            }
        });
        // Show soft keyboard automatically
        etSiteFilter.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return view;
    }

    private void selectSpinnerItemByValue(Spinner spnr, String value)
    {
        ArrayAdapter adapter = (ArrayAdapter) spnr.getAdapter();
        for (int position = 0; position < adapter.getCount(); position++)
        {
            if(adapter.getItem(position).toString().equals(value))
            {
                spnr.setSelection(position);
                return;
            }
        }
    }
}
