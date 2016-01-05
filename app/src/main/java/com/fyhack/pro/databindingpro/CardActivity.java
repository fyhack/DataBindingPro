package com.fyhack.pro.databindingpro;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;

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
    CardRecyclerViewAdapter recyclerViewAdapter;

    RecyclerViewAdapter recyclerViewAdapter;
    List datas;

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

        recyclerViewAdapter = new CardRecyclerViewAdapter(this,getCardDatas(20,1));
        recyclerViewAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.item_card:
                        recyclerViewAdapter.removeItem(position);   //删除
                        break;
                    case R.id.item_footer_card:
                        recyclerViewAdapter.removeAllItem(new int[]{0, 2, 3});    //批量删除
//                        recyclerViewAdapter.addRangeItems(position, getCardDatas(10, position+1000)); //批量添加
//                        recyclerViewAdapter.addItem(position,new Card(""+position,"http://cos.myqcloud.com/1001029/batchmsg_testing/testimg/"+(position+1)+".jpg"));  //添加
//                        recyclerViewAdapter.setItem(position-1,new Card("" + position, "http://cos.myqcloud.com/1001029/batchmsg_testing/testimg/" + (position+1000) + ".jpg"));  //变更
                        break;
                }
            }
        });
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private ArrayList<String> getCardDatas(int count){
        ArrayList<String> arrayList = new ArrayList<String>();

        for(int i=0;i<count;i++){
            arrayList.add(""+i);
        }
        return arrayList;
    }

    private void addItem(int position){
        datas.add(position,""+position);
        recyclerViewAdapter.notifyItemInserted(position);
    }

    private void removeItem(int position){
        datas.remove(position);
        recyclerViewAdapter.notifyItemRemoved(position);
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter{
        private Context context;
        private List datas;
        private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

        public RecyclerViewAdapter(Context context,List datas){
            this.context = context;
            this.datas = datas;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card,parent,false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((ViewHolder)holder).textView.setText((String)datas.get(position));
            ((ViewHolder)holder).imageView.setImageResource(R.drawable.rect2);
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            public TextView textView;
            public ImageView imageView;
            public ViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.item_card_text_view);
                imageView = (ImageView) itemView.findViewById(R.id.item_card_image_view);

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
