package com.fyhack.pro.databindingpro.vo;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.fyhack.pro.databindingpro.BR;

/**
 * News
 * <p/>
 *
 * @author elc_simayi
 * @since 2015/11/19
 */

public class Card extends BaseObservable{
    private String name;

    public Card(String name) {
        this.name = name;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

}
