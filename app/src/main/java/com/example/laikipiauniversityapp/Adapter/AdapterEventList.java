package com.example.laikipiauniversityapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laikipiauniversityapp.Activities.AnnouncementDetailsActivity;
import com.example.laikipiauniversityapp.R;
import com.example.laikipiauniversityapp.models.ModelEventList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class AdapterEventList extends RecyclerView.Adapter<AdapterEventList.HolderGroupChatList> {

    private Context context;
    private ArrayList<ModelEventList> eventLists;

    public AdapterEventList(Context context, ArrayList<ModelEventList> eventLists) {
        this.context = context;
        this.eventLists = eventLists;
    }

    @NonNull
    @Override
    public HolderGroupChatList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragmentjoin,parent,false);
        return new HolderGroupChatList(view);

    }

    @Override
    public void onBindViewHolder(@NonNull HolderGroupChatList holder, int position) {
        //get data
        ModelEventList model = eventLists.get(position);
        String eventId = model.getEventId();
        String eventDescription=model.getEventDescription();
        String eventIcon = model.getEventIcon();
        String timestamp= model.getTimestamp();
        String eventDate=model.getEventDate();
        String eventHost=model.getEventHost();
        String eventVenue=model.getEventVenue();
        String formattedDate= model.getTimestamp();
        String eventTitle = model.getEventTitle();
     holder.descriptionTv.setText("");
     holder.timeTv.setText("");

        // set Data
        holder.dateTv.setText(eventDate);
        holder.eventTitleTv.setText(eventTitle);
        holder.descriptionTv.setText(eventDescription);
        holder.timeTv.setText(formattedDate);

        try {
            Picasso.get().load(eventIcon).placeholder(R.drawable.ic_event_add).into(holder.eventIconIv);
        }
        catch (Exception e ){
            holder.eventIconIv.setImageResource(R.drawable.ic_event_add);
        }
        //handle group clicks
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // wil do later
                Intent myactivity = new Intent(context.getApplicationContext(), AnnouncementDetailsActivity.class);
                myactivity.addFlags(FLAG_ACTIVITY_NEW_TASK);
                context.getApplicationContext().startActivity(myactivity);
                myactivity.putExtra(eventId,eventId);
                myactivity.putExtra(eventDate,eventDate);
                myactivity.putExtra(eventIcon,eventIcon);
                myactivity.putExtra(eventDescription,eventDescription);
                myactivity.putExtra(eventHost,eventHost);
                myactivity.putExtra(eventTitle,eventTitle);
                myactivity.putExtra(timestamp,timestamp);
                myactivity.putExtra(eventVenue,eventVenue);

            }
        });
    }


    @Override
    public int getItemCount() {
        return eventLists.size();
    }

    class HolderGroupChatList extends RecyclerView.ViewHolder {
        private ImageView eventIconIv,messageIv;
        private TextView eventTitleTv, dateTv,descriptionTv,timeTv,date_textview,time_textview;
        public HolderGroupChatList(@NonNull View itemView) {
            super(itemView);
            date_textview=itemView.findViewById(R.id.date_textview);
            time_textview=itemView.findViewById(R.id.time_textview);
            eventIconIv = itemView.findViewById(R.id.eventIconIv);
            eventTitleTv =itemView.findViewById(R.id.eventTitleTv);
            dateTv =itemView.findViewById(R.id.dateTv);
            descriptionTv =itemView.findViewById(R.id.descriptionTv);
            timeTv= itemView.findViewById(R.id.timeTv);

        }
    }
}
