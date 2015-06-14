package com.krimea.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.krimea.R;
import com.krimea.view.MainActivity;


import java.util.Set;

import info.hoang8f.widget.FButton;

public class CustomAdapter extends BaseAdapter {
    String[] resultArray;
    Context context;
    int [] col;

    private static LayoutInflater inflater = null;

    public CustomAdapter(MainActivity mainActivity, Set<String> contacts, int[] colors) {
        resultArray = contacts.toArray(new String[contacts.size()]);

        contacts.toArray(resultArray);
        context = mainActivity;
        col = colors;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return resultArray.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public class Holder {
        FButton button;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView;
        Holder holder=new Holder();
        rowView = inflater.inflate(R.layout.contacts_list, null);
        holder.button = (FButton) rowView.findViewById(R.id.contact_button);
        holder.button.setText(resultArray[position]);
        holder.button.setButtonColor(col[position]);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Panic Mode Enabled", Toast.LENGTH_SHORT).show();
                MainActivity.isButtonPressed = true;
            }
        });
        return rowView;
    }
}
