package com.example.andrey.firebirds;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Mother";

    private final  String TAB_NAME_1 = "My Collections";
    private final  String TAB_NAME_2 = "Other Collections";
    private final  String TAB_NAME_3 = "My Account";

    @BindView(R.id.container) ViewPager viewPager;
    @BindView(R.id.tabs) TabLayout tabLayout;

    private SectionPageAdapter sectionPageAdapter;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        sectionPageAdapter = new SectionPageAdapter(getSupportFragmentManager());
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        String login = "t@t.ru";
//        String pass = "123456";
//        mAuth = FirebaseAuth.getInstance();
//        mAuth.signInWithEmailAndPassword(login, pass)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            //Log.d(TAG, "onComplete: "+ user.getUid());
//                        } else {
//                            Toast.makeText(getApplicationContext(), "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
    }

    public void setupViewPager(ViewPager viewPager){
        SectionPageAdapter adapter = new SectionPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new TabFirstFragment(), TAB_NAME_1);
        adapter.addFragment(new TabSecondFragment(), TAB_NAME_2);
        adapter.addFragment(new TabAccountFragment(), TAB_NAME_3);
        viewPager.setAdapter(adapter);
    }

    public class SectionPageAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentsList = new ArrayList<>();
        private List<String> fragmentsTitle = new ArrayList<>();

        public SectionPageAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title){
            fragmentsList.add(fragment);
            fragmentsTitle.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentsTitle.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentsList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentsList.size();
        }
    }
}

