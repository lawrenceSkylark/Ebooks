package com.example.laikipiauniversityapp.Adapter;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laikipiauniversityapp.Activities.PdfDetailsActivity;
import com.example.laikipiauniversityapp.models.ModelPdf;
import com.example.laikipiauniversityapp.Activities.MyApplication;
import com.example.laikipiauniversityapp.databinding.RowPdfFavoriteBinding;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdapterPdfFavorite extends  RecyclerView.Adapter<AdapterPdfFavorite.HolderPdfFavorite> {
    private Context context;
    private ArrayList<ModelPdf> pdfArrayList;
    private RowPdfFavoriteBinding binding;

    private  static final String TAG = "FAV_BOOK_TAG";

    public AdapterPdfFavorite(Context context, ArrayList<ModelPdf> pdfArrayList) {
        this.context = context;
        this.pdfArrayList = pdfArrayList;
    }

    @NonNull
    @Override
    public HolderPdfFavorite onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= RowPdfFavoriteBinding.inflate(LayoutInflater.from(context));
        return  new HolderPdfFavorite(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderPdfFavorite holder, int position) {

        ModelPdf modelPdf = pdfArrayList.get(position);

        loadBookDetails(modelPdf ,holder);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PdfDetailsActivity.class);
                intent.putExtra("bookId",modelPdf.getId());
                context.startActivity(intent);

            }
        });

        holder.removeFavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.removeFromFavorite(context,modelPdf.getId());

            }
        });
    }

    private void loadBookDetails(ModelPdf modelPdf, HolderPdfFavorite holder) {
        String bookId = modelPdf.getId();
        Log.d(TAG, "loadBookDetails: Book Details of Book Id  "+bookId);
        DatabaseReference reference  = FirebaseDatabase.getInstance().getReference("Books");
        reference.child(bookId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String bookTitle = ""+snapshot.child("title").getValue();
                        String description = ""+snapshot.child("description").getValue();
                        String categoryId = ""+snapshot.child("categoryId").getValue();
                        String viewsCount = ""+snapshot.child("viewsCount").getValue();
                        String uid = ""+snapshot.child("uid").getValue();
                        String timestamp = ""+snapshot.child("timestamp").getValue();
                        String pdfUrl = ""+snapshot.child("Url").getValue();
                        String downloadsCount = ""+snapshot.child("downloadsCount").getValue();

                        modelPdf.setFavorite(true);
                        modelPdf.setTitle(bookTitle);
                        modelPdf.setDescription(description);
                        modelPdf.setTimestamp(Long.parseLong(timestamp));
                        modelPdf.setUid(uid);
                        modelPdf.setUrl(pdfUrl);
                        modelPdf.setCategoryId(categoryId);

                        String date = MyApplication.formatTimestamp(Long.parseLong(timestamp));
                        MyApplication.loadCategory(categoryId,holder.categoryTv);
                        MyApplication.loadPdfFromUrlSinglePage(
                                ""+pdfUrl,
                                ""+bookTitle,
                                holder.pdfView,
                                holder.progressBar);
                        MyApplication.loadPdfSize(""+pdfUrl,""+bookTitle,holder.sizeTv);
                        holder.dateTv.setText(date);
                        holder.titleTv.setText(bookTitle);
                        holder.descriptionTv.setText(description);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return pdfArrayList.size();
    }

    class  HolderPdfFavorite extends RecyclerView.ViewHolder {

        PDFView pdfView;
        ProgressBar progressBar;
        TextView titleTv, descriptionTv,dateTv,categoryTv,sizeTv;
        ImageButton removeFavBtn;

        public HolderPdfFavorite(@NonNull View itemView) {
            super(itemView);

            pdfView = binding.pdfView;
            titleTv=binding.titleTv;
            removeFavBtn= binding.removeFavBtn;
            dateTv= binding.dateTv;
            categoryTv=binding.categoryTv;
            sizeTv=binding.sizeTv;
            progressBar =binding.progressBar;
            descriptionTv =binding.descriptionTv;

        }
    }
}
