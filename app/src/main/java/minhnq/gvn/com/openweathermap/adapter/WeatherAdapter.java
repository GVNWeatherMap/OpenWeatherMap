package minhnq.gvn.com.openweathermap.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import minhnq.gvn.com.openweathermap.R;
import minhnq.gvn.com.openweathermap.model.WeatherFiveDay;
import minhnq.gvn.com.openweathermap.model.WeatherOneDay;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder>  {

    private List<WeatherOneDay> mOneDayList;
    private Context mContext;
    public WeatherAdapter(Context context){
        this.mContext = context;
        mOneDayList = new ArrayList<>();
    }

    public void setDatas( List<WeatherOneDay> list){
        this.mOneDayList = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_main_weather,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        WeatherOneDay weatherOneDay = mOneDayList.get(i);
        long unixTime = weatherOneDay.dt;
        Date date = new Date(unixTime*1000L);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String day = simpleDateFormat.format(date);

        viewHolder.tvDay.setText(weatherOneDay.dt_txt);

        int  temp_min = (int) Math.round(weatherOneDay.main.temp_min);
        int  temp_max = (int) Math.round(weatherOneDay.main.temp_max);
        viewHolder.tvMinTemp.setText(String.valueOf(temp_min));
        viewHolder.tvMaxTemp.setText(String.valueOf(temp_max));
        Glide.with(mContext).load("http://openweathermap.org/img/w/"+ weatherOneDay.weather.get(0).icon +".png").into(viewHolder.imgIcon);
    }

    @Override
    public int getItemCount() {
        return mOneDayList.size();
    }

     class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvDay, tvMinTemp, tvMaxTemp;
        private ImageView imgIcon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDay = itemView.findViewById(R.id.txv_item_day);
            tvMinTemp = itemView.findViewById(R.id.txv_item_temp_min);
            tvMaxTemp = itemView.findViewById(R.id.txv_item_temp_max);
            imgIcon = itemView.findViewById(R.id.img_item_icon);
        }
    }
}
