package net.jejer.hipda.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Toast;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import net.jejer.hipda.R;
import net.jejer.hipda.async.LoginHelper;
import net.jejer.hipda.async.SimpleListLoader;
import net.jejer.hipda.async.UpdateHelper;
import net.jejer.hipda.async.VolleyHelper;
import net.jejer.hipda.bean.HiSettingsHelper;
import net.jejer.hipda.glide.GlideHelper;
import net.jejer.hipda.utils.ACRAUtils;
import net.jejer.hipda.utils.Logger;

public class MainFrameActivity extends AppCompatActivity {

    //    private DrawerLayout mDrawerLayout;
//    private ListView mDrawerList;
    private boolean mEnableSwipe = false;
    //    private OnSwipeTouchListener mSwipeListener;
    private Fragment mOnSwipeCallback = null;
    private int mQuit = 0;

    public Drawer.Result result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.v("onCreate");

        ACRAUtils.init(this);

        // Init Settings
        HiSettingsHelper.getInstance().init(this);
//        if (HiSettingsHelper.getInstance().isEinkModeUIEnabled()) {
//            setTheme(R.style.ThemeEink);
//        } else if (HiSettingsHelper.getInstance().isNightTheme()) {
//            setTheme(R.style.ThemeNight);
//        }

        GlideHelper.init(this);

        // Init Volley
        VolleyHelper.getInstance().init(this);
        NotifyHelper.getInstance().init(this);

        super.onCreate(savedInstanceState);

        if (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT == HiSettingsHelper.getInstance().getScreenOrietation()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE == HiSettingsHelper.getInstance().getScreenOrietation()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
        }

        setContentView(R.layout.activity_main_frame);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
//        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        result = new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
//                .withHeader(R.layout.header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.title_drawer_search).withIdentifier(DrawerItem.SEARCH.id).withIcon(GoogleMaterial.Icon.gmd_search),
                        new PrimaryDrawerItem().withName(R.string.title_drawer_mypost).withIdentifier(DrawerItem.MY_POST.id).withIcon(GoogleMaterial.Icon.gmd_grade),
//                        new SectionDrawerItem().withName(R.string.category_section_categories),
                        new PrimaryDrawerItem().withName(R.string.title_drawer_myreply).withIdentifier(DrawerItem.MY_REPLY.id).withIcon(GoogleMaterial.Icon.gmd_location_city),
                        new PrimaryDrawerItem().withName(R.string.title_drawer_favorites).withIdentifier(DrawerItem.MY_FAVORITES.id).withIcon(GoogleMaterial.Icon.gmd_favorite),
                        new PrimaryDrawerItem().withName(R.string.title_drawer_sms).withIdentifier(DrawerItem.SMS.id).withIcon(GoogleMaterial.Icon.gmd_local_florist),
                        new PrimaryDrawerItem().withName(R.string.title_drawer_notify).withIdentifier(DrawerItem.THREAD_NOTIFY.id).withIcon(GoogleMaterial.Icon.gmd_style),
                        new PrimaryDrawerItem().withName(R.string.title_drawer_setting).withIdentifier(DrawerItem.SETTINGS.id).withIcon(GoogleMaterial.Icon.gmd_person)
                )
                .withOnDrawerItemClickListener(new DrawerItemClickListener())
                .build();

        // Prepare Drawer
//        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        mDrawerList = (ListView) findViewById(R.id.left_drawer);
//        String[] drwerListTitle = getResources().getStringArray(R.array.left_menu);
//        DrawerAdapter adapter = new DrawerAdapter(this, R.layout.item_drawer, drwerListTitle);
//        mDrawerList.setAdapter(adapter);
//        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
//        getSupportActionBar().setHomeButtonEnabled(true);


        // Prepare gesture detector
