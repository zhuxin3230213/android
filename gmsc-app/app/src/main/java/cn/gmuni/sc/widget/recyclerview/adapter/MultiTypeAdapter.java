package cn.gmuni.sc.widget.recyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.gmuni.sc.widget.recyclerview.type.TypeFactory;
import cn.gmuni.sc.widget.recyclerview.type.TypeFactoryForList;
import cn.gmuni.sc.widget.recyclerview.holder.BaseViewHolder;
import cn.gmuni.sc.widget.recyclerview.model.Visitable;

/**
 * @Author: zhuxin
 * @Date: 2018/8/27 09:36
 * @Description:
 */
public class MultiTypeAdapter extends RecyclerView.Adapter<BaseViewHolder> implements View.OnClickListener {

    private OnItemClickListener mItemClickListener; //为RecyclerView的每个子item设置setOnClickListener
    private TypeFactory typeFactory;
    private List<Visitable> models;

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    private Object tag;

    public MultiTypeAdapter(List<Visitable> models) {
        this.models = models;
        this.typeFactory = new TypeFactoryForList();

    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        View itemView = View.inflate(context,viewType,null);
        itemView.setOnClickListener(this); //item点击事件
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        itemView.setLayoutParams(layoutParams); //处理布局文件宽高设置不匹配问题
        return typeFactory.createViewHolder(viewType,itemView);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.setUpView(models.get(position),position,this);
        holder.itemView.setTag(position); //设置和item相关的数据
    }

    //重写方法防止数据渲染混乱
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if(null == models){
            return 0;
        }
        return models.size();
    }

    @Override
    public int getItemViewType(int position) {
        return models.get(position).type(typeFactory);
    }

    //点击事件
    @Override
    public void onClick(View v) {
        if (mItemClickListener!=null){
            mItemClickListener.onItemClick((Integer) v.getTag());
        }
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }
    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public List<Visitable> getModels() {
        return models;
    }
}
