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
    List<Card> datas;
    ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        recyclerView = (RecyclerView) findViewById(R.id.activity_card_recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        gridLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(8);

        datas = getCardDatas(20);
        recyclerViewAdapter = new RecyclerViewAdapter(this,datas);
        recyclerViewAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()){
                    case R.id.item_card:
                        removeItem(position);
                        break;
                    case R.id.item_footer_card:
                        addItem(position);
                }
            }
        });
        recyclerView.setAdapter(recyclerViewAdapter);

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(CardActivity.this));

        Fresco.initialize(this);
    }

    private ArrayList<Card> getCardDatas(int count){
        ArrayList<Card> arrayList = new ArrayList<>();
        for(int i=0;i<count;i++){
            Card card = new Card(""+i,"http://cos.myqcloud.com/1001029/batchmsg_testing/testimg/"+(i+1)+".jpg");
            arrayList.add(card);
        }
        return arrayList;
    }

    private void addItem(int position){
        datas.add(position,new Card(""+position,"http://cos.myqcloud.com/1001029/batchmsg_testing/testimg/"+(position+1)+".jpg"));
        recyclerViewAdapter.notifyItemInserted(position);
}

    private void removeItem(int position){
        datas.remove(position);
        recyclerViewAdapter.notifyItemRemoved(position);
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter{
        private Context context;
        private List<Card> datas;
        private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

        private static final int TYPE_NORMAL = 0;
        private static final int TYPE_FOOTER = 1;

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
                Log.i("onCreateViewHolder","NormalViewHolder");
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
//            switch (position%3){
//                case 0:
//                    ((ViewHolder)holder).imageView.setImageResource(R.drawable.rect);
//                    break;
//                case 1:
//                    ((ViewHolder)holder).imageView.setImageResource(R.drawable.rect1);
//                    break;
//                case 2:
//                    ((ViewHolder)holder).imageView.setImageResource(R.drawable.rect2);
//                    break;
//            }

                /**
                 * 采用universalimageloader加载图片测试OOM
                 */
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .cacheInMemory(true)  //缓存在内存中
                        .cacheOnDisk(true)  //磁盘缓存
                        .build();
                imageLoader.displayImage(datas.get(position).getPic_url(),
                        ((NormalViewHolder)holder).simpleDraweeView,options);

                /**
                 * 采用Fresco加载图片测试OOM
                 */
            /*Uri uri = Uri.parse(datas.get(position).getPic_url());
            ((ViewHolder)holder).simpleDraweeView.setImageURI(uri);*/
            }else if(getItemViewType(position) == TYPE_FOOTER){
                ((FooterViewHolder)holder).textView.setText("Footer");
            }
        }

        @Override
        public int getItemCount() {
            //TODO
            return datas.size()+1;
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
