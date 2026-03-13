package com.harshitcreations.tourguard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AlertAdapter extends RecyclerView.Adapter<AlertAdapter.AlertViewHolder> {

    List<AlertItem> alerts;
    OnAlertClick listener;

    public interface OnAlertClick{
        void onClick(AlertItem alert);
    }

    public AlertAdapter(List<AlertItem> alerts,OnAlertClick listener){
        this.alerts = alerts;
        this.listener = listener;
    }

    @Override
    public AlertViewHolder onCreateViewHolder(ViewGroup parent,int viewType){

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alert_item,parent,false);

        return new AlertViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlertViewHolder holder,int position){

        AlertItem alert = alerts.get(position);

        holder.title.setText(alert.title);
        holder.severity.setText(alert.severity);
        holder.time.setText(alert.created_at);

        holder.itemView.setOnClickListener(v -> listener.onClick(alert));
    }

    @Override
    public int getItemCount(){
        return alerts.size();
    }

    static class AlertViewHolder extends RecyclerView.ViewHolder{

        TextView title,severity,time;

        public AlertViewHolder(View itemView){

            super(itemView);

            title = itemView.findViewById(R.id.alertTitle);
            severity = itemView.findViewById(R.id.alertSeverity);
            time = itemView.findViewById(R.id.alertTime);
        }
    }
}