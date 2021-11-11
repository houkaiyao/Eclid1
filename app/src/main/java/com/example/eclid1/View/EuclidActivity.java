package com.example.eclid1.View;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.eclid1.Bean.Constant;
import com.example.eclid1.Bean.Info;
import com.example.eclid1.Bean.LostBean;
import com.example.eclid1.Bean.EuclidState;
import com.example.eclid1.MainActivity;
import com.example.eclid1.R;
import com.example.eclid1.Utlis.Utlis;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.nhaarman.listviewanimations.appearance.ViewAnimator;
import com.nhaarman.listviewanimations.appearance.simple.SwingLeftInAnimationAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

//  最后一次修改：2021/09/24
//  create by hky
public class EuclidActivity extends Activity implements View.OnClickListener, Constant {
    private static EuclidActivity euclidActivity;
    protected com.mingle.widget.LoadingView loadingView;
    protected RelativeLayout mWrapper;
    protected ListView mListView;
    protected FrameLayout mToolbar;
    protected RelativeLayout mToolbarProfile;
    protected LinearLayout mProfileDetails;
    protected TextView mTextViewProfileName;
    protected TextView mTextViewProfileDescription;
    protected TextView mTellPhone;
    protected TextView mDataTime;
    protected View mButtonProfile;
    List<Map<String, Object>> profilesList = new ArrayList<>();
    public static ShapeDrawable sOverlayShape;
    static int sScreenWidth;
    static int sProfileImageHeight;
    private com.google.android.material.floatingactionbutton.FloatingActionButton add_fab;
    private SwingLeftInAnimationAdapter mListViewAnimationAdapter;
    private ViewAnimator mListViewAnimator;
    private String PostUrl;
    private View mOverlayListItemView;
    private EuclidState mState = EuclidState.Closed;
    private ImageView mtoolbar_profile_back;
    private float mInitialProfileButtonX;
    private TextView euclid_title;
    private AnimatorSet mOpenProfileAnimatorSet;
    private AnimatorSet mCloseProfileAnimatorSet;
    private Animation mProfileButtonShowAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utlis.initState(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_euclid);
        init();
        VollerHepler();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_profile_back:
                animateCloseProfileDetails();
                break;
            case R.id.button_profile:
                String phonenum = (String) mTellPhone.getText();
                Utlis.tellPhone(this, phonenum);
                break;
            case R.id.add_fab:
                Intent intent = new Intent(this, NewUploadActivity.class);
                startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
        euclidActivity = null;
    }

    private void init() {
        euclid_title = (TextView) findViewById(R.id.euclid_title);
        if (Info.Lost_Or_found == 1)
            euclid_title.setText("招领");
        loadingView = (com.mingle.widget.LoadingView) findViewById(R.id.loadView);
        mWrapper = (RelativeLayout) findViewById(R.id.wrapper);
        mListView = (ListView) findViewById(R.id.list_view);
        mToolbar = (FrameLayout) findViewById(R.id.toolbar_list);
        mToolbarProfile = (RelativeLayout) findViewById(R.id.toolbar_profile);
        mProfileDetails = (LinearLayout) findViewById(R.id.wrapper_profile_details);
        mTextViewProfileName = (TextView) findViewById(R.id.text_view_profile_name);
        mTextViewProfileDescription = (TextView) findViewById(R.id.text_view_profile_description);
        mDataTime = (TextView) findViewById(R.id.datetime);
        mTellPhone = (TextView) findViewById(R.id.tellphone);
        mButtonProfile = findViewById(R.id.button_profile);

        mButtonProfile.setOnClickListener(this);
        mButtonProfile.post(new Runnable() {
            @Override
            public void run() {
                mInitialProfileButtonX = mButtonProfile.getX();
            }
        });
        mtoolbar_profile_back = findViewById(R.id.toolbar_profile_back);
        mtoolbar_profile_back.setOnClickListener(this);
        sScreenWidth = getResources().getDisplayMetrics().widthPixels;
        sProfileImageHeight = getResources().getDimensionPixelSize(R.dimen.height_profile_image);
        sOverlayShape = buildAvatarCircleOverlay();
        mListView.setVisibility(View.GONE);
        add_fab = (com.google.android.material.floatingactionbutton.FloatingActionButton) findViewById(R.id.add_fab);
        add_fab.setOnClickListener(this);
        PostUrl = Utlis.getItemUrl(Info.Lost_Or_found);
    }

    private void VollerHepler() {
        JSONObject jsonObject = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                PostUrl,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Gson gson = new Gson();
                        //Toast.makeText(getApplication(),jsonObject.toString(),Toast.LENGTH_SHORT).show();
                        LostBean lostBean = gson.fromJson(jsonObject.toString(), LostBean.class);
                        List<LostBean.DataBean> dataBeanList = lostBean.getData();
                        Map<String, Object> profileMap;

                        for (int i = 0; i < Integer.parseInt(lostBean.getCount()); i++) {
                            profileMap = new HashMap<>();
                            LostBean.DataBean dataBean = dataBeanList.get(i);

                            profileMap.put(EuclidListAdapter.KEY_AVATAR, dataBean.getImage());
                            profileMap.put(EuclidListAdapter.KEY_NAME, dataBean.getItem());
                            profileMap.put(EuclidListAdapter.KEY_DESCRIPTION_SHORT, Utlis.Remark_SubString(dataBean.getRemark()));
                            profileMap.put(EuclidListAdapter.KEY_DESCRIPTION_FULL,
                                    "丢失人：" + dataBean.getUsername() + "\n" + "详细信息：" + dataBean.getRemark() + "\n地点：" + dataBean.getPlace());
                            profileMap.put(EuclidListAdapter.KEY_TELL_PHONE, "tell:" + dataBean.getPhone());
                            profileMap.put(EuclidListAdapter.KEY_DATA_TIME, dataBean.getDatetime());
                            profilesList.add(profileMap);
                        }
                        initList();
                        try {
                            mListView.setVisibility(View.VISIBLE);
                            loadingView.setVisibility(View.GONE);
                        } catch (Exception e) {


                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), volleyError.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(jsonObjectRequest);

    }

    private void initList() {

        //mListViewAnimationAdapter = new SwingLeftInAnimationAdapter(new EuclidListAdapter(this,R.layout.list_item,Utlis.Info_Map()));
        mListViewAnimationAdapter = new SwingLeftInAnimationAdapter(new EuclidListAdapter(this, R.layout.list_item, profilesList));
        mListViewAnimationAdapter.setAbsListView(mListView);
        mListViewAnimator = mListViewAnimationAdapter.getViewAnimator();
        if (mListViewAnimator != null) {
            mListViewAnimator.setAnimationDurationMillis(getAnimationDurationCloseProfileDetails());
            mListViewAnimator.disableAnimations();
        }
        mListView.setAdapter(mListViewAnimationAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mState = EuclidState.Opening;
                showProfileDetails((Map<String, Object>) parent.getItemAtPosition(position), view);
            }
        });
    }

    /**
     * This method counts delay before profile toolbar and profile details start their transition
     * animations, depending on clicked list item on-screen position.
     *
     * @param item - data from adapter, that will be set into overlay view.
     * @param view - clicked view.
     */
    private void showProfileDetails(Map<String, Object> item, final View view) {
        mListView.setEnabled(false);

        int profileDetailsAnimationDelay = getMaxDelayShowDetailsAnimation() * Math.abs(view.getTop())
                / sScreenWidth;

        addOverlayListItem(item, view);
        startRevealAnimation(profileDetailsAnimationDelay);
        animateOpenProfileDetails(profileDetailsAnimationDelay);
    }

    /**
     * This method inflates a clone of clicked view directly above it. Sets data into it.
     *
     * @param item - data from adapter, that will be set into overlay view.
     * @param view - clicked view.
     */
    private void addOverlayListItem(Map<String, Object> item, View view) {
        if (mOverlayListItemView == null) {
            mOverlayListItemView = getLayoutInflater().inflate(R.layout.overlay_list_item, mWrapper, false);
        } else {
            mWrapper.removeView(mOverlayListItemView);
        }

        mOverlayListItemView.findViewById(R.id.view_avatar_overlay).setBackground(sOverlayShape);
//由 Picasso框架更换为 Glide
 /*       Picasso.with(EuclidActivity.this).load((String) item.get(EuclidListAdapter.KEY_AVATAR))
                .resize(sScreenWidth, sProfileImageHeight).centerCrop()
                .placeholder(R.color.blue)
                .into((ImageView) mOverlayListItemView.findViewById(R.id.image_view_reveal_avatar));
        Picasso.with(EuclidActivity.this).load((String) item.get(EuclidListAdapter.KEY_AVATAR))
                .resize(sScreenWidth, sProfileImageHeight).centerCrop()
                .placeholder(R.color.blue)
                .into((ImageView) mOverlayListItemView.findViewById(R.id.image_view_avatar));

  */
        Glide.with(EuclidActivity.this).load((String) item.get(EuclidListAdapter.KEY_AVATAR))
                .override(sScreenWidth, sProfileImageHeight).centerCrop()
                .placeholder(R.color.blue)
                .into((ImageView) mOverlayListItemView.findViewById(R.id.image_view_reveal_avatar));
        Glide.with(EuclidActivity.this).load((String) item.get(EuclidListAdapter.KEY_AVATAR))
                .override(sScreenWidth, sProfileImageHeight).centerCrop()
                .placeholder(R.color.blue)
                .into((ImageView) mOverlayListItemView.findViewById(R.id.image_view_avatar));
        ((TextView) mOverlayListItemView.findViewById(R.id.text_view_name)).setText((String) item.get(EuclidListAdapter.KEY_NAME));
        ((TextView) mOverlayListItemView.findViewById(R.id.text_view_description)).setText((String) item.get(EuclidListAdapter.KEY_DESCRIPTION_SHORT));
        setProfileDetailsInfo(item);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = view.getTop() + mToolbar.getHeight();
        params.bottomMargin = -(view.getBottom() - mListView.getHeight());
        mWrapper.addView(mOverlayListItemView, params);
        mToolbar.bringToFront();
    }

    /**
     * This method sets data of the clicked list item to profile details view.
     *
     * @param item - data from adapter, that will be set into overlay view.
     */
    private void setProfileDetailsInfo(Map<String, Object> item) {
        mTextViewProfileName.setText((String) item.get(EuclidListAdapter.KEY_NAME));
        mTextViewProfileDescription.setText((String) item.get(EuclidListAdapter.KEY_DESCRIPTION_FULL));
        mTellPhone.setText((String) item.get(EuclidListAdapter.KEY_TELL_PHONE));
        mDataTime.setText((String) item.get(EuclidListAdapter.KEY_DATA_TIME));
    }

    /**
     * This method starts circle reveal animation on list item overlay view, to show full-sized
     * avatar image underneath it. And starts transition animation to position clicked list item
     * under the toolbar.
     *
     * @param profileDetailsAnimationDelay - delay before profile toolbar and profile details start their transition
     *                                     animations.
     */
    private void startRevealAnimation(final int profileDetailsAnimationDelay) {
        mOverlayListItemView.post(new Runnable() {
            @Override
            public void run() {
                getAvatarRevealAnimator().start();
                getAvatarShowAnimator(profileDetailsAnimationDelay).start();
            }
        });
    }

    /**
     * This method creates and setups circle reveal animation on list item overlay view.
     * 此方法创建和设置圆显示列表项目叠加视图上的动画。
     *
     * @return - animator object that starts circle reveal animation.
     * 开始圆的动画对象显示动画
     */
    private SupportAnimator getAvatarRevealAnimator() {
        final LinearLayout mWrapperListItemReveal = (LinearLayout) mOverlayListItemView.findViewById(R.id.wrapper_list_item_reveal);

        int finalRadius = Math.max(mOverlayListItemView.getWidth(), mOverlayListItemView.getHeight());

        final SupportAnimator mRevealAnimator = ViewAnimationUtils.createCircularReveal(
                mWrapperListItemReveal,
                sScreenWidth / 2,
                sProfileImageHeight / 2,
                dpToPx(getCircleRadiusDp() * 2),
                finalRadius);
        mRevealAnimator.setDuration(getRevealAnimationDuration());
        mRevealAnimator.addListener(new SupportAnimator.AnimatorListener() {
            @Override
            public void onAnimationStart() {
                mWrapperListItemReveal.setVisibility(View.VISIBLE);
                mOverlayListItemView.setX(0);
            }

            @Override
            public void onAnimationEnd() {

            }

            @Override
            public void onAnimationCancel() {

            }

            @Override
            public void onAnimationRepeat() {

            }
        });
        return mRevealAnimator;
    }

    /**
     * This method creates transition animation to move clicked list item under the toolbar.
     *
     * @param profileDetailsAnimationDelay - delay before profile toolbar and profile details start their transition
     *                                     animations.
     * @return - animator object that starts transition animation.
     */
    private Animator getAvatarShowAnimator(int profileDetailsAnimationDelay) {
        final Animator mAvatarShowAnimator = ObjectAnimator.ofFloat(mOverlayListItemView, View.Y, mOverlayListItemView.getTop(), mToolbarProfile.getBottom());
        mAvatarShowAnimator.setDuration(profileDetailsAnimationDelay + getAnimationDurationShowProfileDetails());
        mAvatarShowAnimator.setInterpolator(new DecelerateInterpolator());
        return mAvatarShowAnimator;
    }

    /**
     * This method starts set of transition animations, which show profile toolbar and profile
     * details views, right after the passed delay.
     *
     * @param profileDetailsAnimationDelay - delay before profile toolbar and profile details
     *                                     start their transition animations.
     */
    private void animateOpenProfileDetails(int profileDetailsAnimationDelay) {
        createOpenProfileButtonAnimation();
        getOpenProfileAnimatorSet(profileDetailsAnimationDelay).start();
    }

    /**
     * This method creates if needed the set of transition animations, which show profile toolbar and profile
     * details views, right after the passed delay.
     *
     * @param profileDetailsAnimationDelay- delay before profile toolbar and profile details
     *                                      start their transition animations.
     * @return - animator set that starts transition animations.
     */
    private AnimatorSet getOpenProfileAnimatorSet(int profileDetailsAnimationDelay) {
        if (mOpenProfileAnimatorSet == null) {
            List<Animator> profileAnimators = new ArrayList<>();
            profileAnimators.add(getOpenProfileToolbarAnimator());
            profileAnimators.add(getOpenProfileDetailsAnimator());

            mOpenProfileAnimatorSet = new AnimatorSet();
            mOpenProfileAnimatorSet.playTogether(profileAnimators);
            mOpenProfileAnimatorSet.setDuration(getAnimationDurationShowProfileDetails());
        }
        mOpenProfileAnimatorSet.setStartDelay(profileDetailsAnimationDelay);
        mOpenProfileAnimatorSet.setInterpolator(new DecelerateInterpolator());
        return mOpenProfileAnimatorSet;
    }

    /**
     * This method, if needed, creates and setups animation of scaling button from 0 to 1.
     */
    private void createOpenProfileButtonAnimation() {
        if (mProfileButtonShowAnimation == null) {
            mProfileButtonShowAnimation = AnimationUtils.loadAnimation(this, R.anim.profile_button_scale);
            mProfileButtonShowAnimation.setDuration(getAnimationDurationShowProfileButton());
            mProfileButtonShowAnimation.setInterpolator(new AccelerateInterpolator());
            mProfileButtonShowAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    mButtonProfile.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    /**
     * This method creates and setups animator which shows profile toolbar.
     *
     * @return - animator object.
     */
    private Animator getOpenProfileToolbarAnimator() {
        Animator mOpenProfileToolbarAnimator = ObjectAnimator.ofFloat(mToolbarProfile, View.Y, -mToolbarProfile.getHeight(), 0);
        mOpenProfileToolbarAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mToolbarProfile.setX(0);
                mToolbarProfile.bringToFront();
                mToolbarProfile.setVisibility(View.VISIBLE);
                mProfileDetails.setX(0);
                mProfileDetails.bringToFront();
                mProfileDetails.setVisibility(View.VISIBLE);

                mButtonProfile.setX(mInitialProfileButtonX);
                mButtonProfile.bringToFront();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mButtonProfile.startAnimation(mProfileButtonShowAnimation);

                mState = EuclidState.Opened;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        return mOpenProfileToolbarAnimator;
    }

    /**
     * This method creates animator which shows profile details.
     *
     * @return - animator object.
     */
    private Animator getOpenProfileDetailsAnimator() {
        Animator mOpenProfileDetailsAnimator = ObjectAnimator.ofFloat(mProfileDetails, View.Y,
                getResources().getDisplayMetrics().heightPixels,
                getResources().getDimensionPixelSize(R.dimen.height_profile_picture_with_toolbar));
        return mOpenProfileDetailsAnimator;
    }

    /**
     * This method starts set of transition animations, which hides profile toolbar, profile avatar
     * and profile details views.
     */
    private void animateCloseProfileDetails() {
        mState = EuclidState.Closing;
        getCloseProfileAnimatorSet().start();
    }

    /**
     * This method creates if needed the set of transition animations, which hides profile toolbar, profile avatar
     * and profile details views. Also it calls notifyDataSetChanged() on the ListView's adapter,
     * so it starts slide-in left animation on list items.
     *
     * @return - animator set that starts transition animations.
     */
    private AnimatorSet getCloseProfileAnimatorSet() {
        if (mCloseProfileAnimatorSet == null) {
            Animator profileToolbarAnimator = ObjectAnimator.ofFloat(mToolbarProfile, View.X,
                    0, mToolbarProfile.getWidth());

            Animator profilePhotoAnimator = ObjectAnimator.ofFloat(mOverlayListItemView, View.X,
                    0, mOverlayListItemView.getWidth());
            profilePhotoAnimator.setStartDelay(getStepDelayHideDetailsAnimation());

            Animator profileButtonAnimator = ObjectAnimator.ofFloat(mButtonProfile, View.X,
                    mInitialProfileButtonX, mOverlayListItemView.getWidth() + mInitialProfileButtonX);
            profileButtonAnimator.setStartDelay(getStepDelayHideDetailsAnimation() * 2);

            Animator profileDetailsAnimator = ObjectAnimator.ofFloat(mProfileDetails, View.X,
                    0, mToolbarProfile.getWidth());
            profileDetailsAnimator.setStartDelay(getStepDelayHideDetailsAnimation() * 2);

            List<Animator> profileAnimators = new ArrayList<>();
            profileAnimators.add(profileToolbarAnimator);
            profileAnimators.add(profilePhotoAnimator);
            profileAnimators.add(profileButtonAnimator);
            profileAnimators.add(profileDetailsAnimator);

            mCloseProfileAnimatorSet = new AnimatorSet();
            mCloseProfileAnimatorSet.playTogether(profileAnimators);
            mCloseProfileAnimatorSet.setDuration(getAnimationDurationCloseProfileDetails());
            mCloseProfileAnimatorSet.setInterpolator(new AccelerateInterpolator());
            mCloseProfileAnimatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    if (mListViewAnimator != null) {
                        mListViewAnimator.reset();
                        mListViewAnimationAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mToolbarProfile.setVisibility(View.INVISIBLE);
                    mButtonProfile.setVisibility(View.INVISIBLE);
                    mProfileDetails.setVisibility(View.INVISIBLE);

                    mListView.setEnabled(true);
                    mListViewAnimator.disableAnimations();

                    mState = EuclidState.Closed;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        return mCloseProfileAnimatorSet;
    }

    /**
     * This method creates a view with empty/transparent circle in it's center. This view is used
     * to cover the profile avatar.
     *
     * @return - ShapeDrawable object.
     */
    private ShapeDrawable buildAvatarCircleOverlay() {
        int radius = 666;
        ShapeDrawable overlay = new ShapeDrawable(new RoundRectShape(null,
                new RectF(
                        sScreenWidth / 2 - dpToPx(getCircleRadiusDp() * 2),
                        sProfileImageHeight / 2 - dpToPx(getCircleRadiusDp() * 2),
                        sScreenWidth / 2 - dpToPx(getCircleRadiusDp() * 2),
                        sProfileImageHeight / 2 - dpToPx(getCircleRadiusDp() * 2)),
                new float[]{radius, radius, radius, radius, radius, radius, radius, radius}));
        overlay.getPaint().setColor(getResources().getColor(R.color.gray));

        return overlay;
    }

    public int dpToPx(int dp) {
        return Math.round((float) dp * getResources().getDisplayMetrics().density);
    }

    @Override
    public void onBackPressed() {
        if (getState() == EuclidState.Opened) {
            animateCloseProfileDetails();
        } else if (getState() == EuclidState.Closed) {
            super.onBackPressed();
        }
    }

    /**
     * To use EuclidActivity class, at least this method must be implemented, with your own data.
     *
     * @return - adapter with data. Check {@link com.example.mylibrary.EuclidListAdapter}
     */

    /**
     * Returns current profile details state.
     *
     * @return - {@link EuclidState}
     */
    public EuclidState getState() {
        return mState;
    }

    /**
     * Duration of circle reveal animation.
     *
     * @return - duration in milliseconds.
     */
    protected int getRevealAnimationDuration() {
        return REVEAL_ANIMATION_DURATION;
    }

    /**
     * Maximum delay between list item click and start of profile toolbar and profile details
     * transition animations. If clicked list item was positioned right at the top - we start
     * profile toolbar and profile details transition animations immediately, otherwise increase
     * start delay up to this value.
     *
     * @return - duration in milliseconds.
     */
    protected int getMaxDelayShowDetailsAnimation() {
        return MAX_DELAY_SHOW_DETAILS_ANIMATION;
    }

    /**
     * Duration of profile toolbar and profile details transition animations.
     *
     * @return - duration in milliseconds.
     */
    protected int getAnimationDurationShowProfileDetails() {
        return ANIMATION_DURATION_SHOW_PROFILE_DETAILS;
    }

    /**
     * Duration of delay between profile toolbar, profile avatar and profile details close animations.
     *
     * @return - duration in milliseconds.
     */
    protected int getStepDelayHideDetailsAnimation() {
        return STEP_DELAY_HIDE_DETAILS_ANIMATION;
    }

    /**
     * Duration of profile details close animation.
     *
     * @return - duration in milliseconds.
     */
    protected int getAnimationDurationCloseProfileDetails() {
        return ANIMATION_DURATION_CLOSE_PROFILE_DETAILS;
    }

    protected int getAnimationDurationShowProfileButton() {
        return ANIMATION_DURATION_SHOW_PROFILE_BUTTON;
    }

    /**
     * Radius of empty circle inside the avatar overlay.
     *
     * @return - size dp.
     */
    protected int getCircleRadiusDp() {
        return CIRCLE_RADIUS_DP;
    }

}