package com.commax.ocf.server.app.listview;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.commax.lib.Common.Log;
import com.commax.ocf.server.app.R;

import java.util.ArrayList;

public class MainListviewAdapter extends BaseAdapter {

    private ArrayList<MainListviewItem> data;
    private int layout;
    private LayoutInflater inflater;

    private int previousPosition_ = -1;
    private int nowPosition_ = 0;//-1;

    private boolean[] isChekedConfirm;
    private ArrayList<MainListviewItem> sArrayList = new ArrayList<>();

    public MainListviewAdapter(Context context, int layout, ArrayList<MainListviewItem> data) {
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = data;
        //sArrayList = data;
        this.layout = layout;
        isChekedConfirm = new boolean[data.size()];
    }

    public void setAllCheck(boolean ischecked){
        int tempSize = isChekedConfirm.length;
        for(int i=0;i<tempSize;i++)
            isChekedConfirm[i] = ischecked;
    }

    public void setChecked(int position){
        isChekedConfirm[position] = !isChekedConfirm[position];
    }

    public ArrayList<Integer> getChecked(){
        int tempSize = isChekedConfirm.length;
        ArrayList<Integer> mArrayList = new ArrayList<>();
        for(int i=0;i<tempSize;i++){
            if(isChekedConfirm[i]){
                mArrayList.add(i);
            }
        }
        return mArrayList;

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
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        ViewHolder holder;

        if(convertView==null){
            convertView=inflater.inflate(layout,parent,false);
            holder = new ViewHolder();

            holder.title = (TextView)convertView.findViewById(R.id.title);
            holder.icon = (ImageView)convertView.findViewById(R.id.image);
            holder.exp = (TextView)convertView.findViewById(R.id.explanation);
            holder.chk = (CheckBox)convertView.findViewById(R.id.checkbox);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }


        holder.title.setText(data.get(position).getTitle());
        holder.icon.setImageResource(data.get(position).getImage());
        holder.exp.setText(data.get(position).getExp());

        //holder.chk.setChecked(false);
        //holder.chk.setChecked(isChekedConfirm[position]);
        //holder.chk.setChecked(((ListView)parent).isItemChecked(position));

        holder.chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Log.e("","");
                    isChekedConfirm[position] = true;
                }else
                    isChekedConfirm[position] = false;
            }
        });


        convertView.setMinimumHeight(100);
        convertView.setBackgroundColor(Color.WHITE);

        holder.chk.setChecked(isChekedConfirm[position]);

        if(nowPosition_==position){
            convertView.setBackgroundColor(Color.argb(255,0,250,154));
        }

        return convertView;
    }

    static class ViewHolder{
        ImageView icon;
        TextView title;
        TextView exp;
        CheckBox chk;
    }

}
