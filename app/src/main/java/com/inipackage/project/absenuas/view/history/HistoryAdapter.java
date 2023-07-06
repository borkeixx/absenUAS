package com.inipackage.project.absenuas.view.history;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.imageview.ShapeableImageView;
import com.inipackage.project.absenuas.R;
import com.inipackage.project.absenuas.model.ModelDatabase;
import com.inipackage.project.absenuas.utils.BitmapManager;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private Context mContext;
    private List<ModelDatabase> modelDatabase;
    private HistoryAdapterCallback mAdapterCallback;

    public HistoryAdapter(Context context, List<ModelDatabase> modelDatabase, HistoryAdapterCallback callback) {
        this.mContext = context;
        this.modelDatabase = modelDatabase;
        this.mAdapterCallback = callback;
    }

    public void setDataAdapter(List<ModelDatabase> items) {
        modelDatabase.clear();
        modelDatabase.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_history_absen, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ModelDatabase data = modelDatabase.get(position);
        holder.tvNomor.setText(String.valueOf(data.uid)); // Menggunakan atribut uid langsung dari objek data
        holder.tvNama.setText(data.nama);
        holder.tvLokasi.setText(data.lokasi);
        holder.tvAbsenTime.setText(data.tanggal);
        holder.tvStatusAbsen.setText(data.keterangan);

        Glide.with(mContext)
                .load(BitmapManager.base64ToBitmap(data.fotoSelfie))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_photo_camera)
                .into(holder.imageProfile);

        switch (data.keterangan) {
            case "Absen Masuk":
                holder.colorStatus.setBackgroundResource(R.drawable.bg_circle_radius);
                holder.colorStatus.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                break;
            case "Absen Keluar":
                holder.colorStatus.setBackgroundResource(R.drawable.bg_circle_radius);
                holder.colorStatus.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                break;
            case "Izin":
                holder.colorStatus.setBackgroundResource(R.drawable.bg_circle_radius);
                holder.colorStatus.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
                break;
        }
    }


    @Override
    public int getItemCount() {
        return modelDatabase.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvStatusAbsen;
        private TextView tvNomor;
        private TextView tvNama;
        private TextView tvLokasi;
        private TextView tvAbsenTime;
        private CardView cvHistory;
        private ShapeableImageView imageProfile;
        private View colorStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            tvStatusAbsen = itemView.findViewById(R.id.tvStatusAbsen);
            tvNomor = itemView.findViewById(R.id.tvNomor);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvLokasi = itemView.findViewById(R.id.tvLokasi);
            tvAbsenTime = itemView.findViewById(R.id.tvAbsenTime);
            cvHistory = itemView.findViewById(R.id.cvHistory);
            imageProfile = itemView.findViewById(R.id.imageProfile);
            colorStatus = itemView.findViewById(R.id.colorStatus);

            cvHistory.setOnClickListener(view -> {
                ModelDatabase modelLaundry = modelDatabase.get(getAdapterPosition());
                mAdapterCallback.onDelete(modelLaundry);
            });
        }
    }

    public interface HistoryAdapterCallback {
        void onDelete(ModelDatabase modelDatabase);
    }
}
