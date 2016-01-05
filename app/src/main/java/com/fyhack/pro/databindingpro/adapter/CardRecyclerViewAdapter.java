package com.fyhack.pro.databindingpro.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.fyhack.pro.databindingpro.R;
import com.fyhack.pro.databindingpro.vo.Card;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

/**
 * CardRecyclerViewAdapter
 * <p/>
 *
 * @author elc_simayi
 * @since 2015/12/23
 */

public class CardRecyclerViewAdapter extends RecyclerViewAdapter<Card> {
    private ImageLoader imageLoader;
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    public CardRecyclerViewAdapter(Context context, List<Card> datas) {
        super(context, datas);

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        Fresco.initialize(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateNormalViewHolderHandle(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card,parent,false);
        NormalViewHolder viewHolder = new NormalViewHolder(view);
        return viewHolder;
    }

    @Override
    public RecyclerView.ViewHolder onCreateFooterViewHolderHandle(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footer_card, parent, false);
        FooterViewHolder viewHolder = new FooterViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindNormalViewHolderHandle(RecyclerView.ViewHolder holder, int position) {
        ((NormalViewHolder)holder).textView.setText(getDatas().get(position).getName());
        /**
         * 采用universalimageloader加载图片测试OOM
         */
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)  //缓存在内存中
                .cacheOnDisk(true)  //磁盘缓存
                .build();
        imageLoader.displayImage(getDatas().get(position).getPic_url(),
                ((NormalViewHolder) holder).simpleDraweeView, options);

        /**
         * 采用Fresco加载图片测试OOM
         */
        /*Uri uri = Uri.parse(getDatas().get(position).getPic_url());
        ((NormalViewHolder)holder).simpleDraweeView.setImageURI(uri);*/
    }

    @Override
    public void onBindFooterViewHolderHandle(RecyclerView.ViewHolder holder, int position) {
        ((FooterViewHolder)holder).textView.setText("Footer");
        ((FooterViewHolder)holder).imageView.setImageResource(R.drawable.plus);
    }

    public class NormalViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;
        public SimpleDraweeView simpleDraweeView;
        public NormalViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_card_text_view);
            simpleDraweeView = (SimpleDraweeView) itemView.findViewById(R.id.item_card_image_view);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    onRecyclerViewItemClickListener.onItemClick(view,getAdapterPosition());
                }
            });
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;
        public ImageView imageView;
        public FooterViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_footer_card_text_view);
            imageView = (ImageView) itemView.findViewById(R.id.item_footer_card_image_view);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    onRecyclerViewItemClickListener.onItemClick(view,getAdapterPosition());
                }
            });
        }
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }
}
