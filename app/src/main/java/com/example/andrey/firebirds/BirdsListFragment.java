package com.example.andrey.firebirds;

import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BirdsListFragment extends Fragment implements View.OnClickListener{

    public final static String LOGIN = "login_";
    private FloatingActionButton btnAddBird;

    private FirebaseAuth mAuth;
    private DatabaseReference dataBase;

    private RecyclerView.Adapter adapter ;
    private RecyclerView recyBirds;

    public static BirdsListFragment newInstance(String login) {
        Bundle args = new Bundle();
        args.putString(LOGIN, login);
        BirdsListFragment fragment = new BirdsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments().getString(LOGIN) != null){
//
//        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_birds_lis, container, false);
        recyBirds = view.findViewById(R.id.recy_birds_list);
        recyBirds.setLayoutManager(new LinearLayoutManager(getActivity()));
        dataBase = FirebaseDatabase.getInstance().getReference();

        btnAddBird = view.findViewById(R.id.floatBtnAdd);
        btnAddBird.setOnClickListener(this);

        updateUI();
        return view;
    }

    private void updateUI() {
        DatabaseReference refBirds = FirebaseDatabase.getInstance().getReference(BirdFragment.TABLE_BIRDS);
        refBirds.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Bird> birds = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    birds.add(snapshot.getValue(Bird.class));
                }
                adapter = new BirdsAdapter(birds);
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
    @Override
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

        public void bind(Bird bird) {
            id = bird.getId();
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

        public BirdsAdapter(List<Bird> birdList) {
            this.birdList = birdList;
        }

        @Override
        public BirdsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new BirdsHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(BirdsHolder holder, int position) {
            Bird bird = birdList.get(position);
            holder.bind(bird);
        }

        @Override
        public int getItemCount() {
            return birdList.size();
        }
    }
}
