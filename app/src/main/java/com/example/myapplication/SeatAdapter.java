package com.example.myapplication;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.models.Seat;
import java.util.List;

public class SeatAdapter extends RecyclerView.Adapter<SeatAdapter.SeatViewHolder> {
    private List<Seat> seats;
    private OnSeatClickListener listener;

    public interface OnSeatClickListener {
        void onSeatClick(Seat seat);
    }

    public SeatAdapter(List<Seat> seats, OnSeatClickListener listener) {
        this.seats = seats;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SeatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        // Tùy chỉnh kích thước cho ô ghế
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = parent.getMeasuredWidth() / 6; // 6 ghế mỗi hàng
        params.height = 120;
        view.setLayoutParams(params);
        return new SeatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeatViewHolder holder, int position) {
        Seat seat = seats.get(position);
        holder.tvSeat.setText(seat.getSeatNumber());
        holder.tvSeat.setTextSize(12);
        holder.tvSeat.setGravity(android.view.Gravity.CENTER);

        if (seat.isBooked()) {
            holder.itemView.setBackgroundColor(Color.parseColor("#F44336")); // Đỏ: Đã đặt
            holder.tvSeat.setTextColor(Color.WHITE);
        } else if (seat.isSelected()) {
            holder.itemView.setBackgroundColor(Color.parseColor("#4CAF50")); // Xanh: Đang chọn
            holder.tvSeat.setTextColor(Color.WHITE);
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#DDDDDD")); // Xám: Trống
            holder.tvSeat.setTextColor(Color.BLACK);
        }

        holder.itemView.setOnClickListener(v -> {
            if (!seat.isBooked()) {
                listener.onSeatClick(seat);
            }
        });
    }

    @Override
    public int getItemCount() {
        return seats.size();
    }

    static class SeatViewHolder extends RecyclerView.ViewHolder {
        TextView tvSeat;
        public SeatViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSeat = itemView.findViewById(android.R.id.text1);
        }
    }
}