//        mSwipeListener = new OnSwipeTouchListener(this) {
//            public void onSwipeRight() {
//                if (HiSettingsHelper.getInstance().isGestureBack()
//                        && !HiSettingsHelper.getInstance().getIsLandscape()
//                        && !(getFragmentManager().findFragmentByTag(PostFragment.class.getName()) instanceof PostFragment)) {
//                    popFragment(false);
//                }
//            }
//
//            public void onSwipeTop() {
//                if (mOnSwipeCallback != null) {
//                    if (mOnSwipeCallback instanceof ThreadDetailFragment) {
//                        ((ThreadDetailFragment) mOnSwipeCallback).onSwipeTop();
//                    }
//                }
//            }
//
//            public void onSwipeBottom() {
//                if (mOnSwipeCallback != null) {
//                    if (mOnSwipeCallback instanceof ThreadDetailFragment) {
//                        ((ThreadDetailFragment) mOnSwipeCallback).onSwipeBottom();
//                    }
//                }
//            }
//        };
//        findViewById(R.id.main_frame_container).setOnTouchListener(mSwipeListener);

//        if (findViewById(R.id.thread_detail_container_in_main) != null) {
//            HiSettingsHelper.getInstance().setIsLandscape(true);
//        } else {
//            HiSettingsHelper.getInstance().setIsLandscape(false);
//        }

        // Prepare Fragments
        getFragmentManager().addOnBackStackChangedListener(new BackStackChangedListener());
        int lastForumId = HiSettingsHelper.getInstance().getLastForumId();
        ThreadListFragment threadListFragment = new ThreadListFragment();
        Bundle argments = new Bundle();
        if (lastForumId > 0) {
            argments.putInt(ThreadListFragment.ARG_FID_KEY, lastForumId);
            threadListFragment.setArguments(argments);
        }

        getFragmentManager().beginTransaction()
                .replace(R.id.main_frame_container, threadListFragment, ThreadListFragment.class.getName())
                .commit();

        if (LoginHelper.isLoggedIn()) {
            if (HiSettingsHelper.getInstance().isUpdateCheckable()) {
                new UpdateHelper(this, true).check();
            }
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main_frame, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Logger.v("onOptionsItemSelected");
        switch (item.getItemId()) {
            case android.R.id.home:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                popFragment(false);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (mOnSwipeCallback instanceof ThreadDetailFragment) {
            if (((ThreadDetailFragment) mOnSwipeCallback).hideQuickReply())
                return;
        }

        Fragment postFragment = getFragmentManager().findFragmentByTag(PostFragment.class.getName());
        if (postFragment instanceof PostFragment && ((PostFragment) postFragment).isUserInputted()) {
            Dialog dialog = new AlertDialog.Builder(this)
                    .setTitle("放弃发表？")
                    .setMessage("\n确认放弃已输入的内容吗？\n")
                    .setPositiveButton(getResources().getString(android.R.string.ok),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    popFragment(true);
                                }
                            })
                    .setNegativeButton(getResources().getString(android.R.string.cancel),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).create();
            dialog.show();
        } else {
            if (!popFragment(true)) {
                mQuit++;
                if (mQuit == 1 && HiSettingsHelper.getInstance().getIsLandscape()) {
                    Toast.makeText(this, "再按一次退出HiPDA", Toast.LENGTH_LONG).show();
                } else {
                    finish();
                }
            }
        }

    }

    public boolean popFragment(boolean backPressed) {
        Logger.v("popFragment");
        FragmentManager fm = getFragmentManager();
        int count = fm.getBackStackEntryCount();
        Logger.v("before pop, count=" + count);
        if (count > 0) {
            fm.popBackStackImmediate();
            count = fm.getBackStackEntryCount();
            Logger.v("after pop, count=" + count);
            if (count > 0) {
                FragmentManager.BackStackEntry backEntry = getFragmentManager().getBackStackEntryAt(count - 1);
                String str = backEntry.getName();
                Logger.v("after pop, name=" + str);
                Fragment fragment = getFragmentManager().findFragmentByTag(str);

                if (fragment != null) {
                    fragment.setHasOptionsMenu(true);
                }
            } else {
                Fragment fg = fm.findFragmentById(R.id.main_frame_container);
                if (fg != null) {
                    fg.setHasOptionsMenu(true);
                }
            }
            return true;
        } else {
            if (!backPressed) {
//                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
//                if (!mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
//                    mDrawerLayout.openDrawer(Gravity.LEFT);
//                } else {
//                    mDrawerLayout.closeDrawers();
//                }
            }
            return false;
        }

    }

    public void onNotification() {
        if (mOnSwipeCallback instanceof ThreadListFragment) {
            ((ThreadListFragment) mOnSwipeCallback).showNotification();
        }
    }

    public enum DrawerItem {
        SEARCH(1),
        MY_POST(2),
        MY_REPLY(3),
        MY_FAVORITES(4),
        SMS(5),
        THREAD_NOTIFY(6),
        SETTINGS(7);

        public final int id;

        private DrawerItem(int id) {
            this.id = id;
        }
    }

    private class DrawerItemClickListener implements Drawer.OnDrawerItemClickListener {

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            switch (position) {
                case 0:    // forum
                    getFragmentManager().beginTransaction()
                            .replace(R.id.main_frame_container, new ThreadListFragment(), ThreadListFragment.class.getName())
                            .commit();
                    break;
                case 1:    // search
                    Bundle searchBundle = new Bundle();
                    searchBundle.putInt(SimpleListFragment.ARG_TYPE, SimpleListLoader.TYPE_SEARCH);
                    SimpleListFragment searchFragment = new SimpleListFragment();
                    searchFragment.setArguments(searchBundle);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.main_frame_container, searchFragment, SimpleListFragment.class.getName())
                            .addToBackStack(SimpleListFragment.class.getName())
                            .commit();
                    break;
                case 2:    // my posts
                    Bundle postsBundle = new Bundle();
                    postsBundle.putInt(SimpleListFragment.ARG_TYPE, SimpleListLoader.TYPE_MYPOST);
                    SimpleListFragment postsFragment = new SimpleListFragment();
                    postsFragment.setArguments(postsBundle);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.main_frame_container, postsFragment, SimpleListFragment.class.getName())
                            .addToBackStack(SimpleListFragment.class.getName())
                            .commit();
                    break;
                case 3:    // my reply
                    Bundle replyBundle = new Bundle();
                    replyBundle.putInt(SimpleListFragment.ARG_TYPE, SimpleListLoader.TYPE_MYREPLY);
                    SimpleListFragment replyFragment = new SimpleListFragment();
                    replyFragment.setArguments(replyBundle);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.main_frame_container, replyFragment, SimpleListFragment.class.getName())
                            .addToBackStack(SimpleListFragment.class.getName())
                            .commit();
                    break;
                case 4:    // my favorites
                    Bundle favBundle = new Bundle();
                    favBundle.putInt(SimpleListFragment.ARG_TYPE, SimpleListLoader.TYPE_FAVORITES);
                    SimpleListFragment favFragment = new SimpleListFragment();
                    favFragment.setArguments(favBundle);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.main_frame_container, favFragment, SimpleListFragment.class.getName())
                            .addToBackStack(SimpleListFragment.class.getName())
                            .commit();
                    break;
                case 5:    // sms
                    Bundle smsBundle = new Bundle();
                    smsBundle.putInt(SimpleListFragment.ARG_TYPE, SimpleListLoader.TYPE_SMS);
                    SimpleListFragment smsFragment = new SimpleListFragment();
                    smsFragment.setArguments(smsBundle);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.main_frame_container, smsFragment, SimpleListFragment.class.getName())
                            .addToBackStack(SimpleListFragment.class.getName())
                            .commit();
                    break;
                case 6:    // thread notify
                    Bundle notifyBundle = new Bundle();
                    notifyBundle.putInt(SimpleListFragment.ARG_TYPE, SimpleListLoader.TYPE_THREADNOTIFY);
                    SimpleListFragment notifyFragment = new SimpleListFragment();
                    notifyFragment.setArguments(notifyBundle);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.main_frame_container, notifyFragment, SimpleListFragment.class.getName())
                            .addToBackStack(SimpleListFragment.class.getName())
                            .commit();
                    break;
                case 7:    // settings
                    getFragmentManager().beginTransaction()
                            .replace(R.id.main_frame_container, new SettingsFragment(), SettingsFragment.class.getName())
                            .addToBackStack(SettingsFragment.class.getName())
                            .commit();
                    break;
                case 8:    // switch day/night theme
                    //cancel eink mode ui
                    HiSettingsHelper.getInstance().setNightTheme(!HiSettingsHelper.getInstance().isNightTheme());
                    Intent intent = getIntent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();
                    startActivity(intent);
                    break;
                default:
                    break;
            }

