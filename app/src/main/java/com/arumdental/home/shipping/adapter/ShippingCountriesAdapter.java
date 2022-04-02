package com.arumdental.home.shipping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.arumdental.R;
import com.arumdental.home.shipping.model.ShippingBodyItem;

import java.util.ArrayList;

public class ShippingCountriesAdapter  extends BaseAdapter {

    Context context;
    ArrayList<ShippingBodyItem> shippingBodyItemArrayList;
    LayoutInflater inflater;


    public  ShippingCountriesAdapter(Context context, ArrayList<ShippingBodyItem> shippingBodyItems)
    {
        this.context=context;
        this.shippingBodyItemArrayList=shippingBodyItems;
        inflater=(LayoutInflater.from(context));
    }
    @Override
    public int getCount() {
       return shippingBodyItemArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return shippingBodyItemArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.spinner_item, null);
        TextView names = convertView.findViewById(R.id.text1);
        names.setText(shippingBodyItemArrayList.get(position).getCountry());
        return convertView;
    }
}
