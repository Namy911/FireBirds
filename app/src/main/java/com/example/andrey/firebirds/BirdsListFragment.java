package com.example.andrey.firebirds;

import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrey.firebirds.Repository.Repository;
import com.example.andrey.firebirds.model.Bird;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BirdsListFragment extends Fragment{

    @BindView(R.id.floatBtnAdd) FloatingActionButton btnAddBird;
    @BindView(R.id.recy_birds_list) RecyclerView recyBirds;

    public final static String LOGIN = "login_";

    private FirebaseAuth mAuth;
    private DatabaseReference dataBase;

    private RecyclerView.Adapter adapter ;
    Map<String, Bird> allInfoBirds;
    String id;

    public static BirdsListFragment newInstance(String login) {
        Bundle args = new Bundle();
        args.putString(LOGIN, login);
        BirdsListFragment fragment = new BirdsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_birds_lis, container, false);
        ButterKnife.bind(this, view);

        recyBirds.setLayoutManager(new LinearLayoutManager(getActivity()));
        dataBase = FirebaseDatabase.getInstance().getReference();

        updateUI();
        return view;
    }

    private void updateUI() {
        DatabaseReference refBirds = FirebaseDatabase.getInstance().getReference(Repository.TABLE_BIRDS);
        refBirds.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Bird> birds = new ArrayList<>();
                List<String> idBirds = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    birds.add(snapshot.getValue(Bird.class));
                    idBirds.add(snapshot.getKey());
                    //allInfoBirds.put(snapshot.getKey(), snapshot.getValue(Bird.class));
                }

                adapter = new BirdsAdapter(birds, idBirds);
                adapter.notifyDataSetChanged();
                recyBirds.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void signOut(){
        //FirebaseAuth.getInstance().signOut();
        mAuth.signOut();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, new LoginFragment())
                .commit();
        Toast.makeText(getActivity(), "Log Out.",
                Toast.LENGTH_SHORT).show();
    }

    private void birdAction(String action){
        getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, BirdFragment.newInstance(action))
                .commit();
    }

    @OnClick(R.id.floatBtnAdd)
    public void onClick(View v) {
        birdAction(BirdFragment.ADD_BIRD);
    }

    private class BirdsHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{

        private TextView birdName, birdId;
        private String id;

        public BirdsHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.fragment_birds_item, parent, false));

            birdName = itemView.findViewById(R.id.bird_name);
            birdId = itemView.findViewById(R.id.bird_id);
            itemView.setOnLongClickListener(this);

        }

        public void bind(Bird bird, String idBird) {
            //id = bird.getId();
            id = idBird;
            birdName.setText(bird.getName());
            birdId.setText(Long.toString(bird.getBirth()));
        }


        @Override
        public boolean onLongClick(View v) {
            birdAction(id);
            return true;
        }
    }
    private class BirdsAdapter extends RecyclerView.Adapter<BirdsHolder>{
        private List<Bird> birdList;
        private List<String> idBirds;

        public BirdsAdapter(List<Bird> birdList, List<String> idBirds) {
            this.birdList = birdList;
            this.idBirds = idBirds;
        }

        @Override
        public BirdsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new BirdsHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(BirdsHolder holder, int position) {
            Bird bird = birdList.get(position);
            String idBird = idBirds.get(position);
            holder.bind(bird, idBird);
        }

        @Override
        public int getItemCount() {
            return birdList.size();
        }
    }
}
