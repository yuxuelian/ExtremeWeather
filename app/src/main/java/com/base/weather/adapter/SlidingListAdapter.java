package com.base.weather.adapter;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.weather.R;
import com.base.weather.adapter.draghelper.ItemTouchHelperAdapter;
import com.base.weather.adapter.draghelper.OnStartDragListener;
import com.base.weather.model.bean.SlidingWeatherBean;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 *
 * 邮箱：568966289@qq.com
 *
 * 创建时间：2017/4/15 22:00
 * 备注：
 */

public class SlidingListAdapter extends RecyclerView.Adapter<SlidingListAdapter.MyHolder>
        implements ItemTouchHelperAdapter {

    private List<SlidingWeatherBean> mItems;

    private OnStartDragListener mDragStartListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    private OnItemClickListener listener = null;


    public SlidingListAdapter(List<SlidingWeatherBean> mItems, OnStartDragListener dragStartListener) {
        this.mItems = mItems;
        mDragStartListener = dragStartListener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.sliding_menu_list_item_layout, parent, false);
        MyHolder myHolder = new MyHolder(inflate);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.slidingListItemCity.setText(mItems.get(position).getCityText());
        holder.slidingListItemWeatherTemp.setText(mItems.get(position).getWeatherTemp());
        holder.slidingListItemWeatherIc.setImageResource(mItems.get(position).getWeatherIcon());
        if (mItems.get(position).isShowLocationIcon()) {
            holder.slidingListItemLoc.setVisibility(View.VISIBLE);
        } else {
            holder.slidingListItemLoc.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    @Override
    public void onItemDismiss(int position) {
        mDragStartListener.onItemDismiss(position);
//        mItems.remove(position);

        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        //对mItems这个集合中的数据进行排序
        Collections.swap(mItems, fromPosition, toPosition);
        //刷新排序后的集合
        notifyItemMoved(fromPosition, toPosition);

        mDragStartListener.onItemMove(fromPosition, toPosition);
        return true;
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener {
        @BindView(R.id.sliding_list_item_loc)
        ImageView slidingListItemLoc;

        @BindView(R.id.sliding_list_item_city)
        TextView slidingListItemCity;

        @BindView(R.id.sliding_list_item_weather_ic)
        ImageView slidingListItemWeatherIc;

        @BindView(R.id.sliding_list_item_weather_temp)
        TextView slidingListItemWeatherTemp;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setClickable(true);
            itemView.setOnClickListener(this);

            //当触摸这个view的时候才能进行拖动排序
            slidingListItemWeatherIc.setOnTouchListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClick(v, this.getAdapterPosition());
            }
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                mDragStartListener.onStartDrag(this);
            }
            return false;
        }
    }
}
