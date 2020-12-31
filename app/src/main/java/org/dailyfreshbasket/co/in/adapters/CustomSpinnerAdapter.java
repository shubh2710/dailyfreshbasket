package org.dailyfreshbasket.co.in.adapters;

/**
 * Created by shubham on 5/1/2018.
 */

import android.content.Context;
        import android.support.annotation.LayoutRes;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.TextView;

import org.dailyfreshbasket.co.in.R;
import org.dailyfreshbasket.co.in.models.addressModel;

import java.util.List;

public class CustomSpinnerAdapter extends ArrayAdapter<addressModel>{

    private final LayoutInflater mInflater;
    private final Context mContext;
    private final List<addressModel> items;
    private final int mResource;

    public CustomSpinnerAdapter(@NonNull Context context, @LayoutRes int resource,@NonNull List objects) {
        super(context, resource, 0, objects);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        items = objects;
    }
    @Override
    public View getDropDownView(int position, @Nullable View convertView,
                                @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent){
        final View view = mInflater.inflate(mResource, parent, false);

        TextView city = (TextView) view.findViewById(R.id.tv_sppiner_city);
        TextView land = (TextView) view.findViewById(R.id.tv_sppiner_land);
        TextView line = (TextView) view.findViewById(R.id.tv_sppiner_line);
        TextView mobile = (TextView) view.findViewById(R.id.tv_sppiner_mobilr);
        TextView state = (TextView) view.findViewById(R.id.tv_sppiner_state);
        addressModel offerData = items.get(position);
        state.setText("state: "+offerData.getState());
        city.setText("city: "+offerData.getCity());
        land.setText("land mark: "+offerData.getLand());
        mobile.setText("mobile: "+offerData.getMobile());
        line.setText("street: "+offerData.getStreet());
        return view;
    }
}