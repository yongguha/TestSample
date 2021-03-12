package com.commax.ocf.server.app.listview;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import com.commax.ocf.server.app.R;
public class SubMenuAdapter extends BaseAdapter {

    private ArrayList<SubMenuItem> ocf_menu_data;
    private int layout;
    private LayoutInflater inflater;

    private int previousPosition_ = -1;
    private int nowPosition_ = 0;//-1;


    public SubMenuAdapter(Context context, int layout, ArrayList<SubMenuItem> data) {
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.ocf_menu_data = data;
        this.layout = layout;
    }

    public void setPositionSelected(int position) {
        this.notifyDataSetChanged();
        if (previousPosition_ == -1)
            previousPosition_ = position;
        else
            previousPosition_ = nowPosition_;

        if (nowPosition_ == -1)
            nowPosition_ = previousPosition_;
        else
            nowPosition_ = position;
    }


    @Override
    public int getCount() {
        return ocf_menu_data.size();
    }

    @Override
    public Object getItem(int position) {
        return ocf_menu_data.get(position).getTitle();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if(convertView==null){
            convertView=inflater.inflate(layout,parent,false);
            holder = new ViewHolder();

            holder.content = (TextView)convertView.findViewById(R.id.ocf_title);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        holder.content.setText(ocf_menu_data.get(position).getTitle());


        convertView.setMinimumHeight(80);

        convertView.setBackgroundColor(Color.WHITE);
        if(nowPosition_==position){
            convertView.setBackgroundColor(Color.argb(255,255,105,180));
        }

        return convertView;
    }

    static class ViewHolder{
        TextView content;
    }
}
