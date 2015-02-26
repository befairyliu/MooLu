package com.moolu.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.moolu.R;
import com.moolu.application.NApplication;
import com.moolu.framework.NananLog;
import com.moolu.framework.entity.model.Center;
import com.moolu.framework.entity.model.Entity;

import org.slf4j.Logger;

import java.util.List;

/**
 * Created by Nanan on 2/5/2015.
 */
public class AppSelectAdapter extends BaseAdapter{
    //TODO for LayoutInflater class
    private LayoutInflater inflater = null;
    private List<Center> items = null;
    private Bitmap icon;
    private int selectedPosition = -1;
    private Entity entity = null;
    private Activity mContext;

    private boolean onlyShowLocaleDesc = false;
    private int selectedTextColor = Color.BLACK;
    private int textColor = Color.BLACK;

    private final static Logger Log = new NananLog(AppSelectAdapter.class);
    private NApplication application;
    private String commonsavedLocaleStr;
    private String checkedRadioButton = "";
    private String uncheckedRadioButton = "";

    public void setSelectedPosition(int position){
        selectedPosition = position;
    }
    public AppSelectAdapter(Activity context,List<Center> arrayList){
        inflater = LayoutInflater.from(context);
        this.items = arrayList;
        application = (NApplication)context.getApplication();
        entity = application.getEntity();
        this.mContext = context;
        if(entity == null){
            Log.error("Unable parse regional config");
            return;
        }
    }

    @Override
    public int getCount() {
        return items != null? items.size():0;
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Center item = items.get(position);
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_app_select,null);
            holder = new ViewHolder();
            holder.textView = (TextView)convertView.findViewById(R.id.text);
            holder.status = (TextView)convertView.findViewById(R.id.tv_status);
            holder.icon = (ImageView)convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        if(item != null){
            holder.textView.setText(item.getRegionName());
        } else {
            holder.textView.setText("");
        }

        if(selectedPosition == position){
            holder.icon.setImageResource(R.drawable.red_checked_icon);
            holder.textView.setContentDescription(holder.textView.getText()+","+checkedRadioButton);
            holder.textView.setTextColor(selectedTextColor);
        } else {
            holder.icon.setImageResource(R.drawable.red_unchecked_icon);
            holder.textView.setContentDescription(holder.textView.getText()+","+uncheckedRadioButton);
            holder.textView.setTextColor(textColor);
        }

        switch(item.getApp_status()){
            case Center.status_default:
                holder.status.setText(R.string.status_default);
                break;
            case Center.status_invalid:
                holder.status.setText(R.string.status_invalid);
                break;
            case Center.status_need_update:
                holder.status.setText(R.string.status_need_update);
                break;
        }

        return convertView;
    }

    class ViewHolder{
        TextView textView;
        TextView status;
        ImageView icon;
    }

    public void setTextColor(int unselectedColor, int selectedColor){
        this.textColor = unselectedColor;
        this.selectedTextColor = selectedColor;
    }
}
