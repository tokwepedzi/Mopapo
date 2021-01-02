package com.tafrica.mopapov2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tafrica.mopapov2.AccountsMenuItems.CashupModel.CahsupModelclass;

import java.util.ArrayList;

public class CashupModelClassListAdpater extends ArrayAdapter<CahsupModelclass> {
    private Context mContext;
    int mResource;
    private int lastPosition =-1;

    static class ViewHolder{
        TextView cashpdt;
        TextView totamnt;
        TextView tosbmt;
        TextView shot;

    }


    public CashupModelClassListAdpater(@NonNull Context context, int resource, @NonNull ArrayList<CahsupModelclass> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String totalcollection = getItem(position).getTotalcollection();
        String cashinhand = getItem(position).getCashinhand();
        String cashupdate = getItem(position).getCashupdate();
        String shortfall = getItem(position).getShortfall();

        //Create CahsupModelclass object with three info vars
        CahsupModelclass cahsupModelclass = new CahsupModelclass(cashupdate,totalcollection,cashinhand,shortfall);

        //create the view result for showing the animation;
        final View result;
        ViewHolder holder;
        if(convertView==null){

            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource,parent,false);


            holder = new ViewHolder();

            holder.totamnt = (TextView) convertView.findViewById(R.id.total_collection);
            holder.tosbmt = (TextView) convertView.findViewById(R.id.submmitted_amount);
            holder.cashpdt = (TextView) convertView.findViewById(R.id.cashupdate);
            holder.shot = (TextView) convertView.findViewById(R.id.shortfall) ;

            result = convertView;
            convertView.setTag(holder);

        }
        else {
            holder = (ViewHolder) convertView.getTag();
            result = convertView;

        }

        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position>lastPosition) ? R.anim.loading_down_anim:R.anim.loading_up_anim);
        result.startAnimation(animation);
        lastPosition = position;


        holder.totamnt.setText(cahsupModelclass.getTotalcollection());
        holder.tosbmt.setText(cahsupModelclass.getCashinhand());
        holder.cashpdt.setText(cahsupModelclass.getCashupdate());
        holder.shot.setText(cahsupModelclass.getShortfall());
        return convertView;


    }
}


//https://www.youtube.com/watch?v=SApBLHIpH8A&t=6s ANIMATION SCROLLING AND BEST PRACTICE FOR LIST VIEWS
//https://www.youtube.com/watch?v=E6vE8fqQPTE HOW TO SET LIST ITEMS TO CUSTOM LISTVIEW USING LIST ADAPTER
