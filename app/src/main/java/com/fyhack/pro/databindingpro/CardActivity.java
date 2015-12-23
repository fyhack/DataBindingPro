package com.fyhack.pro.databindingpro;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.fyhack.pro.databindingpro.vo.Card;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * CardActivity
 * RecyclerView + CardView 视图demo
 * <p/>
 *
 * @author elc_simayi
 * @since 2015/11/23
 */

public class CardActivity extends AppCompatActivity {
    private final String TAG = "CardActivity";
    private RecyclerView recyclerView;

    RecyclerViewAdapter recyclerViewAdapter;
    ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        init();
    }

    private void init(){
        recyclerView = (RecyclerView) findViewById(R.id.activity_card_recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        gridLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(8);

        recyclerViewAdapter = new RecyclerViewAdapter(this,getCardDatas(20,1));
        recyclerViewAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()){
                    case R.id.item_card:
                        recyclerViewAdapter.removeItem(position);   //删除
                        break;
                    case R.id.item_footer_card:
                        recyclerViewAdapter.removeAllItem(new int[]{0,2,3});    //批量删除
//                        recyclerViewAdapter.addRangeItems(position, getCardDatas(10, position+1000)); //批量添加
//                        recyclerViewAdapter.addItem(position,new Card(""+position,"http://cos.myqcloud.com/1001029/batchmsg_testing/testimg/"+(position+1)+".jpg"));  //添加
//                        recyclerViewAdapter.setItem(position-1,new Card("" + position, "http://cos.myqcloud.com/1001029/batchmsg_testing/testimg/" + (position+1000) + ".jpg"));  //变更
                        break;
                }
            }
        });
        recyclerView.setAdapter(recyclerViewAdapter);

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(CardActivity.this));

        Fresco.initialize(this);
    }

    private ArrayList<Card> getCardDatas(int count,int picStartNum){
        ArrayList<Card> arrayList = new ArrayList<>();
        for(int i=0;i<count;i++){
            Card card = new Card(""+(picStartNum+i),"http://cos.myqcloud.com/1001029/batchmsg_testing/testimg/"+(i+picStartNum)+".jpg");
            arrayList.add(card);
        }
        return arrayList;
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter{
        private static final int TYPE_NORMAL = 0;
        private static final int TYPE_FOOTER = 1;
        private static final int FOOTER_NUM = 1;

        private Context context;
        private List<Card> datas;
        private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

        public RecyclerViewAdapter(Context context,List datas){
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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card,parent,false);
                NormalViewHolder viewHolder = new NormalViewHolder(view);
                Log.i("onCreateViewHolder", "NormalViewHolder");
                return viewHolder;
            }else{
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footer_card,parent,false);
                FooterViewHolder viewHolder = new FooterViewHolder(view);
                Log.i("onCreateViewHolder", "FooterViewHolder");
                return viewHolder;
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (getItemViewType(position) == TYPE_NORMAL){
                ((NormalViewHolder)holder).textView.setText(datas.get(position).getName());
                /**
                 * 采用universalimageloader加载图片测试OOM
                 */
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .cacheInMemory(true)  //缓存在内存中
                        .cacheOnDisk(true)  //磁盘缓存
                        .build();
                imageLoader.displayImage(datas.get(position).getPic_url(),
                        ((NormalViewHolder) holder).simpleDraweeView, options);

                /**
                 * 采用Fresco加载图片测试OOM
                 */
                /*Uri uri = Uri.parse(datas.get(position).getPic_url());
                ((NormalViewHolder)holder).simpleDraweeView.setImageURI(uri);*/
            }else if(getItemViewType(position) == TYPE_FOOTER){
                ((FooterViewHolder)holder).textView.setText("Footer");
                ((FooterViewHolder)holder).imageView.setImageResource(R.drawable.plus);
            }
        }

        @Override
        public int getItemCount() {
            return getDataCount() + FOOTER_NUM;
        }

        public int getDataCount() {
            return datas.size();
        }

        public void addItem(int position,Card item){
            if (position > -1 && position <= getDataCount()){
                datas.add(position,item);
                notifyItemInserted(position);
            }
        }

        public Card removeItem(int position){
            Card card = null;
            if (position > -1 && position <= getDataCount()-1){
                card = datas.remove(position);
                notifyItemRemoved(position);
            }
            return card;
        }

        public void removeAllItem(int[] position){
            for(int i=0; i<position.length; i++){
                removeItem(position[i]-i);
            }
        }

        public void setItem(int position,Card item){
            if (position > -1 && position <= getDataCount()-1){
                datas.set(position,item);
                notifyItemChanged(position);
            }
        }

        public void addRangeItems(int position, List<Card> items){
            if (position > -1 && position <= getDataCount()) {
                datas.addAll(position, items);
                notifyItemRangeInserted(position,items.size());
            }
        }

        public List<Card> getDatas() {
            return datas;
        }

        public void setDatas(List<Card> datas) {
            this.datas = datas;
            notifyDataSetChanged();
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

    public interface OnRecyclerViewItemClickListener{
        void onItemClick(View view, int position);
    }
}
