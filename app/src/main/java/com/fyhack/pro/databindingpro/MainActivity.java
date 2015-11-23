package com.fyhack.pro.databindingpro;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.fyhack.pro.databindingpro.databinding.ActivityMainBinding;
import com.fyhack.pro.databindingpro.vo.Card;

/**
 *
 */
public class MainActivity extends AppCompatActivity {
    public final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private Card card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        card = new Card(" " + System.currentTimeMillis());
        binding.setCard(card);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.activity_main_btn:
                card.setName(" " + System.currentTimeMillis());
                break;
        }
    }

}
