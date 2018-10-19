package minhnq.gvn.com.openweathermap.constract;

import minhnq.gvn.com.openweathermap.constract.base.BaseContract;
import minhnq.gvn.com.openweathermap.constract.base.IPresenterCallBack;
import minhnq.gvn.com.openweathermap.constract.base.IViewCallBack;
import minhnq.gvn.com.openweathermap.model.Weathers;

public interface MainContract extends BaseContract {

    interface IMainView extends IViewCallBack {
        void onResponse(Weathers weather);
    }

    interface IMainPresenter extends IPresenterCallBack {
        void getWeatherNow(String lat,
                           String lng,
                           String appid,
                           String unit);
    }
}
