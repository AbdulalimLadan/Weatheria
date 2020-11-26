package com.z4rtx.weatheria.widgets;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.z4rtx.weatheria.R;

import java.util.Objects;

public class AwesomeBottomSheetDialog {
    private BottomSheetDialog bottomSheetDialog;
    private EditText E_search_field;
    private ImageButton IB_search_btn;
    private ImageView IV_dialog_close;

    private OnSearchBtnClicked listener;

    public AwesomeBottomSheetDialog(Context context){
        bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.dialog_city);
        E_search_field = bottomSheetDialog.findViewById(R.id.awesome_dialog_city_search_field);
        IB_search_btn = bottomSheetDialog.findViewById(R.id.awesome_dialog_city_search_button);
        IV_dialog_close = bottomSheetDialog.findViewById(R.id.awesome_dialog_close);

    }

    public void setOnSearchBtnClickListener(OnSearchBtnClicked listener){
        this.listener = listener;
    }

    public interface OnSearchBtnClicked{
        void searchCity();
    }

    public void initialize(){
        IB_search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.searchCity();
            }
        });
        IV_dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
        Objects.requireNonNull(bottomSheetDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public String getSearchText(){
        return this.E_search_field.getText().toString();
    }

    public void dismiss(){
        bottomSheetDialog.dismiss();
    }
}
