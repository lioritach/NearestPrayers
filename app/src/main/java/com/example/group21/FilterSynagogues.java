package com.example.group21;

import android.widget.Filter;

import java.util.ArrayList;

public class FilterSynagogues extends Filter {

    private AdapterSynagogues adapterSynagogues;
    private ArrayList<ModelShowSynagogues> filterList;

    public FilterSynagogues(AdapterSynagogues adapterSynagogues, ArrayList<ModelShowSynagogues> filterList) {
        this.adapterSynagogues = adapterSynagogues;
        this.filterList = filterList;
    }


    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        //validate data for search query
        if(constraint != null && constraint.length() > 0){
            //search filed not empty, searching something, perform search

            //change to upper case, to make case sensitive
            constraint = constraint.toString().toUpperCase();
            //store our filtered list
            ArrayList<ModelShowSynagogues> filterModels = new ArrayList<>();
            for(int i = 0; i < filterList.size(); i++){
                if(filterList.get(i).getSynName().toUpperCase().contains(constraint)){
                    filterList.get(i).getCategory().toUpperCase().contains(constraint);
                    //add filtered data to list
                    filterModels.add(filterList.get(i));
                }
            }
            results.count = filterModels.size();
            results.values = filterModels;

        }else{
            //search filed empty, not searching, return original/all/complete list
            results.count = filterList.size();
            results.values = filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapterSynagogues.synagoguesArrayList = (ArrayList<ModelShowSynagogues>) results.values;
        //refresh adapter
        adapterSynagogues.notifyDataSetChanged();
    }
}
