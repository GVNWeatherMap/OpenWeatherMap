package minhnq.gvn.com.openweathermap.presenter;

import minhnq.gvn.com.openweathermap.constract.base.IViewCallBack;

public class BasePresenter<view extends IViewCallBack> {
    view view;
    public BasePresenter(view view) {
        this.view = view;
    }
}
