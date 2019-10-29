package Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import com.orm.SugarRecord;
import java.util.ArrayList;
import java.util.List;
import Adapter.NewsRecycleAdapter;
import Controller.Common;
import Controller.Api;
import Controller.DataFromApi;
import Utils.EndlessRecyclerViewScrollListener;
import Models.NewsClass;
import Models.NewsResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.shahbaapp.lft.R;

public class WhtsNewFragment extends Fragment {

    View view;

    private Api api;
    private NewsRecycleAdapter bAdapter;


    private RecyclerView recyclerView;


    private List<NewsClass> newsList;

    private SwipeRefreshLayout swipe;
    private RecyclerView.LayoutManager layoutManager;
    private EndlessRecyclerViewScrollListener scrollListener;
    public long subcategoryId;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_whts_new, container, false);
        newsList = new ArrayList<>();

        swipe = view.findViewById(R.id.swipe);
        swipe.getLayoutParams().height = Common.getScreenHeight(getActivity()) + 250;
        swipe.setColorSchemeResources(R.color.orange, R.color.twittercolor, R.color.redBg);
        swipe.setRefreshing(true);
        swipe.setOnRefreshListener(onRefreshListener);



        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        bAdapter = new NewsRecycleAdapter(getActivity(), newsList);
        recyclerView.setAdapter(bAdapter);

        api = DataFromApi.getApi();
        fetchDataFromDb(WhtsNewFragment.this.subcategoryId, 1);
        fetchDataFromApi(1);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                fetchDataFromApi(page);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);


        return view;
    }


    private void fetchDataFromApi(final int page) {
        Call<NewsResult> call = api.News(subcategoryId, page);

        call.enqueue(new Callback<NewsResult>() {
            @Override
            public void onResponse(Call<NewsResult> call, Response<NewsResult> response) {
                swipe.setRefreshing(false);
                NewsResult news = response.body();
                if (news.results != null) {
                    newsList.addAll(filter(news.results));
                    bAdapter.notifyDataSetChanged();
                    saveToDb(WhtsNewFragment.this.subcategoryId);
                }
            }

            @Override
            public void onFailure(Call<NewsResult> call, Throwable t) {
                swipe.setRefreshing(false);
                fetchDataFromDb(WhtsNewFragment.this.subcategoryId, page);
                bAdapter.notifyDataSetChanged();
            }
        });
    }


    private void fetchDataFromDb(long id, int page) {
        int limit = 5;
        int offset = (page - 1) * limit;
        String query = "SELECT * FROM NEWS_CLASS WHERE PRIVATE_NEWS_TYPE = 0 and SUBCATEGORY_FK = ? ORDER BY CREATION_DATE DESC LIMIT ? OFFSET ? ";

        List<NewsClass> temp = SugarRecord.findWithQuery(NewsClass.class, query , new String[]{String.valueOf(id),
                String.valueOf(limit), String.valueOf(offset)});

        if (temp.size() > 0) {
            newsList.addAll(filter(temp));
        }
    }


    private void saveToDb(long subcategoryId) {

        for (NewsClass item : newsList)
            item.save();

    }

    private List<NewsClass> filter(List<NewsClass> temp){
        List<NewsClass> filter = temp;
        for(NewsClass newsFromAPI : temp){
            boolean isExist = false;
            for(NewsClass newsRecycle : newsList) {
                isExist = newsRecycle.getId() == newsFromAPI.getId();
                if (isExist) {
                    newsList.remove(newsRecycle);
                    break;
                }
            }
        }

        return filter;
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            scrollListener.resetState();
            newsList.clear();
            bAdapter.notifyDataSetChanged();
            fetchDataFromApi(1);
        }
    };

}
