package com.malaab.ya.action.actionyamalaab.custom;

import android.app.Activity;
import android.app.Dialog;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.data.model.DirectionListItem;
import com.malaab.ya.action.actionyamalaab.data.model.DurationListItem;
import com.malaab.ya.action.actionyamalaab.data.model.GenericListItem;
import com.malaab.ya.action.actionyamalaab.data.model.SizeListItem;
import com.malaab.ya.action.actionyamalaab.data.network.model.City;
import com.malaab.ya.action.actionyamalaab.data.network.model.Region;
import com.malaab.ya.action.actionyamalaab.ui.adapter.CitiesAdapter;
import com.malaab.ya.action.actionyamalaab.ui.adapter.DirectionItemsAdapter;
import com.malaab.ya.action.actionyamalaab.ui.adapter.DurationItemsAdapter;
import com.malaab.ya.action.actionyamalaab.ui.adapter.GenericItemsAdapter;
import com.malaab.ya.action.actionyamalaab.ui.adapter.RegionsAdapter;
import com.malaab.ya.action.actionyamalaab.ui.adapter.SizeItemsAdapter;
import com.malaab.ya.action.actionyamalaab.utils.ScreenUtils;
import com.malaab.ya.action.actionyamalaab.utils.StringUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DialogList {

    @BindView(R.id.dialog_btn_close)
    ImageButton btn_close;
    @BindView(R.id.dialog_txt_title)
    TextView txt_title;

    @BindView(R.id.dialog_rv_list)
    RecyclerView rv_list;

    private Activity mActivity;
    private Dialog mDialog;

    private GenericItemsAdapter mGenericItemsAdapter;
    private RegionsAdapter mRegionsAdapter;
    private CitiesAdapter mCitiesAdapter;
    private DirectionItemsAdapter mDirectionAdapter;
    private SizeItemsAdapter mSizeAdapter;
    private DurationItemsAdapter mDurationAdapter;


    public DialogList with(Activity activity) {
        if (mDialog == null) {
            mActivity = activity;
            mDialog = new Dialog(mActivity);
        }

        return this;
    }

    public DialogList build() {
        if (mDialog.getWindow() != null) {
            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            mDialog.setContentView(R.layout.custom_dialog_list);

            mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            mDialog.setCanceledOnTouchOutside(true);

            ButterKnife.bind(this, mDialog);
        }

        rv_list.setHasFixedSize(true);
        rv_list.setLayoutManager(new LinearLayoutManager(mActivity));
        rv_list.setNestedScrollingEnabled(false);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        rv_list.setLayoutParams(lp);

        return this;
    }


    public void onConfigurationChanged() {
        if (mDialog != null && mDialog.isShowing()) {
            if (mDialog.getWindow() != null)
                mDialog.getWindow().setLayout((int) (ScreenUtils.getScreenWidth(mActivity) * 0.9f), (int) (ScreenUtils.getScreenHeight(mActivity) * 0.9f));
        }
    }


    @OnClick(R.id.dialog_btn_close)
    void back() {
        dismiss();
    }


    public DialogList withTitle(@StringRes int resId) {
        withTitle(mActivity.getString(resId));
        return this;
    }

    public DialogList withTitle(String title) {
        txt_title.setText(title);
        return this;
    }


    public void addItems(List<GenericListItem> items) {
        mGenericItemsAdapter = new GenericItemsAdapter(items);
    }

    public void showItems() {
        rv_list.setAdapter(mGenericItemsAdapter);
        show();
    }


    public void addRegions(List<Region> items) {
        mRegionsAdapter = new RegionsAdapter(items);
    }

    public void showRegions() {
        rv_list.setAdapter(mRegionsAdapter);
        show();
    }

    public void setRegionSelected(String region) {
        mRegionsAdapter.setSelected(new Region(region, null));
    }


    public void addCities(List<City> items) {
        mCitiesAdapter = new CitiesAdapter(items);
    }

    public void showCities() {
        rv_list.setAdapter(mCitiesAdapter);
        show();
    }


    public void addDirections(List<DirectionListItem> items) {
        mDirectionAdapter = new DirectionItemsAdapter(items);
    }

    public void showDirections() {
        rv_list.setAdapter(mDirectionAdapter);
        show();
    }


    public void addSizes(List<SizeListItem> items) {
        mSizeAdapter = new SizeItemsAdapter(items);
    }

    public void showSizes() {
        if (mDurationAdapter.getItemCount() == 0) {
            showMessage(mActivity.getString(R.string.msg_day_select));
            return;
        }

        txt_title.setText(mActivity.getString(R.string.title_size_select));
        rv_list.setAdapter(mSizeAdapter);
        show();
    }

    public void setSizeSelected(SizeListItem item) {
        mSizeAdapter.updateItem(item, false);
    }

    public List<SizeListItem> getSizes() {
        return mSizeAdapter.getItems();
    }


    public void addDurations(List<DurationListItem> items) {
        mDurationAdapter = new DurationItemsAdapter(items);
    }

    public void showDurations() {
        if (mDurationAdapter.getItemCount() == 0) {
            showMessage(mActivity.getString(R.string.msg_day_select));
            return;
        }

        txt_title.setText(mActivity.getString(R.string.title_duration_select));
        rv_list.setAdapter(mDurationAdapter);
        show();
    }

    public void setDurationSelected(DurationListItem item) {
        mDurationAdapter.updateItem(item, false);
    }

    public List<DurationListItem> getDurations() {
        return mDurationAdapter.getItems();
    }


    public void showMessage(String message) {
        if (!StringUtils.isEmpty(message)) {
            Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();
        }
    }


    private boolean isDialogShown() {
        return (mDialog != null && mDialog.isShowing());
    }


    private void show() {
        if (mDialog != null && !mDialog.isShowing()) {
            if (mDialog.getWindow() != null) {
//                mDialog.getWindow().setLayout((int) (ScreenUtils.getScreenWidth(mActivity) * 0.9f), (int) (ScreenUtils.getScreenHeight(mActivity) * 0.6f));
                mDialog.getWindow().setLayout((int) (ScreenUtils.getScreenWidth(mActivity) * 0.9f), RelativeLayout.LayoutParams.WRAP_CONTENT);
            }

            mDialog.show();
        }
    }

    public void dismiss() {
        if (isDialogShown()) {
            mDialog.dismiss();
        }
    }


    public void onDestroy() {
        if (mDialog != null) {
            dismiss();

            mGenericItemsAdapter = null;

            mDialog = null;
            mActivity = null;
        }
    }
}
