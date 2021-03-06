package com.example.laikipiauniversityapp.Filters;


import android.widget.Filter;


import com.example.laikipiauniversityapp.Adapter.AdapterCategory;
import com.example.laikipiauniversityapp.models.ModelCategory;

import java.util.ArrayList;

public class FilterCategory extends Filter {

    ArrayList<ModelCategory> filterList;

    AdapterCategory adapterCategory;

    public FilterCategory(ArrayList<ModelCategory> filterList, AdapterCategory adapterCategory) {
        this.filterList = filterList;
        this.adapterCategory = adapterCategory;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();

        if (constraint != null && constraint.length() > 0){
            constraint = constraint.toString().toUpperCase();
            ArrayList<ModelCategory> filteredModels = new ArrayList<>();

            for (int i=0; i<filterList.size(); i++){
                //validate

                if (filterList.get(i).getCategory().toUpperCase().contains(constraint)){
                    // add to filtered list
                    filteredModels.add(filterList.get(i));
                }
            }
            results.count = filteredModels.size();
            results.values = filteredModels;
        }
        else {
            results.count = filterList.size();
            results.values = filterList;
        }
        return results ;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        // apply filter
        adapterCategory.categoryArrayList = (ArrayList<ModelCategory>) results.values;
        // notify changes
        adapterCategory.notifyDataSetChanged();
    }
}

