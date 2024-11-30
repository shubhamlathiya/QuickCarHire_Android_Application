package com.codershubham.quickcarhire.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.codershubham.quickcarhire.CancelBooking;
import com.codershubham.quickcarhire.ClassFile.Booking;
import com.codershubham.quickcarhire.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.BookingViewHolder> {
    private Context context;
    private List<Booking> bookingList;
    private int pagewidth = 792;
    private int pageHeight = 1120;
    private Bitmap scaledbmp, bmp;


    public HistoryAdapter(Context context, List<Booking> bookingList) {
        this.context = context;
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);


//        Toast.makeText(context, String.valueOf(booking.getId()), Toast.LENGTH_SHORT).show();

        holder.carNameTextView.setText(booking.getName() + "\n" + booking.getRegistrationNo());
        holder.address.setText(booking.getAddress());
        holder.amount.setText(String.valueOf(booking.getAmount()));



        String bookingStartDateString = booking.getStartDate();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date bookingStartDate = dateFormat.parse(bookingStartDateString);

            Date currentDate = new Date();

//if(String.valueOf(booking.getId()) == ){
//
//}
            if (bookingStartDate.after(currentDate)) {
                holder.cancelbtn.setVisibility(View.VISIBLE);

                holder.cancelbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(context, CancelBooking.class);
                        i.putExtra("bookingID" ,String.valueOf(booking.getId()));
                        context.startActivity(i);
                    }
                });
            } else {
                holder.cancelbtn.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.start_date.setText(booking.getStartDate());
        holder.end_date.setText(booking.getEndDate());

        Picasso.get().load(booking.getImage()).into(holder.carImageView);

        holder.pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String carname = booking.getName();
                String startDate = booking.getStartDate();
                String endDate = booking.getEndDate();
                String address = booking.getAddress();
                String  Rno= booking.getRegistrationNo();
                String img = booking.getImage();
                String amount = String.valueOf((int) booking.getAmount());
                String kms = booking.getSelected_Kms();
                String Security_Deposit =booking.getSecurity_Deposit();
                String city =booking.getCity();

                generatePDF(Rno,carname,address,startDate,endDate,img,amount,city,kms,Security_Deposit);
            }
        });
    }


    @SuppressLint("StaticFieldLeak")
    private void generatePDF(String Rno, String carname, String address, String startDate, String endDate, String img, String amount, String city, String kms, String Security_Deposit) {
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint title = new Paint();
        Paint text = new Paint();

        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();
        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);
        Canvas canvas = myPage.getCanvas();

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        title.setTextSize(15);
        text.setTextSize(28);
        text.setColor(context.getColor(R.color.bule)); // Assuming you meant "blue" here

        title.setColor(ContextCompat.getColor(context, R.color.black));

        new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... params) {
                String imgUrl = params[0];
                try {
                    URL url = new URL(imgUrl);
                    return BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                if (result != null) {
                    Bitmap scaledbmp = Bitmap.createScaledBitmap(result, 140, 140, false);
                    canvas.drawBitmap(scaledbmp, 56, 100, paint);

                    // Continue drawing text and other elements
                    canvas.drawText("Quick Car Hire", 380, 60, text);
                    canvas.drawText("Car Name :- " + carname, 209, 100, title);
                    canvas.drawText("Car Registration No:- " + Rno, 209, 120, title);
                    canvas.drawText("Address :- " + address, 209, 140, title);
                    canvas.drawText("Start Date :- " + startDate, 209, 160, title);
                    canvas.drawText("End Date :- " + endDate, 209, 180, title);
                    canvas.drawText("City :- " + city, 209, 200, title);
                    canvas.drawText("Security Deposit :- " + Security_Deposit, 209, 220, title);
                    canvas.drawText("Selected Kms :- " + kms, 209, 240, title);
                    canvas.drawText("Total Amount :- " + amount, 209, 260, title);

                    title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    title.setColor(ContextCompat.getColor(context, R.color.black));
                    title.setTextSize(15);

                    canvas.drawText("Term & Conditions", 140, 540, text);
                    canvas.drawText("1)Extra kms charge: 23 per KM 2)Fuel: Full Tank", 100, 560, title);
                    canvas.drawText("3)Tolls, Parking & Inter-state taxes: To be paid by you", 100, 580, title);
                    canvas.drawText("4)Cancellation Charges RS. 500 before booking start date and after not cancel", 100, 600, title);
                    canvas.drawText("5)ID Verification: Please keep your original Driving License handy.", 100, 620, title);
                    canvas.drawText("6)Pre-Handover Inspection: Please inspect the car (including the fuel gauge and odometer).", 100, 640, title);


                    title.setTextAlign(Paint.Align.CENTER);


                    pdfDocument.finishPage(myPage);

                    File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    File pdfFile = new File(downloadsDir, Rno + ".pdf");

                    try {
                        FileOutputStream fos = new FileOutputStream(pdfFile);
                        pdfDocument.writeTo(fos);
                        fos.close();

                        Toast.makeText(context, "PDF file generated successfully.", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Error generating PDF", Toast.LENGTH_SHORT).show();
                    } finally {
                        pdfDocument.close();
                    }
                } else {
                    Toast.makeText(context, "Error loading image from URL", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute(img);
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        ImageView carImageView;
        TextView cancelbtn;
        TextView carNameTextView;
        TextView registrationNoTextView, address, start_date, end_date, amount, pdf;
        CardView cardView;

        BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            carImageView = itemView.findViewById(R.id.carImageView);
            carNameTextView = itemView.findViewById(R.id.carNameTextView);
            address = itemView.findViewById(R.id.address);
            start_date = itemView.findViewById(R.id.start_date);
            end_date = itemView.findViewById(R.id.end_date);
            cancelbtn = itemView.findViewById(R.id.cancelbtn);
            amount = itemView.findViewById(R.id.amount);
            cardView = itemView.findViewById(R.id.cardView);
            pdf = itemView.findViewById(R.id.pdf);
        }
    }
}