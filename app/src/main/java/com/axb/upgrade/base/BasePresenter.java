package com.axb.upgrade.base;

import android.content.Context;

public class BasePresenter<V extends IBaseView> implements IBasePresenter<V> {

   // Fields
   protected Context mContext;
   protected V mView;

   // Constructors
   public BasePresenter(Context context) {
      mContext = context;
   }

   // Override functions for IBasePresenter
   @Override
   public void attachView(V view) {
      mView = view;
   }
   @Override
   public void detachView() {
      mView = null;
   }

}