//            mDrawerLayout.closeDrawer(mDrawerList);
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {
            switch (iDrawerItem.getIdentifier()) {
                case 1:    // search
                    Bundle searchBundle = new Bundle();
                    searchBundle.putInt(SimpleListFragment.ARG_TYPE, SimpleListLoader.TYPE_SEARCH);
                    SimpleListFragment searchFragment = new SimpleListFragment();
                    searchFragment.setArguments(searchBundle);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.main_frame_container, searchFragment, SimpleListFragment.class.getName())
                            .addToBackStack(SimpleListFragment.class.getName())
                            .commit();
                    break;
                case 2:    // my posts
                    Bundle postsBundle = new Bundle();
                    postsBundle.putInt(SimpleListFragment.ARG_TYPE, SimpleListLoader.TYPE_MYPOST);
                    SimpleListFragment postsFragment = new SimpleListFragment();
                    postsFragment.setArguments(postsBundle);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.main_frame_container, postsFragment, SimpleListFragment.class.getName())
                            .addToBackStack(SimpleListFragment.class.getName())
                            .commit();
                    break;
                case 3:    // my reply
                    Bundle replyBundle = new Bundle();
                    replyBundle.putInt(SimpleListFragment.ARG_TYPE, SimpleListLoader.TYPE_MYREPLY);
                    SimpleListFragment replyFragment = new SimpleListFragment();
                    replyFragment.setArguments(replyBundle);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.main_frame_container, replyFragment, SimpleListFragment.class.getName())
                            .addToBackStack(SimpleListFragment.class.getName())
                            .commit();
                    break;
                case 4:    // my favorites
                    Bundle favBundle = new Bundle();
                    favBundle.putInt(SimpleListFragment.ARG_TYPE, SimpleListLoader.TYPE_FAVORITES);
                    SimpleListFragment favFragment = new SimpleListFragment();
                    favFragment.setArguments(favBundle);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.main_frame_container, favFragment, SimpleListFragment.class.getName())
                            .addToBackStack(SimpleListFragment.class.getName())
                            .commit();
                    break;
                case 5:    // sms
                    Bundle smsBundle = new Bundle();
                    smsBundle.putInt(SimpleListFragment.ARG_TYPE, SimpleListLoader.TYPE_SMS);
                    SimpleListFragment smsFragment = new SimpleListFragment();
                    smsFragment.setArguments(smsBundle);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.main_frame_container, smsFragment, SimpleListFragment.class.getName())
                            .addToBackStack(SimpleListFragment.class.getName())
                            .commit();
                    break;
                case 6:    // thread notify
                    Bundle notifyBundle = new Bundle();
                    notifyBundle.putInt(SimpleListFragment.ARG_TYPE, SimpleListLoader.TYPE_THREADNOTIFY);
                    SimpleListFragment notifyFragment = new SimpleListFragment();
                    notifyFragment.setArguments(notifyBundle);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.main_frame_container, notifyFragment, SimpleListFragment.class.getName())
                            .addToBackStack(SimpleListFragment.class.getName())
                            .commit();
                    break;
                case 7:    // settings
                    getFragmentManager().beginTransaction()
                            .replace(R.id.main_frame_container, new SettingsFragment(), SettingsFragment.class.getName())
                            .addToBackStack(SettingsFragment.class.getName())
                            .commit();
                    break;
                case 8:    // switch day/night theme
                    //cancel eink mode ui
                    HiSettingsHelper.getInstance().setNightTheme(!HiSettingsHelper.getInstance().isNightTheme());
                    Intent intent = getIntent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }

    private class BackStackChangedListener implements FragmentManager.OnBackStackChangedListener {

        @Override
        public void onBackStackChanged() {
            // reset back key press counter
            mQuit = 0;

            // Make sure drawer only showed in top fragment
            // Make sure swipe only worked in second fragment
            FragmentManager fm = getFragmentManager();
            Logger.v("getBackStackEntryCount = " + String.valueOf(fm.getBackStackEntryCount()));
            if (!HiSettingsHelper.getInstance().getIsLandscape()) {
                if (fm.getBackStackEntryCount() > 0) {
//                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    mEnableSwipe = true;
                } else {
//                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    mEnableSwipe = false;
                }
            }
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (mEnableSwipe) {
//            mSwipeListener.onTouch(null, ev);
//        }
        return super.dispatchTouchEvent(ev);
    }

    public void registOnSwipeCallback(Fragment f) {
        mOnSwipeCallback = f;
    }
}
