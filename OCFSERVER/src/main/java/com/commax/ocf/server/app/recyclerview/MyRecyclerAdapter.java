package com.commax.ocf.server.app.recyclerview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.commax.ocf.server.app.R;

import java.util.List;

/*\
view, viewHolder를 생성하고, item과 viewHolder를 bind 하며
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {

    Context context;
    private List<Recycler_item> items;
    private int itemLayout;

    private ItemClick itemClick;

    int selectedPosition = -1;

    public interface ItemClick{
        public void onClick(View view, int position);
    }

    public void setIemClick(ItemClick iemClick){
        this.itemClick = iemClick;
    }

    public MyRecyclerAdapter(Context context, List<Recycler_item> items, int itemLayout) {
        this.context = context;
        this.items = items;
        this.itemLayout = itemLayout;
    }


    /*
    onCreateView에서는 ViewHolder를 불러줍니다.
     */
    @NonNull
    @Override
    public MyRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview,null);
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_cardview,null);

        /**
         * onCreateViewHolder에 리스너를 !!!
         *
         */

        final ViewHolder holder = new ViewHolder(v);

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                v.setBackgroundResource(R.color.listseletec);
                Toast.makeText(context, holder.getAdapterPosition() , Toast.LENGTH_LONG).show();
            }
        });

        return holder;
    }

    /*
    onBindViewHolder에서는 액티비티에서 불러왔던 데이터를 아이텀에 넣어주면된다.
     */
    @Override
    public void onBindViewHolder(@NonNull MyRecyclerAdapter.ViewHolder holder, final int position) {
        final Recycler_item item = items.get(position);
        Drawable drawable = context.getResources().getDrawable(item.getImage());

        if(selectedPosition==position){
            holder.itemView.setBackgroundResource(R.color.listseletec);
        }else{
            holder.itemView.setBackgroundResource(R.color.cardview_light_background);
        }



        holder.image.setBackground(drawable);
        holder.title.setText(item.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;
                //v.setBackgroundResource(R.color.listseletec);
                notifyDataSetChanged();
            }
        });


        //holder.cardView.setOnClickListener(new View.OnClickListener(){
        //    @Override
        //    public void onClick(View v) {
         //       Toast.makeText(context, item.getTitle(), Toast.LENGTH_LONG).show();
        //    }
        //});
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }




    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView image;
        public TextView title;
        CardView cardView;


        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView)itemView.findViewById(R.id.image);
            title = (TextView)itemView.findViewById(R.id.title);
            //cardView = (CardView)itemView.findViewById(R.id.cardview);


        }
    }
}
