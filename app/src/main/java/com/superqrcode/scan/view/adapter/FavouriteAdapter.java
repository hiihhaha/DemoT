package com.superqrcode.scan.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.docxmaster.docreader.base.BaseAdapter;
import com.superqrcode.scan.App;
import com.superqrcode.scan.Const;
import com.superqrcode.scan.R;
import com.superqrcode.scan.databinding.ItemDemoBinding;
import com.superqrcode.scan.databinding.ItemFavoriteBinding;
import com.superqrcode.scan.model.Favourite;
import com.superqrcode.scan.model.History;
import com.superqrcode.scan.utils.CodeGenerator;
import com.superqrcode.scan.utils.CommonUtils;
import com.superqrcode.scan.utils.QRCodeUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FavouriteAdapter extends BaseAdapter<Favourite> {

    public FavouriteAdapter(List<Favourite> mList, Context context) {
        super(mList, context);
    }

    @NotNull
    @Override
    protected RecyclerView.ViewHolder viewHolder(@NotNull ItemDemoBinding parent, int viewType) {
        return new FavouriteViewHolder(ItemFavoriteBinding.inflate(LayoutInflater.from(getContext()), parent, false));
    }

    @Override
    protected void onBindView(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof FavouriteViewHolder) {
            FavouriteViewHolder holder = (FavouriteViewHolder) viewHolder;
            holder.load(getMList().get(position));
        }
    }

    public void updateList(@NotNull List<Favourite> favouriteList) {
        this.setMList(favouriteList);
        notifyDataSetChanged();
    }

    private class FavouriteViewHolder extends RecyclerView.ViewHolder {
        private final ItemFavoriteBinding binding;

        public FavouriteViewHolder(ItemFavoriteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            itemView.setOnClickListener(v -> {
//                if (mCallback != null) {
//                    mCallback.callback(Const.K_CLICK_ITEM, itemView.getTag());
//                }
            });

        }

        @SuppressLint("SetTextI18n")
        public void load(Favourite favourite) {
            History history = App.getInstance().getDatabase().favoriteDao().getHistory(favourite.getHistoryId());
            if (history == null) {
                return;
            }
            history.setFavourite(true);
            itemView.setTag(history);
            try {
                binding.tvContent.setText(history.getQrCode().getResultOfTypeAndValue().getValue());
            } catch (Exception e) {
                e.printStackTrace();
                binding.tvContent.setText(history.getQrCode().getContent());

            }
            if (history.isFavourite()) {
                binding.ivFavorite.setImageResource(R.drawable.ic_favourite_actived);
            } else {
                binding.ivFavorite.setImageResource(R.drawable.ic_favourite);
            }
            binding.tvName.setText(history.getQrCode().getName());
            Glide.with(getContext()).load(QRCodeUtils.getIcon(history.getQrCode().getResultOfTypeAndValue().getType())).into(binding.ivIcon);
            String typeCode = history.getQrCode().getTypeCode() == CodeGenerator.TYPE_QR ? "QR_CODE" : "BAR_CODE";
            binding.tvDate.setText(CommonUtils.getInstance().formatDate(history.getQrCode().getTimeMillis()) + "," + typeCode);
        }
    }
}
