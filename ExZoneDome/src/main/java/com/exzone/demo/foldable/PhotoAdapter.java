package com.exzone.demo.foldable;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.exzone.demo.R;
import com.exzone.lib.view.FoldableLayout;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Add a class header comment!
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    private String[] mDataSet;
    private Map<Integer, Boolean> mFoldStates = new HashMap<>();
    private Context mContext;

    public PhotoAdapter(String[] dataSet, Context context) {
        mDataSet = dataSet;
        mContext = context;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PhotoViewHolder(new FoldableLayout(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, int position) {
        final String path = "content://com.exzone.demo.foldable/demo-pictures/" + mDataSet[position];

        Picasso.with(holder.mFoldableLayout.getContext()).load(path).into(holder.mImageViewCover);
        Picasso.with(holder.mFoldableLayout.getContext()).load(path).into(holder.mImageViewDetail);
        holder.mTextViewCover.setText(mDataSet[position].replace(".jpg", ""));

        // Bind state
        if (mFoldStates.containsKey(position)) {
            if (mFoldStates.get(position) == Boolean.TRUE) {
                if (!holder.mFoldableLayout.isFolded()) {
                    holder.mFoldableLayout.foldWithoutAnimation();
                }
            } else if (mFoldStates.get(position) == Boolean.FALSE) {
                if (holder.mFoldableLayout.isFolded()) {
                    holder.mFoldableLayout.unfoldWithoutAnimation();
                }
            }
        } else {
            holder.mFoldableLayout.foldWithoutAnimation();
        }

        holder.mButtonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/jpg");
                Uri uri = Uri.parse(path);
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                mContext.startActivity(Intent.createChooser(shareIntent, "Share image using"));
            }
        });

        holder.mFoldableLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.mFoldableLayout.isFolded()) {
                    holder.mFoldableLayout.unfoldWithAnimation();
                } else {
                    holder.mFoldableLayout.foldWithAnimation();
                }
            }
        });
        holder.mFoldableLayout.setFoldListener(new FoldableLayout.FoldListener() {
            @Override
            public void onUnFoldStart() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.mFoldableLayout.setElevation(5);
                }
            }

            @Override
            public void onUnFoldEnd() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.mFoldableLayout.setElevation(0);
                }
                mFoldStates.put(holder.getAdapterPosition(), false);
            }

            @Override
            public void onFoldStart() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.mFoldableLayout.setElevation(5);
                }
            }

            @Override
            public void onFoldEnd() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.mFoldableLayout.setElevation(0);
                }
                mFoldStates.put(holder.getAdapterPosition(), true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.length;
    }

    protected static class PhotoViewHolder extends RecyclerView.ViewHolder {

        protected FoldableLayout mFoldableLayout;

        protected ImageView mImageViewCover;

        protected ImageView mImageViewDetail;

        protected TextView mTextViewCover;

        protected Button mButtonShare;

        public PhotoViewHolder(FoldableLayout foldableLayout) {
            super(foldableLayout);
            mFoldableLayout = foldableLayout;
            foldableLayout.setupViews(R.layout.list_item_cover, R.layout.list_item_detail, R.dimen.card_cover_height, itemView.getContext());

            mImageViewDetail = (ImageView) mFoldableLayout.getDetailView().findViewById(R.id.imageview_detail);

            mImageViewCover = (ImageView) mFoldableLayout.getCoverView().findViewById(R.id.imageview_cover);
            mTextViewCover = (TextView) mFoldableLayout.getCoverView().findViewById(R.id.textview_cover);
            mButtonShare = (Button) mFoldableLayout.getCoverView().findViewById(R.id.share_button);
        }
    }
}
