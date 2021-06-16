package com.malaab.ya.action.actionyamalaab.ui.account.accountdetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageButton;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.annotations.PermissionsCodes;
import com.malaab.ya.action.actionyamalaab.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.ui.base.BaseActivity;
import com.malaab.ya.action.actionyamalaab.utils.ListUtils;
import com.malaab.ya.action.actionyamalaab.utils.PermissionsUtils;
import com.malaab.ya.action.actionyamalaab.utils.ViewUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;


public class AccountDetailsActivity extends BaseActivity implements AccountDetailsMvpView {

    @BindView(R.id.header_txt_title)
    TextView header_txt_title;
    @BindView(R.id.header_btn_back)
    AppCompatImageButton header_btn_back;
    @BindView(R.id.header_btn_edit)
    AppCompatImageButton header_btn_edit;

    @BindView(R.id.sc_view)
    public ScrollView sc_view;

    @BindView(R.id.img_profile)
    public CircleImageView img_profile;
    @BindView(R.id.btn_editProfileImage)
    AppCompatImageButton btn_editProfileImage;

    @BindView(R.id.edt_fName)
    public EditText edt_fName;
    @BindView(R.id.edt_lName)
    public EditText edt_lName;
    @BindView(R.id.edt_phoneNo)
    public EditText edt_phoneNo;
    @BindView(R.id.edt_email)
    public EditText edt_email;
    @BindView(R.id.edt_age)
    public EditText edt_age;
    @BindView(R.id.edt_city)
    public EditText edt_city;

    @BindView(R.id.btn_update)
    public Button btn_update;

    @Inject
    AccountDetailsMvpPresenter<AccountDetailsMvpView> mPresenter;

    private boolean isEditMode;
    private boolean isLoaded;
    private File mProfileImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(this);
    }

    @Override
    protected void initUI() {
        header_btn_back.setVisibility(View.VISIBLE);
        header_txt_title.setText(R.string.title_my_profile);
        header_btn_edit.setVisibility(View.VISIBLE);

        mValidator.addField(edt_fName)
                .addField(edt_lName)
                .addField(edt_phoneNo)
                .addField(edt_email)
                .addField(edt_age)
                .addField(edt_city);
    }


    @OnClick(R.id.header_btn_back)
    public void goBack() {
        onBackPressed();
    }

    @OnClick(R.id.header_btn_edit)
    public void enableEdit() {
        enableEditMode();
    }

    @OnClick(R.id.img_profile)
    public void selectProfileImage() {
        PermissionsUtils.requestPermission(AccountDetailsActivity.this, PermissionsCodes.WRITE_EXTERNAL_STORAGE, Permission.STORAGE);
    }

    @OnClick(R.id.btn_update)
    public void update() {
        if (mValidator.isValid()) {
            mPresenter.updateCurrentUser(mProfileImage, edt_fName.getText().toString(), edt_lName.getText().toString(),
                    edt_phoneNo.getText().toString(), edt_email.getText().toString(),
                    edt_age.getText().toString(), edt_city.getText().toString());
        }
    }


    @PermissionYes(PermissionsCodes.WRITE_EXTERNAL_STORAGE)
    private void gotStoragePermission(@NonNull List<String> grantedPermissions) {
        EasyImage.openChooserWithGallery(this, getString(R.string.title_image_source), 0);
    }

    @PermissionNo(PermissionsCodes.WRITE_EXTERNAL_STORAGE)
    private void noStoragePermission(@NonNull List<String> deniedPermissions) {
        if (AndPermission.hasAlwaysDeniedPermission(this, deniedPermissions)) {
            PermissionsUtils.showAlwaysDeniedCustomDialog(this);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                showMessage(e.getLocalizedMessage());
            }

            @Override
            public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {
                if (!ListUtils.isEmpty(imageFiles)) {

                    mProfileImage = imageFiles.get(0);

                    if (mProfileImage != null) {
                        Glide.with(getApplicationContext())
                                .load(mProfileImage)
                                .error(R.drawable.img_profile_default)
                                .placeholder(R.drawable.img_profile_default)
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .into(img_profile);
                    }
                }
            }

            @SuppressWarnings("ResultOfMethodCallIgnored")
            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                // Cancel handling, you might wanna remove taken photo if it was canceled
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(AccountDetailsActivity.this);
                    if (photoFile != null)
                        photoFile.delete();
                }
            }
        });
    }


    @Override
    public void onGetCurrentUser(User user) {
        Log.d("ttt",user.profileImageUrl);
        Glide.with(this)
                .load(user.profileImageUrl)

                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(img_profile);

        img_profile.setClickable(isEditMode);

        edt_fName.setText(user.fName);
        edt_lName.setText(user.lName);
        edt_phoneNo.setText(user.mobileNo);
        edt_email.setText(user.email);
        edt_city.setText(user.address_city);

        if (user.age > 0) {
            edt_age.setText(String.valueOf(user.age));
        }
    }

    @Override
    public void onUpdateCurrentUserSuccess() {
        enableEditMode();

        ViewUtils.scrollToTop(sc_view);
    }


    private void enableEditMode() {
        isEditMode = !isEditMode;

        img_profile.setClickable(isEditMode);

        enableEditText(edt_fName, isEditMode);
        enableEditText(edt_lName, isEditMode);
        enableEditText(edt_phoneNo, isEditMode);
        enableEditText(edt_email, isEditMode);
        enableEditText(edt_age, isEditMode);
        enableEditText(edt_city, isEditMode);

        if (isEditMode) {
            btn_editProfileImage.setVisibility(View.VISIBLE);
            btn_update.setVisibility(View.VISIBLE);
        } else {
            btn_editProfileImage.setVisibility(View.GONE);
            btn_update.setVisibility(View.INVISIBLE);
        }
    }

    private void enableEditText(EditText editText, boolean isEnabled) {
        editText.setEnabled(isEnabled);
        editText.setClickable(isEnabled);
        editText.setCursorVisible(isEnabled);
        editText.setLongClickable(isEnabled);

        if (isEnabled) {
            editText.setTextAppearance(this, R.style.EditText);
        } else {
            editText.setTextAppearance(this, R.style.EditText_NoBottomLine);
        }

        editText.setTextColor(ContextCompat.getColor(this, R.color.dark_gray));
    }


    @Override
    public void onNoInternetConnection() {
        onError(R.string.error_no_connection);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!isLoaded) {
            isLoaded = true;
            mPresenter.getCurrentUserLocal();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (isEditMode) {
            enableEditMode();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
