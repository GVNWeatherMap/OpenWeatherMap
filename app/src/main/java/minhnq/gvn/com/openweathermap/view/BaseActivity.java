package minhnq.gvn.com.openweathermap.view;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import minhnq.gvn.com.openweathermap.constract.base.IPresenterCallBack;
import minhnq.gvn.com.openweathermap.utils.RetrofitUtils;
import retrofit2.Retrofit;

public abstract class BaseActivity<presenter extends IPresenterCallBack> extends AppCompatActivity {

     presenter presenter;
     Retrofit.Builder builder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        builder = RetrofitUtils.newInstance();
        setContentView(getIdLayout());
        presenter = getPresenter();
    }

    protected abstract presenter getPresenter();

    abstract int getIdLayout();


}
