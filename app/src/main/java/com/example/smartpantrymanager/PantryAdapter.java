package com.example.smartpantrymanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PantryAdapter extends ArrayAdapter<String> {

    public PantryAdapter(
            Context context,
            ArrayList<String> pantryItems
    ) {
        super(context, 0, pantryItems);
    }

    @Override
    public View getView(
            int position,
            View convertView,
            ViewGroup parent
    ) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    android.R.layout.simple_list_item_1,
                    parent,
                    false
            );
        }

        TextView ingredientText = convertView.findViewById(
                android.R.id.text1
        );

        String ingredient = getItem(position);

        ingredientText.setText(ingredient);

        return convertView;
    }
}