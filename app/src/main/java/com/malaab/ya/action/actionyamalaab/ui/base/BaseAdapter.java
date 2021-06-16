package com.malaab.ya.action.actionyamalaab.ui.base;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;

import com.malaab.ya.action.actionyamalaab.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public abstract class BaseAdapter<T> extends RecyclerView.Adapter implements Filterable {

    public abstract RecyclerView.ViewHolder setViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindData(RecyclerView.ViewHolder holder, int position, T val);

    private Handler handler = new Handler();

    protected List<T> list;
    private List<T> originalList;

    //region LoadMore
    private final int VIEW_ITEM = 1;
    public static final int VIEW_PROGRESS = 2;
    //endregion

    /* The minimum amount of items to have below your current scroll position before loading more. */
    private int visibleThreshold = 2;

    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    //endregion


    public BaseAdapter(List<T> list) {
        this.originalList = list;
        this.list = list;
    }


    @Override
    public int getItemViewType(int position) {
        return list.get(position) != null ? VIEW_ITEM : VIEW_PROGRESS;      /* 0 To show loading progress */
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {
            return setViewHolder(parent, viewType);

        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_loadmore, parent, false);
            return new ProgressViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        onBindData(holder, position, list.get(position));
    }


    //region LoadMore
    public void setRecyclerView(RecyclerView recyclerView) {
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

//                    AppLogger.w("loading = " + loading + "\n totalItemCount = " + totalItemCount + "\n lastVisibleItem = " + lastVisibleItem);

                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        // End has been reached
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    public void showLoadingMore() {
        addItem(getItemCount(), null);
    }

    public void hideLoadingMore() {
        if (getItemCount() - 1 >= 0 && list.get(getItemCount() - 1) == null) {
            removeItem(getItemCount() - 1);
        }
    }

    public void setLoaded() {
        loading = false;
    }
    //endregion


    @Override
    public int getItemCount() {
        return list.size();
    }


    public void addItem(T item, boolean withAnimation) {
        if (!list.contains(item)) {
            list.add(item);
        }

        if (withAnimation) {
            this.notifyItemInserted(getItemCount() - 1);
        } else {
            this.notifyDataSetChanged();
        }
    }

    public void addItem(int position, T item, boolean withAnimation) {
        if (!list.contains(item)) {
            list.add(position, item);
        }

        if (withAnimation) {
            this.notifyItemInserted(position);
        } else {
            this.notifyDataSetChanged();
        }
    }

    public void addItem(int position, T item) {
        if (!list.contains(item)) {
            list.add(position, item);
        }

        Runnable r = new Runnable() {
            public void run() {
                notifyDataSetChanged();
            }
        };
        handler.post(r);
    }

    public void setItem(T item, int pos) {
        list.set(pos, item);
        this.notifyDataSetChanged();
    }

    public void updateItem(T item, int pos, boolean withAnimation) {
        if (list.contains(item)) {

            list.remove(pos);
            list.add(pos, item);

            if (withAnimation) {
                this.notifyItemChanged(pos);
            } else {
                this.notifyDataSetChanged();
            }
        }
    }

    public void updateItem(T item, boolean withAnimation) {
        if (list.contains(item)) {

            int pos = list.indexOf(item);
            list.remove(pos);
            list.add(pos, item);

            if (withAnimation) {
                this.notifyItemChanged(pos);
            } else {
                this.notifyDataSetChanged();
            }
        }
    }

    public void updateItem(int pos) {
        T item = list.get(pos);

        if (item != null) {
            list.remove(pos);
            list.add(pos, item);

            this.notifyItemChanged(pos);
        }
    }


    public void removeItem(T item, boolean withAnimation) {
        if (list.contains(item)) {
            int pos = list.indexOf(item);

            if (withAnimation) {
                removeItem(pos);
            } else {
                this.notifyDataSetChanged();
            }
        }
    }

    public void removeItem(int position) {
        list.remove(position);
        this.notifyDataSetChanged();
    }


    public void setItems(List<T> items) {
        list = items;
        this.notifyDataSetChanged();
    }

    public void addItems(List<T> items) {
        list.addAll(items);
        this.notifyDataSetChanged();
    }


    public T getItem(int position) {
        return list.get(position);
    }

    public int getItemPosition(T object) {
        return list.indexOf(object);
    }


    public List<T> getItems() {
        return list;
    }


    public void clear() {
        if (list != null) {
            list.clear();
            originalList.clear();
            this.notifyDataSetChanged();
        }
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint.length() == 0) {
                    list = originalList;
                } else {
                    list = getFilteredResults(constraint.toString().toLowerCase());
                }

                FilterResults results = new FilterResults();
                results.values = list;

                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list = (List<T>) results.values;
                BaseAdapter.this.notifyDataSetChanged();
            }

        };
    }

    private List<T> getFilteredResults(String constraint) {
        List<T> results = new ArrayList<>();

        if (originalList != null && originalList.size() > 0) {
            for (T item : originalList) {
//                if (item instanceof Transaction && StringUtils.contain(((Transaction) item).transactionId, constraint)) {
//                    results.add(item);
//
//                } else if (item instanceof Batch && StringUtils.contain(((Batch) item).batchId, constraint)) {
//                    results.add(item);
//
//                } else if (item instanceof Driver && StringUtils.contain(((Driver) item).name, constraint)) {
//                    results.add(item);
//                }
            }
        }

        return results;
    }


    public static class ProgressViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.pBar_loading)
        ProgressBar pBar_loading;


        public ProgressViewHolder(View v) {
            super(v);

            ButterKnife.bind(this, itemView);
        }


        public void bind() {
            pBar_loading.setIndeterminate(true);
        }
    }

}
