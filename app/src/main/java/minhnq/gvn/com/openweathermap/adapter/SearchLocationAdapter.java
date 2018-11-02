package minhnq.gvn.com.openweathermap.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import minhnq.gvn.com.openweathermap.R;
import minhnq.gvn.com.openweathermap.model.Location;


public class SearchLocationAdapter extends ArrayAdapter<Location> {

    private Context context;
    private int resourceId;
    private List<Location> items = new ArrayList<>();
    private List<Location>  tempItems, suggestions;

    public SearchLocationAdapter(@NonNull Context context, int resource, @NonNull List<Location> objects) {
        super(context, resource, objects);

        this.items = objects;
        this.context = context;
        this.resourceId = resource;
        tempItems = new ArrayList<>(items);
        suggestions = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        try {
            if (convertView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                view = inflater.inflate(resourceId, parent, false);
            }
            Location location = getItem(position);

            TextView name = view.findViewById(R.id.txv_row_item_city_name);
            name.setText(location.cityName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Nullable
    @Override
    public Location getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return locationFilter;
    }
    private Filter locationFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            if(charSequence!= null){
                suggestions.clear();
                for(Location location:tempItems){
                    if(location.cityName.toLowerCase().startsWith(charSequence.toString().toLowerCase())){
                        suggestions.add(location);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            }else {
                FilterResults filterResults = new FilterResults();
                return filterResults;
            }
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            ArrayList<Location> tempLocation = (ArrayList<Location>) filterResults.values;
            if(filterResults != null && filterResults.count>0){
                clear();
                for(Location location: tempLocation){
                    add(location);
                    notifyDataSetChanged();
                }
            }
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            Location location = (Location) resultValue;
            return location.cityName;
        }
    };
}
