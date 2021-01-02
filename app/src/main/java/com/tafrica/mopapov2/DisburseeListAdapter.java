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

import java.util.ArrayList;

public class DisburseeListAdapter extends ArrayAdapter<Disbursee> {
    private Context mContext;
    private int mResource;
    private int lastPosition =-1;

    static class ViewHolder {
        TextView disdate;
        TextView clntname;
        TextView amnt;
        TextView amntdu;
    }

    public DisburseeListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Disbursee> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String disbursementdate = getItem(position).getDisbursementdate();
        String clientname = getItem(position).getClientname();
        String amount = getItem(position).getAmount();
        String amountdue = getItem(position).getAmountdue();

        //Create Disbursee object with four info vars
        Disbursee disbursee = new Disbursee(disbursementdate,clientname,amount,amountdue);

        //create the view result for showing the animation
        final View result;
        ViewHolder holder;

        if(convertView==null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView= inflater.inflate(mResource,parent,false);
            holder = new ViewHolder();
            holder.disdate = (TextView) convertView.findViewById(R.id.disbursementdate_lstvw_item);
            holder.clntname = (TextView) convertView.findViewById(R.id.client_name_lstvw_item);
            holder.amnt = (TextView) convertView.findViewById(R.id.amount_lstvw_item);
            holder.amntdu = (TextView) convertView.findViewById(R.id.amount_with_interest_lstvw_item);

            result= convertView;
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

        holder.disdate.setText(disbursee.getDisbursementdate());
        holder.clntname.setText(disbursee.getClientname());
        holder.amnt.setText(disbursee.getAmount());
        holder.amntdu.setText(disbursee.getAmountdue());
return convertView;
    }
}


//https://www.youtube.com/watch?v=SApBLHIpH8A&t=6s ANIMATION SCROLLING AND BEST PRACTICE FOR LIST VIEWS
//https://www.youtube.com/watch?v=E6vE8fqQPTE HOW TO SET LIST ITEMS TO CUSTOM LISTVIEW USING LIST ADAPTER