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
import android.widget.ImageView;
import android.widget.TextView;

import com.fyhack.pro.databindingpro.vo.Card;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        recyclerView = (RecyclerView) findViewById(R.id.activity_card_recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        gridLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        datas = getCardDatas(20);
        recyclerViewAdapter = new RecyclerViewAdapter(this,datas);
        recyclerViewAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.i(TAG, "onItemClick " + position);
                if(position%2 == 0)
                    removeItem(position);
                else
                    addItem(position);

            }
        });
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private ArrayList<Card> getCardDatas(int count){
        ArrayList<Card> arrayList = new ArrayList<Card>();

        for(int i=0;i<count;i++){
            arrayList.add(new Card(""+i));
        }

        return arrayList;
    }

    private void addItem(int position){
        datas.add(position,new Card(""+position));
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

        public RecyclerViewAdapter(Context context,List<Card> datas){
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
            ((ViewHolder)holder).textView.setText(datas.get(position).getName());
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
