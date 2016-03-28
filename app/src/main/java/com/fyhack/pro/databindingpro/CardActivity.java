package com.fyhack.pro.databindingpro;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;

import com.fyhack.pro.databindingpro.adapter.CardRecyclerViewAdapter;
import com.fyhack.pro.databindingpro.adapter.OnRecyclerViewItemClickListener;
import com.fyhack.pro.databindingpro.vo.Card;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

//        try {
//            ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
//            StatConfig.setDebugEnable(applicationInfo.metaData.getBoolean("isDebug"));
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        StatService.trackCustomEvent(this,"onCreate",">onCreate...");

        init();
    }

    private void init(){
        recyclerView = (RecyclerView) findViewById(R.id.activity_card_recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        gridLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(8);

        recyclerViewAdapter = new CardRecyclerViewAdapter(this,getCardDatas(20,2000));
        recyclerViewAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.item_card:
                        recyclerViewAdapter.removeItem(position);   //删除
                        break;
                    case R.id.item_footer_card:
//                        recyclerViewAdapter.removeAllItem(new int[]{0, 2, 3});    //批量删除
//                        recyclerViewAdapter.addRangeItems(position, getCardDatas(10, position+1000)); //批量添加
                        recyclerViewAdapter.addItem(position,new Card(""+position,"http://cos.myqcloud.com/1001029/batchmsg_testing/testimg/"+(position+1)+".jpg"));  //添加
//                        recyclerViewAdapter.setItem(position-1,new Card("" + position, "http://cos.myqcloud.com/1001029/batchmsg_testing/testimg/" + (position+1000) + ".jpg"));  //变更
                        break;
                }
            }
        });
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private ArrayList<Card> getCardDatas(int count,int picStartNum){
        ArrayList<Card> arrayList = new ArrayList<>();
        for(int i=0;i<count;i++){
            Card card = new Card(""+(picStartNum+i),"http://cos.myqcloud.com/1001029/batchmsg_testing/testimg/"+(i+picStartNum)+".jpg");
            arrayList.add(card);
        }
        return arrayList;
    }
}
