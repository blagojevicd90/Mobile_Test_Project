package com.example.mobiletestproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobiletestproject.Model.CharityModel;
import com.example.mobiletestproject.R;

import java.util.ArrayList;
import java.util.List;

public class CharityAdapter extends RecyclerView.Adapter<CharityAdapter.MyViewHolder> {

    private Context context;
    private List<CharityModel> mList;


    public CharityAdapter(Context context, List<CharityModel> mList) {
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public CharityAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.charity_item, parent, false);

        return new CharityAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull CharityAdapter.MyViewHolder holder, int position) {

        holder.txt_charity_name.setText(mList.get(position).getName());
        Glide.with(context).load(mList.get(position).getLogo_url()).into(holder.img_logo);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_charity_name;
        private ImageView img_logo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_charity_name = (TextView) itemView.findViewById(R.id.txt_charity_name);
            img_logo = (ImageView) itemView.findViewById(R.id.img_logo);
        }
    }
}
