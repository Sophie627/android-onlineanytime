package com.austraila.online_anytime.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.austraila.online_anytime.R;
import com.austraila.online_anytime.model.Listmodel;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter implements Filterable {
    ArrayList listItem = new ArrayList<>();
    public Context context;
    public ArrayList<Listmodel> employeeArrayList;
    public ArrayList<Listmodel> orig;

    public CustomAdapter(Context context, ArrayList<Listmodel> employeeArrayList) {
        super();
        this.context = context;
        this.employeeArrayList = employeeArrayList;
    }

    public class EmployeeHolder
    {
        TextView title;
    }

    @Override
    public int getCount() {
        return employeeArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return employeeArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EmployeeHolder holder;
        if(convertView==null)
        {
            convertView=LayoutInflater.from(context).inflate(R.layout.mainlist_item, parent, false);
            holder=new EmployeeHolder();
            holder.title=(TextView) convertView.findViewById(R.id.listitem_title);
            convertView.setTag(holder);
        }
        else
        {
            holder=(EmployeeHolder) convertView.getTag();
        }

        holder.title.setText(employeeArrayList.get(position).getListText());
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Listmodel> results = new ArrayList<Listmodel>();
                if (orig == null)
                    orig = employeeArrayList;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final Listmodel g : orig) {
                            if (g.getListText().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                employeeArrayList = (ArrayList<Listmodel>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
