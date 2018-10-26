package minhnq.gvn.com.openweathermap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import minhnq.gvn.com.openweathermap.R;
import minhnq.gvn.com.openweathermap.model.Location;

public class LocationApdater extends RecyclerView.Adapter<LocationApdater.ViewHolder> {
    private Context mContext;
    private List<Location> locationList;
    private ILocationListener mILocationListener;


    public LocationApdater ( Context context){
        this.mContext = context;
    }

    public void setDatas(List<Location> list){
        locationList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LocationApdater.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(mContext).inflate(R.layout.row_item_navigation_all_location,parent,false);
        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationApdater.ViewHolder holder, int position) {
        Location location = locationList.get(position);
        holder.tvCityName.setText(location.cityName);
    }

    @Override
    public int getItemCount(){
        return locationList.size();
    }

     class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvCityName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCityName = itemView.findViewById(R.id.txv_row_item_city_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mILocationListener.onClickLocation(locationList.get(getAdapterPosition()));
                }
            });
        }
    }

    public void initILocationListener(ILocationListener iLocationListener){
        this.mILocationListener = iLocationListener;
    }

   public interface ILocationListener{
        void onClickLocation(Location location);
    }
}
