package com.tafrica.mopapov2.ReportsMenuItems;

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

import com.tafrica.mopapov2.PaymentModel.Payer;
import com.tafrica.mopapov2.R;

import java.util.ArrayList;

public class CollectionsListAdapter extends ArrayAdapter<Payer> {
    private Context mContext;
    private  int mResource;
    private int lastpstion=-1;

    static class ViewHolder {
        TextView paidate;
        TextView name;
        TextView paid;
        TextView balance;
        //TextView amntdu;
    }

    public CollectionsListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Payer> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String clientname = getItem(position).getClientname();
        String paid = getItem(position).getPaid();
        String balance = getItem(position).getBalance();
        String paiddate = getItem(position).getPaymentdate();

        //Create Disbursee object with four info vars
        Payer payer = new Payer(clientname,paid,balance,paiddate);

        //create the view result for showing the animation
        final View result;
        ViewHolder holder;

        if(convertView==null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView= inflater.inflate(mResource,parent,false);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.client_name_lstvw_item);
            holder.paid = (TextView) convertView.findViewById(R.id.paid_amount_lstvw_item);
            holder.balance = (TextView) convertView.findViewById(R.id.balance_lstvw_item);
            holder.paidate = (TextView) convertView.findViewById(R.id.paymentdate);

            result= convertView;
            convertView.setTag(holder);

        }
        else {
            holder = (CollectionsListAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }


        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position>lastpstion)? R.anim.loading_down_anim:R.anim.loading_up_anim);
        result.startAnimation(animation);
        lastpstion=position;

        holder.name.setText(payer.getClientname());
        holder.paid.setText(payer.getPaid());
        holder.balance.setText(payer.getBalance());
       holder.paidate.setText(payer.getPaymentdate());
        return convertView;
    }

}



//https://www.youtube.com/watch?v=SApBLHIpH8A&t=6s ANIMATION SCROLLING AND BEST PRACTICE FOR LIST VIEWS
//https://www.youtube.com/watch?v=E6vE8fqQPTE HOW TO SET LIST ITEMS TO CUSTOM LISTVIEW USING LIST ADAPTER