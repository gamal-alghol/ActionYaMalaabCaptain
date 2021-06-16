package com.malaab.ya.action.actionyamalaab.ui.search.search_price;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.appyvet.materialrangebar.RangeBar;
import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.annotations.SearchOption;
import com.malaab.ya.action.actionyamalaab.di.component.ActivityComponent;
import com.malaab.ya.action.actionyamalaab.events.OnEventSearch;
import com.malaab.ya.action.actionyamalaab.ui.base.BaseFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SearchPriceFragment extends BaseFragment implements RangeBar.OnRangeBarChangeListener {

    @BindView(R.id.mRangeBar_price)
    RangeBar mRangeBar_price;
    @BindView(R.id.txt_min)
    TextView txt_min;
    @BindView(R.id.txt_max)
    TextView txt_max;

    @BindView(R.id.btn_search)
    Button btn_search;

    boolean isFragmentVisible = true;
    private float priceMinValue, priceMaxValue;


    public SearchPriceFragment() {
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

        View rootView = inflater.inflate(R.layout.fragment_seach_price, container, false);

        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, rootView));
        }

        return rootView;
    }


    @Override
    protected void initUI() {
        mRangeBar_price.setOnRangeBarChangeListener(this);
        mRangeBar_price.setRangePinsByValue(0, 1000);
    }


    @OnClick(R.id.btn_search)
    public void search() {
        EventBus.getDefault().post(new OnEventSearch(SearchOption.PRICE, priceMinValue, priceMaxValue, "", "", "", 0, 0, 0));
    }


    @Override
    public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
        priceMinValue = Float.valueOf(leftPinValue);
        priceMaxValue = Float.valueOf(rightPinValue);

        txt_min.setText(String.format(Locale.ENGLISH, "%.0f ر.س", priceMinValue));
        txt_max.setText(String.format(Locale.ENGLISH, "%.0f ر.س", priceMaxValue));
    }


    @Override
    public void onStart() {
        super.onStart();

//        if (!EventBus.getDefault().isRegistered(this))
//            EventBus.getDefault().register(this);
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

//        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}