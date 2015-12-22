package com.fyhack.pro.databindingpro.vo;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.fyhack.pro.databindingpro.BR;

/**
 * Card
 * 使用dataBind的pojo
 * <p/>
 *
 * @author elc_simayi
 * @since 2015/11/19
 */

public class Card extends BaseObservable{
    private String name;
    private String pic_url;

    public Card(String name, String pic_url) {
        this.name = name;
        this.pic_url = pic_url;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
        notifyPropertyChanged(BR.pic_url);
    }

}
