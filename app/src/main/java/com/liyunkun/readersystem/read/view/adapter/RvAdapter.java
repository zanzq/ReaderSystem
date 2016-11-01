package com.liyunkun.readersystem.read.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liyunkun.readersystem.R;
import com.liyunkun.readersystem.both.module.bean.PageBean;

import java.util.List;

/**
 * Created by liyunkun on 2016/11/1 0001.
 */
public class RvAdapter extends RecyclerView.Adapter {
    private List<PageBean> list;
    private Context context;
    private LayoutInflater inflater;

    public RvAdapter(List<PageBean> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.read_rv_item, parent, false);
        return new ReadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ReadViewHolder viewHolder = (ReadViewHolder) holder;
        PageBean pageBean = list.get(position);
        viewHolder.contents.setText(pageBean.getMessage());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class ReadViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView contents;
        TextView pager;
        TextView chapter;
        TextView currentTime;

        public ReadViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            contents = (TextView) itemView.findViewById(R.id.contents);
            pager = (TextView) itemView.findViewById(R.id.pager);
            chapter = (TextView) itemView.findViewById(R.id.chapter);
            currentTime = (TextView) itemView.findViewById(R.id.current_time);

        }
    }
}
