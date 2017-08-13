package com.villevalta.exampleapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.villevalta.exampleapp.ExampleApplication;
import com.villevalta.exampleapp.R;
import com.villevalta.exampleapp.adapter.ImagesAdapter;
import com.villevalta.exampleapp.model.Image;
import com.villevalta.exampleapp.model.Images;
import com.villevalta.exampleapp.model.Page;
import com.villevalta.exampleapp.network.service.ImgurApiService;
import com.villevalta.exampleapp.view.PaginatingRecyclerView;

import java.util.Date;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by villevalta on 8/13/17.
 */

public class ImagesListFragment extends Fragment implements PaginatingRecyclerView.LoadMoreListener {

    private static final String TAG = "ImagesListFragment";

    public static final String ARG_SUBREDDIT = "sub";
    public static final String ARG_SORT = "sort";

    private String subreddit = "funny";
    private String sort = "hot";

    ImgurApiService apiService;

    private Images images;

    private Realm realm;

    private boolean loading = false;
    private Call<Page> pageCall;

    private PaginatingRecyclerView recycler;
    private ImagesAdapter adapter;

    // Fragmentilla pitää olla tyhjä konstruktori
    public ImagesListFragment() {
    }

    public static ImagesListFragment newInstance(String subreddit, String sort) {
        ImagesListFragment fragment = new ImagesListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SUBREDDIT, subreddit);
        args.putString(ARG_SORT, sort);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        subreddit = getArguments().getString(ARG_SUBREDDIT);
        sort = getArguments().getString(ARG_SORT);

        apiService = ExampleApplication.getInstance().getImgurApiService();
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "onResume");

        realm = Realm.getDefaultInstance();

        // haetaan tallennetut kuvat realmista jos löytyy
        images = realm.where(Images.class).contains("id", subreddit + sort).findFirst();

        // Jos ei löytynyt, lisätään se sinne
        if (images == null) {
            images = new Images();
            images.setId(subreddit + sort);

            // Kaikki realm kirjoitukset pitää tapahtua transaction sisällä
            // Näillä komennoilla voidaan tehdä transactio synkronisesti
            realm.beginTransaction();
            images = realm.copyToRealm(images); // Realm palauttaa oman kopionsa, joten luetaan se takaisin muuttujaan
            realm.commitTransaction();
        }

        adapter.initialize(images);

        // Ensimmäinen sivuhaku, jos sivuja ei ole vielä haettu
        if (images.getPagesLoaded() == 0) {
            loadPage();
        } else {
            Log.d(TAG, "Images loaded from database: " + images.getImages().size());
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list, container, false);

        root.findViewById(R.id.clear_db).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realm.beginTransaction();
                images.reset();
                realm.commitTransaction();
            }
        });

        recycler = (PaginatingRecyclerView) root.findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setLoadMoreListener(this);
        recycler.setPageLoadTriggerLimit(6);
        adapter = new ImagesAdapter();
        recycler.setAdapter(adapter);

        return root;
    }


    @Override
    public void onPause() {

        if (images != null) {
            images.removeAllChangeListeners();
        }

        cancelRequest();

        if (realm != null && !realm.isClosed()) {
            realm.close(); // Suljetaan realm
        }
        super.onPause();
    }

    private void cancelRequest() {
        if (pageCall != null && !pageCall.isCanceled()) {
            pageCall.cancel();
        }
    }

    private void setIsLoading(boolean loading) {
        this.loading = loading;
        if (adapter != null) {
            adapter.setShowLoading(loading);
        }
    }

    private void loadPage() {
        int page = images.getPagesLoaded();
        setIsLoading(true);
        pageCall = apiService.getImagesPage(subreddit, sort, page);
        pageCall.enqueue(new Callback<Page>() {
            @Override
            public void onResponse(Call<Page> call, Response<Page> response) {
                if (response != null && response.body().isSuccess()) {
                    realm.beginTransaction();
                    images.incrementPagesLoaded();
                    images.addImages(response.body().getData());
                    images.setLastUpdated(new Date().getTime());
                    realm.commitTransaction();
                    Log.d(TAG, "Images loaded from web: ");
                    for (Image image : images.getImages()) {
                        Log.d(TAG, "image: " + image.getTitle());
                    }
                } else {
                    Log.e(TAG, "onResponse: NO SUCCESS :(");
                }
                setIsLoading(false);
            }

            @Override
            public void onFailure(Call<Page> call, Throwable t) {
                setIsLoading(false);
                Log.e(TAG, "onFailure:", t);
            }
        });

    }

    @Override
    public void shouldLoadMore() {
        if (!loading) {
            loadPage();
        }
    }

}
