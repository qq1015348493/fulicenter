package cn.ucai.fulicenter.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.Bean.CategoryChildBean;
import cn.ucai.fulicenter.Bean.CategoryGroupBean;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;


public class CategoryAdapter extends BaseExpandableListAdapter {
    Context context;
    ArrayList<CategoryGroupBean> groupList;
    ArrayList<ArrayList<CategoryChildBean>> childList;
    int id;
    public CategoryAdapter(Context context, ArrayList<CategoryGroupBean> groupList, ArrayList<ArrayList<CategoryChildBean>> childList) {
        this.context = context;
        this.groupList = groupList;
        this.childList = childList;
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childList.get(groupPosition).size();
    }

    @Override
    public CategoryGroupBean getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public CategoryChildBean getChild(int groupPosition, int childPosition) {
        return childList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.category_group, null);
            holder= new GroupViewHolder();
            holder.category_group_image = (ImageView) convertView.findViewById(R.id.category_group_image);
            holder.expanded = (ImageView) convertView.findViewById(R.id.expanded);
            holder.category_group_name = (TextView) convertView.findViewById(R.id.category_group_name);
            convertView.setTag(holder);
        }else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        holder.category_group_name.setText(groupList.get(groupPosition).getName());

        ImageLoader.downloadImg(context,holder.category_group_image,groupList.get(groupPosition).getImageUrl());
        if(isExpanded){
            holder.expanded.setImageResource(R.mipmap.expand_off);
        }else {
            holder.expanded.setImageResource(R.mipmap.expand_on);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder = null;
        if(convertView==null){
            convertView = View.inflate(context,R.layout.category_child,null);
            holder=new ChildViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            convertView.getTag();
            holder = (ChildViewHolder) convertView.getTag();
        }
        CategoryChildBean child = getChild(groupPosition,childPosition);
        ImageLoader.downloadImg(context,holder.category_child_image,childList.get(groupPosition).get(childPosition).getImageUrl());
        holder.category_child_name.setText(childList.get(groupPosition).get(childPosition).getName());
//        holder.category_child_linearLayout.setTag(childList.get(groupPosition).get(childPosition).getId());
        id = childList.get(groupPosition).get(childPosition).getId();
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {

        return false;
    }

    private class GroupViewHolder{
        ImageView category_group_image,expanded;
        TextView category_group_name;
    }
      class ChildViewHolder{
         @Bind(R.id.category_child_image)
         ImageView category_child_image;
         @Bind(R.id.category_child_name)
         TextView category_child_name;
         @Bind(R.id.category_child)
         LinearLayout category_child_linearLayout;

          public ChildViewHolder(View convertView) {
              ButterKnife.bind(this,convertView);
          }

          @OnClick(R.id.category_child)
         public  void OnClick(){
             String name = category_child_name.getText().toString();
             MFGT.gotoCategoryGoods((Activity) context,id,name);

         }

    }
    private class GroupView extends GroupViewHolder {
    }
}
