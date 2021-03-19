package com.ygha.spinner;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class SpinnerAdapter extends BaseAdapter {

    String fruit[];
    Context context;
    private int previousPosition = -1;
    private int nowPosition = 0;//-1;

    public SpinnerAdapter(String[] fruit, Context context) {
        this.fruit = fruit;
        this.context = context;
    }

    public void setPositionSelected(int position) {
        this.notifyDataSetChanged();
        if (previousPosition == -1)
            previousPosition = position;
        else
            previousPosition = nowPosition;

        if (nowPosition == -1)
            nowPosition = previousPosition;
        else
            nowPosition = position;
    }

    @Override
    public int getCount() {
        return fruit.length;
    }

    @Override
    public Object getItem(int position) {
        return fruit[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;

        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_spinner,null);
            holder = new ViewHolder();
            holder.mode = (TextView)convertView.findViewById(R.id.textView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        holder.mode.setText(fruit[position]);

        convertView.setMinimumHeight(80);;
        convertView.setBackgroundColor(Color.rgb(200,100,100));

        if(nowPosition == position){
            convertView.setBackgroundColor(Color.argb(255,0,250,154));
        }

        return convertView;
    }

    static class ViewHolder{
        TextView mode;
    }


}
