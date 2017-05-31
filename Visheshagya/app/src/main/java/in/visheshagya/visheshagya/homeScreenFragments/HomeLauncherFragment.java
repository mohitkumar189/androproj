package in.visheshagya.visheshagya.homeScreenFragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import in.visheshagya.visheshagya.R;
import in.visheshagya.visheshagya.activities.ConsultImmActivity;
import in.visheshagya.visheshagya.activities.ExpertsListWithCategory;
import in.visheshagya.visheshagya.adapters.SlideOfferImageAdapter;
import in.visheshagya.visheshagya.resourses.Constants;

public class HomeLauncherFragment extends Fragment implements View.OnClickListener {

    private static final Integer[] IMAGES = {R.drawable.vishesh_logo, R.drawable.vishesh_logo};
    private static ViewPager offerImagePager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();
    private ImageView caImage, csImage, cmaImage, lawImage;
    private Button consultImButton;
    private LinearLayout taxLayout, legalLayout;


    public HomeLauncherFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_launcher, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        caImage = (ImageView) getActivity().findViewById(R.id.caImageBtn);
        csImage = (ImageView) getActivity().findViewById(R.id.csImageBtn);
        cmaImage = (ImageView) getActivity().findViewById(R.id.cmaImageBtn);
        lawImage = (ImageView) getActivity().findViewById(R.id.lawImageBtn);
        //consultImButton=(Button) getActivity().findViewById(R.id.consultImButtonTax);
        taxLayout = (LinearLayout) getActivity().findViewById(R.id.consultImLayoutTax); // layout for immediately
        legalLayout = (LinearLayout) getActivity().findViewById(R.id.consultImLayoutLegal); // layout for immediately
        taxLayout.setOnClickListener(this);
        legalLayout.setOnClickListener(this);
        caImage.setOnClickListener(this);
        csImage.setOnClickListener(this);
        cmaImage.setOnClickListener(this);
        lawImage.setOnClickListener(this);
        init();
        //consultImButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), ExpertsListWithCategory.class);
        Log.d("button", "Button pressed");
        int res_id = view.getId();
        switch (res_id) {
            case R.id.caImageBtn:
                //Toast.makeText(context,"ca "+Constants.CATEGORY_CA,Toast.LENGTH_SHORT).show();
                intent.putExtra("categoryName", Constants.CATEGORY_CA);
                startActivity(intent);
                //  startActivity(new Intent(context, ExpertsListActivity.class));
                break;
            case R.id.csImageBtn:
                //Toast.makeText(context,"cs Image pressed",Toast.LENGTH_SHORT).show();
                intent.putExtra("categoryName", Constants.CATEGORY_CS);
                startActivity(intent);
                // startActivity(new Intent(context, ExpertsListActivity.class));
                break;
            case R.id.cmaImageBtn:
                //Toast.makeText(context,"cma Image pressed",Toast.LENGTH_SHORT).show();
                intent.putExtra("categoryName", Constants.CATEGORY_CMA);
                startActivity(intent);
                //  startActivity(new Intent(context, ExpertsListActivity.class));
                break;
            case R.id.lawImageBtn:
                // Toast.makeText(context,"law Image pressed",Toast.LENGTH_SHORT).show();
                intent.putExtra("categoryName", Constants.CATEGORY_LAW);
                startActivity(intent);
                //startActivity(new Intent(context, ExpertsListActivity.class));
                break;
            case R.id.consultImLayoutLegal:
                // Toast.makeText(context,"legal pressed",Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(getActivity(), ConsultImmActivity.class);
                intent1.putExtra("expertType", "legal");
                startActivity(intent1);
                break;
            case R.id.consultImLayoutTax:
                //Toast.makeText(context,"tax pressed",Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(getActivity(), ConsultImmActivity.class);
                intent2.putExtra("expertType", "tax");
                startActivity(intent2);
                break;
            default:
                break;
        }

    }

    private void init() {

        for (int i = 0; i < IMAGES.length; i++)
            ImagesArray.add(IMAGES[i]);

        offerImagePager = (ViewPager) getActivity().findViewById(R.id.offerImagePager);
        offerImagePager.setAdapter(new SlideOfferImageAdapter(getActivity(), ImagesArray));

        final float density = getResources().getDisplayMetrics().density;

        NUM_PAGES = IMAGES.length;

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                offerImagePager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);


    }
}
