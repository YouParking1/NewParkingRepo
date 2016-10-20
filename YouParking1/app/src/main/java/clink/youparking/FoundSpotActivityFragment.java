package clink.youparking;

import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class FoundSpotActivityFragment extends Fragment {

    public Fragment mapFrag;
    public FloatingActionButton fab;

    public FoundSpotActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_found_spot, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = new Bundle();
        bundle.putString("TYPE", "BOUGHT");

        mapFrag = new GMapFragment();
        mapFrag.setArguments(bundle);

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.bought_spot_map, mapFrag).commit();

        fab = (FloatingActionButton)getView().findViewById(R.id.fab_details);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VehicleDetailsDialog dialog = new VehicleDetailsDialog();
                dialog.show(getActivity().getFragmentManager(), "tag");
            }
        });
    }
}
