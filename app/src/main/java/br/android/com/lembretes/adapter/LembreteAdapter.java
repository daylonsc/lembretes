package br.android.com.lembretes.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.android.com.lembretes.R;
import br.android.com.lembretes.data.model.Lembrete;
import br.android.com.lembretes.uteis.RecyclerViewOnClickListenerHack;
import br.android.com.lembretes.uteis.Util;

public class LembreteAdapter extends RecyclerView.Adapter<LembreteAdapter.ViewHolder> {

    private static final String TAG = LembreteAdapter.class.getSimpleName();
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

    private List<Lembrete> list;

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r) {
        mRecyclerViewOnClickListenerHack = r;
    }

    public LembreteAdapter(List<Lembrete> list) {
        this.list = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textViewLembrete;
        public TextView textViewData;
        public TextView textViewId;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewLembrete = (TextView) itemView.findViewById(R.id.textViewLembrete);
            textViewData = (TextView) itemView.findViewById(R.id.textViewData);
            textViewId = (TextView) itemView.findViewById(R.id.textViewId);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mRecyclerViewOnClickListenerHack != null){
                mRecyclerViewOnClickListenerHack.onClickListener(view, getPosition());
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.lembrete_item_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Lembrete item = list.get(position);
        holder.textViewLembrete.setText(item.getLembrete());
        holder.textViewData.setText(Util.format(item.getDataHora(), "dd/MM/yyyy"));
        holder.textViewId.setText(String.valueOf(item.getId()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}