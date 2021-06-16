package com.malaab.ya.action.actionyamalaab.ui.search.search_size;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.annotations.ItemAction;
import com.malaab.ya.action.actionyamalaab.annotations.SearchOption;
import com.malaab.ya.action.actionyamalaab.custom.DialogList;
import com.malaab.ya.action.actionyamalaab.data.model.GenericListItem;
import com.malaab.ya.action.actionyamalaab.di.component.ActivityComponent;
import com.malaab.ya.action.actionyamalaab.events.OnEventItemClicked;
import com.malaab.ya.action.actionyamalaab.events.OnEventSearch;
import com.malaab.ya.action.actionyamalaab.ui.base.BaseFragment;
import com.malaab.ya.action.actionyamalaab.ui.search.SearchActivity;
import com.malaab.ya.action.actionyamalaab.utils.Constants;
import com.yayandroid.theactivitymanager.TheActivityManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SearchSizeFragment extends BaseFragment {

    @BindView(R.id.txt_size)
    TextView txt_size;

    @BindView(R.id.btn_search)
    Button btn_search;

    DialogList mDialogList;

    private boolean isFragmentVisible = false;
    private int size;


    public SearchSizeFragment() {
        // Required empty public constructor
    }


    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);

        isFragmentVisible = visible;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_seach_size, container, false);

        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, rootView));
        }

        return rootView;
    }


    @Override
    protected void initUI() {
        mDialogList = new DialogList().with(getActivity()); /* because we using dialogList in another fragment
                                                               this dialog is defined under those fragment activity,
                                                               so wil have a conflict if we Inject it in multiple fragments under same activity */
        mDialogList.build();

        List<GenericListItem> list = new ArrayList<>();
        list.add(new GenericListItem("5 x 5", String.valueOf(Constants.SIZE_10)));
        list.add(new GenericListItem("6 x 6", String.valueOf(Constants.SIZE_12)));
        list.add(new GenericListItem("7 x 7", String.valueOf(Constants.SIZE_14)));
        list.add(new GenericListItem("8 x 8", String.valueOf(Constants.SIZE_16)));
        list.add(new GenericListItem("9 x 9", String.valueOf(Constants.SIZE_18)));
        list.add(new GenericListItem("10 x 10", String.valueOf(Constants.SIZE_20)));
        mDialogList.addItems(list);
    }


    @OnClick(R.id.ln_size)
    public void selectAge() {
        mDialogList
                .withTitle(getString(R.string.title_playground_size))
                .showItems();
    }

    @OnClick(R.id.btn_search)
    public void search() {
        EventBus.getDefault().post(new OnEventSearch(SearchOption.SIZE, 0, 0, "", "", "", 0, 0, size));
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEventItemClicked(OnEventItemClicked event) {
        if (TheActivityManager.getInstance().getCurrentActivity() instanceof SearchActivity
                && isFragmentVisible) {

            if (event.getItem() instanceof GenericListItem && event.getAction() == ItemAction.PICK) {
                size = Integer.valueOf(((GenericListItem) event.getItem()).value);
                txt_size.setText(((GenericListItem) event.getItem()).name);
            }

            mDialogList.dismiss();
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);
        mDialogList.dismiss();
    }

    @Override
    public void onDestroyView() {
        mDialogList.onDestroy();
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}