package com.example.laikipiauniversityapp.Adapter;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.laikipiauniversityapp.Activities.PdfDetailsActivity;
import com.example.laikipiauniversityapp.Activities.pdfEditActivity;
import com.example.laikipiauniversityapp.Filters.FilterPdfAdmin;
import com.example.laikipiauniversityapp.models.ModelPdf;
import com.example.laikipiauniversityapp.Activities.MyApplication;
import com.example.laikipiauniversityapp.databinding.RowPdfListAdminBinding;
import com.github.barteksc.pdfviewer.PDFView;

import java.util.ArrayList;

public class AdapterPdfAdmin extends  RecyclerView.Adapter<AdapterPdfAdmin.HolderPdfAdmin> implements Filterable {
    private final Context context;

    public ArrayList<ModelPdf> pdfArrayList,fileterList;

    private RowPdfListAdminBinding binding;
    private FilterPdfAdmin filter;

    private  static  final  String TAG = "PDF_ADAPTER_TAG";
    private ProgressDialog progressDialog;


    public AdapterPdfAdmin(Context context, ArrayList<ModelPdf> pdfArrayList) {
        this.context = context;
        this.pdfArrayList = pdfArrayList;
        this.fileterList = pdfArrayList;

        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @NonNull
    @Override
    public HolderPdfAdmin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowPdfListAdminBinding.inflate(LayoutInflater.from(context),parent,false);

        return new HolderPdfAdmin(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderPdfAdmin holder, int position) {
        // getdata

        ModelPdf model  = pdfArrayList.get(position);
        String title = model.getTitle();
        String pdfId = model.getId();
        String pdfUrl = model.getUrl();
        String pdfTitle = model.getTitle();
        String categoryId = model.getCategoryId();
        String description = model.getDescription();
        long timestamp = model.getTimestamp();

        String formatDate = MyApplication.formatTimestamp(timestamp);

        holder.titleTv.setText(title);
        holder.dateTv.setText(formatDate);
        holder.descriptionTv.setText(description);

        // load from url in separe functions
        MyApplication.loadCategory(
                ""+categoryId,
                holder.categoryTv);
        MyApplication.loadPdfFromUrlSinglePage(
               ""+pdfUrl,""+pdfTitle,
                binding.pdfView,
                binding.progressBar
        );
        MyApplication.loadPdfSize(
                ""+pdfUrl,
                ""+pdfTitle,
                holder.sizeTv);

        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreOptionDialog(model,holder);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PdfDetailsActivity.class);
                intent.putExtra("bookId", pdfId);
                context.startActivity(intent);
            }
        });
    }

    private void moreOptionDialog(ModelPdf model, HolderPdfAdmin holder) {
        String bookId= model.getId();
        String bookUrl = model.getUrl();
        String bookTitle = model.getTitle();
//        optio to show dialog
        String[] options ={"Edit","Delete"};
//        alrt dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose Options")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//               clicks handle
                        if (which == 0){
                            //edit clickopen new activity
                            Intent intent = new Intent(context, pdfEditActivity.class);
                            intent.putExtra("bookId",bookId);
                            context.startActivity(intent );

                        }
                        else if (which == 1)
                        {
//                            delete click
                            MyApplication.deleteBook(context,
                                    ""+bookId,
                                    ""+bookUrl,
                                    ""+bookTitle);
//
                        }
                    }
                }).show();
    }






    @Override
    public int getItemCount() {
        return pdfArrayList.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null){
            filter = new FilterPdfAdmin(fileterList,this);
        }
        return filter;
    }

    class HolderPdfAdmin extends RecyclerView.ViewHolder {

        PDFView pdfView;
        ProgressBar progressBar;
        TextView titleTv,descriptionTv,categoryTv,sizeTv,dateTv;
        ImageButton moreBtn;
        public HolderPdfAdmin(@NonNull View itemView) {

            super(itemView);
            pdfView = binding.pdfView;
            progressBar = binding.progressBar;
            categoryTv= binding.categoryTv;
            titleTv = binding.titleTv;
            descriptionTv = binding.descriptionTv;
            sizeTv= binding.sizeTv;
            dateTv= binding.dateTv;
            moreBtn = binding.moreBtn;
        }
    }
}

