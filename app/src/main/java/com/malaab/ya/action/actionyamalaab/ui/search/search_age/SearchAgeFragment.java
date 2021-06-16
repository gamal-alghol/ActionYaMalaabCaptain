package com.malaab.ya.action.actionyamalaab.ui.search.search_age;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.appyvet.materialrangebar.RangeBar;
import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.annotations.ItemAction;
import com.malaab.ya.action.actionyamalaab.annotations.SearchOption;
import com.malaab.ya.action.actionyamalaab.custom.DialogList;
import com.malaab.ya.action.actionyamalaab.data.model.GenericListItem;
import com.malaab.ya.action.actionyamalaab.data.network.model.City;
import com.malaab.ya.action.actionyamalaab.data.network.model.Region;
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
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SearchAgeFragment extends BaseFragment {

    @BindView(R.id.txt_age)
    TextView txt_age;

    @BindView(R.id.btn_search)
    Button btn_search;

    DialogList mDialogList;

    private boolean isFragmentVisible = false;
    private int ageStart, ageEnd;


    public SearchAgeFragment() {
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

        View rootView = inflater.inflate(R.layout.fragment_seach_age, container, false);

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
        list.add(new GenericListItem("8 - 12", String.valueOf(Constants.AGE_8)));
        list.add(new GenericListItem("13 - 17", String.valueOf(Constants.AGE_13)));
        list.add(new GenericListItem("+18", String.valueOf(Constants.AGE_18)));
        mDialogList.addItems(list);
    }


    @OnClick(R.id.ln_age)
    public void selectAge() {
        mDialogList
                .withTitle(getString(R.string.age))
                .showItems();
    }

    @OnClick(R.id.btn_search)
    public void search() {
        EventBus.getDefault().post(new OnEventSearch(SearchOption.AGE, 0, 0, "", "", "", ageStart, ageEnd, 0));
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEventItemClicked(OnEventItemClicked event) {
        if (TheActivityManager.getInstance().getCurrentActivity() instanceof SearchActivity
                && isFragmentVisible) {

            if (event.getItem() instanceof GenericListItem && event.getAction() == ItemAction.PICK) {
                if (((GenericListItem) event.getItem()).value.equals("8")) {
                    ageStart = Constants.AGE_8;
                    ageEnd = Constants.AGE_12;
                } else if (((GenericListItem) event.getItem()).value.equals("13")) {
                    ageStart = Constants.AGE_13;
                    ageEnd = Constants.AGE_17;

                } else if (((GenericListItem) event.getItem()).value.equals("18")) {
                    ageStart = Constants.AGE_18;
                    ageEnd = Constants.AGE_50;

                }

                txt_age.setText(((GenericListItem) event.getItem()).name);
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