package cn.ucai.fulicenter.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.Bean.CategoryChildBean;
import cn.ucai.fulicenter.Bean.CategoryGroupBean;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.MainActivity;
import cn.ucai.fulicenter.adapter.CategoryAdapter;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.ConvertUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.OkHttpUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class Category extends Fragment {
    ExpandableListView melvCaregory;
    CategoryAdapter mAdapter;
    ArrayList<CategoryGroupBean> mGrouplist = new ArrayList<>();
    ArrayList<ArrayList<CategoryChildBean>> mChildList = new ArrayList<>();
    @Bind(R.id.Category_list)
    ExpandableListView CategoryList;
    Context context;
    int parentId;
    int pageid=1;

    public Category() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        context = (MainActivity) getContext();
        ButterKnife.bind(this, view);
        initData();
        return view;
    }

    private void initView() {
        mAdapter = new CategoryAdapter(context,mGrouplist,mChildList);
        CategoryList.setAdapter(mAdapter);
    }

    private void initData() {

        downloadGroupList();

    }


    private void downloadGroupList() {
        NetDao.downloadGroupList(context, new OkHttpUtils.OnCompleteListener<CategoryGroupBean[]>() {
            @Override
            public void onSuccess(CategoryGroupBean[] result) {
                ArrayList<CategoryGroupBean> arrayList = ConvertUtils.array2List(result);
                if(result!=null){

                    mGrouplist = arrayList;
//                    L.i("发"+mGrouplist.toString());
                    for(int i=0;i<result.length;i++){
                        parentId = mGrouplist.get(i).getId();
                        String S = "";
                        for(int j=0;j<100_00;j++){
                            S=S+"a";
                        }
                        downloadchikd(parentId);
                    }
                    initView();
                }
            }
            @Override
            public void onError(String error) {

            }
        });
    }

    private void  downloadchikd(int parentId) {

            NetDao.downloadChildList(context,parentId,pageid,new OkHttpUtils.OnCompleteListener<CategoryChildBean[]>() {
                @Override
                public void onSuccess(CategoryChildBean[] result) {

                    if(result!=null){
                        ArrayList<CategoryChildBean> child = ConvertUtils.array2List(result);
                        mChildList.add(child);
                    }
                }
                @Override
                public void onError(String error) {

                }
            });
        }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
