package com.fyhack.pro.databindingpro.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * RecyclerViewAdapter
 * <p/>
 *
 * @author elc_simayi
 * @since 2015/12/23
 */

public abstract class RecyclerViewAdapter<E> extends RecyclerView.Adapter{
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_FOOTER = 1;
    private static final int FOOTER_NUM = 1;

    private Context context;
    private List<E> datas;

    public RecyclerViewAdapter(Context context,List<E> datas){
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == getItemCount()-1)
            return TYPE_FOOTER;
        return TYPE_NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(TYPE_NORMAL == viewType){
            return onCreateNormalViewHolderHandle(parent,viewType);
        }else{
            return onCreateFooterViewHolderHandle(parent,viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == RecyclerViewAdapter.TYPE_NORMAL){
            onBindNormalViewHolderHandle(holder,position);
        }else if(getItemViewType(position) == RecyclerViewAdapter.TYPE_FOOTER){
            onBindFooterViewHolderHandle(holder,position);
        }
    }

    @Override
    public int getItemCount() {
        return getDataCount() + FOOTER_NUM;
    }

    public int getDataCount() {
        return datas.size();
    }

    public void addItem(int position,E item){
        if (position > -1 && position <= getDataCount()){
            datas.add(position,item);
            notifyItemInserted(position);
        }
    }

    public E removeItem(int position){
        E item = null;
        if (position > -1 && position <= getDataCount()-1){
            item = datas.remove(position);
            notifyItemRemoved(position);
        }
        return item;
    }

    public void removeAllItem(int[] position){
        for(int i=0; i<position.length; i++){
            removeItem(position[i]-i);
        }
    }

    public void setItem(int position,E item){
        if (position > -1 && position <= getDataCount()-1){
            datas.set(position, item);
            notifyItemChanged(position);
        }
    }

    public void addRangeItems(int position, List<E> items){
        if (position > -1 && position <= getDataCount()) {
            datas.addAll(position, items);
            notifyItemRangeInserted(position, items.size());
        }
    }

    public List<E> getDatas() {
        return datas;
    }

    public void setDatas(List<E> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    public abstract void onBindNormalViewHolderHandle(RecyclerView.ViewHolder holder, int position);
    public abstract void onBindFooterViewHolderHandle(RecyclerView.ViewHolder holder, int position);

    public abstract RecyclerView.ViewHolder onCreateNormalViewHolderHandle(ViewGroup parent, int viewType);
    public abstract RecyclerView.ViewHolder onCreateFooterViewHolderHandle(ViewGroup parent, int viewType);
}

