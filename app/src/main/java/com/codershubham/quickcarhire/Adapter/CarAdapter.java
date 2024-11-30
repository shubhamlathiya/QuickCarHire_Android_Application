package com.codershubham.quickcarhire.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.codershubham.quickcarhire.BookingActivity;
import com.codershubham.quickcarhire.ClassFile.Car;
import com.codershubham.quickcarhire.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {

    private Context context;
    private List<Car> carList;

    public CarAdapter(Context context, List<Car> carList) {
        this.context = context;
        this.carList = carList;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_car, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        Car car = carList.get(position);

        holder.carNameTextView.setText(car.getCarName());
        holder.brandTextView.setText(car.getBrand());
//        holder.hireCostTextView.setText("Hire Cost: $" + car.getCarHireCost());
        holder.totalBookedTextView.setText("Total Booked: " + car.getTotalBooked());
        holder.seatsTextView.setText(car.getSeats());
        holder.fuelTextView.setText(car.getFuel());
        holder.transmissionTextView.setText(car.getTransmission());

//        Bundle data = DataAdapter.getInstance().getData();
//
//        if (data != null) {
//            String city = data.getString("City");
//            String startDate = data.getString("startDate");
//            String endDate = data.getString("endDate");
//        }

        Picasso.get().load(car.getImage()).into(holder.carImageView);

        holder.cardViewCakeDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to open CarDetailActivity
                Intent intent = new Intent(context, BookingActivity.class);

                // Put the selected car's details as extras
                intent.putExtra("registrationNo", car.getRegistrationNo());
                intent.putExtra("carName", car.getCarName());
                intent.putExtra("brand", car.getBrand());
                intent.putExtra("imageUrl", car.getImage());
                intent.putExtra("carHireCost", car.getCarHireCost());
                intent.putExtra("totalBooked", car.getTotalBooked());
                intent.putExtra("seats", car.getSeats());
                intent.putExtra("fuel", car.getFuel());
                intent.putExtra("transmission", car.getTransmission());

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    static class CarViewHolder extends RecyclerView.ViewHolder {
        ImageView carImageView;
        TextView carNameTextView;
        TextView brandTextView;
        TextView hireCostTextView;
        TextView totalBookedTextView;
        TextView seatsTextView;
        TextView fuelTextView;
        TextView transmissionTextView;
        CardView cardViewCakeDetails;

        CarViewHolder(@NonNull View itemView) {
            super(itemView);
            cardViewCakeDetails = itemView.findViewById(R.id.cardViewCakeDetails);
            carImageView = itemView.findViewById(R.id.carImageView);
            carNameTextView = itemView.findViewById(R.id.carNameTextView);
            brandTextView = itemView.findViewById(R.id.brandTextView);
//            hireCostTextView = itemView.findViewById(R.id.hireCostTextView);
            totalBookedTextView = itemView.findViewById(R.id.totalBookedTextView);
            seatsTextView = itemView.findViewById(R.id.seaterTextView);
            fuelTextView = itemView.findViewById(R.id.fuelTextView);
            transmissionTextView = itemView.findViewById(R.id.transmissionTextView);
        }
    }
}
