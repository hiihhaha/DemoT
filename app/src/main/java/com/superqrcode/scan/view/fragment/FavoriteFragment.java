package com.superqrcode.scan.view.fragment;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.docxmaster.docreader.base.BaseFragment;
import com.superqrcode.scan.App;
import com.superqrcode.scan.Const;
import com.superqrcode.scan.R;
import com.superqrcode.scan.databinding.FragmentFavoriteBinding;
import com.superqrcode.scan.model.Favourite;
import com.superqrcode.scan.view.OnActionCallback;
import com.superqrcode.scan.view.activity.MainActivity;
import com.superqrcode.scan.view.adapter.FavouriteAdapter;

import java.util.List;

public class FavoriteFragment extends BaseFragment {

    private List<Favourite> list;
    private FavouriteAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_favorite;
    }

    @Override
    protected void initView() {
        list = App.getInstance().getDatabase().favoriteDao().getList();
        adapter = new FavouriteAdapter(list, getContext());

//        .setLayoutManager(new LinearLayoutManager(context));
//        binding.rvFavourite.setAdapter(adapter);
    }

    @Override
    protected void addEvent() {

    }

    public static FavoriteFragment newInstance() {

        Bundle args = new Bundle();

        FavoriteFragment fragment = new FavoriteFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
