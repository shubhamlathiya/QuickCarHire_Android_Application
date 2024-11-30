package com.codershubham.quickcarhire;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private int pagewidth = 792;
    private int pageHeight = 1120;
    private Bitmap scaledbmp, bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.img);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false);

        generatePDF();
    }

    private void generatePDF() {

        PdfDocument pdfDocument = new PdfDocument();

        Paint paint = new Paint();
        Paint title = new Paint();

        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();

        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);

        Canvas canvas = myPage.getCanvas();

        canvas.drawBitmap(scaledbmp, 56, 40, paint);


        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        title.setTextSize(15);

        title.setColor(ContextCompat.getColor(this, R.color.black));

        canvas.drawText("A portal for IT professionals.", 209, 100, title);
        canvas.drawText("Geeks for Geeks", 209, 80, title);

        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        title.setColor(ContextCompat.getColor(this, R.color.black));
        title.setTextSize(15);

        title.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("This is a sample document that we have created.", 396, 560, title);

        pdfDocument.finishPage(myPage);

        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File pdfFile = new File(downloadsDir, "Quickcarhire.pdf");

        try {
            FileOutputStream fos = new FileOutputStream(pdfFile);
            pdfDocument.writeTo(fos);
            fos.close();

            Toast.makeText(MainActivity.this, "PDF file generated successfully.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {

            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Error generating PDF", Toast.LENGTH_SHORT).show();
        }

        pdfDocument.close();
    }
}
