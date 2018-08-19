package ir.oveissi.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ir.oveissi.search.db.Suggestion;

public class MovieSearchAdapter extends RecyclerView.Adapter<MovieSearchAdapter.ViewHolder> {
    ItemClickListener itemClickListener;
    private Context mContext;
    private List<Suggestion> itemsData;

    MovieSearchAdapter(Context mContext, List<Suggestion> itemsData) {
        this.mContext = mContext;
        this.itemsData = itemsData;
    }

    void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    void clear() {
        this.itemsData.clear();
        notifyDataSetChanged();
    }

    void addItem(Suggestion post) {
        this.itemsData.add(post);
        notifyItemInserted(this.itemsData.size() - 1);
    }

    @Override
    public MovieSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_suggestion, parent, false);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Suggestion tempItem = itemsData.get(position);
        viewHolder.bind(tempItem);
    }

    @Override
    public int getItemCount() {
        return itemsData.size();
    }

    public interface ItemClickListener {
        void ItemClicked(int position, Suggestion item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvMovieTitle;

        ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            tvMovieTitle = itemLayoutView.findViewById(R.id.tvMovieTitle);
            itemLayoutView.setOnClickListener(this);

        }

        void bind(Suggestion item) {
            tvMovieTitle.setText(item.getTitle());
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.ItemClicked(getAdapterPosition(),
                        itemsData.get(getAdapterPosition()));
            }

        }
    }
}