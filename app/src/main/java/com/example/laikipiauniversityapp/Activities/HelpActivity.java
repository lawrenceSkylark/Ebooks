package com.example.laikipiauniversityapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.example.laikipiauniversityapp.copied.DummyChildDataItem;
import com.example.laikipiauniversityapp.copied.DummyParentDataItem;
import com.example.laikipiauniversityapp.copied.RecyclerDataAdapter;
import com.example.laikipiauniversityapp.R;
import com.example.laikipiauniversityapp.databinding.ActivityHelpBinding;

import java.util.ArrayList;

public class HelpActivity extends AppCompatActivity {
    private ActivityHelpBinding binding;
    private RecyclerView mRecyclerView;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityHelpBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        mContext = HelpActivity.this;
        mRecyclerView = findViewById(R.id.recyclerView);
        RecyclerDataAdapter recyclerDataAdapter = new RecyclerDataAdapter(getDummyDataToPass());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(recyclerDataAdapter);
        mRecyclerView.setHasFixedSize(true);

     binding.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }



        private ArrayList<DummyParentDataItem> getDummyDataToPass() {
            ArrayList<DummyParentDataItem> arrDummyData = new ArrayList<>();
            ArrayList<DummyChildDataItem> childDataItems;
            /////////
            childDataItems = new ArrayList<>();
            childDataItems.add(new DummyChildDataItem("welcome to our app"));
            childDataItems.add(new DummyChildDataItem("Log in to the Campus App\n" +
                    "Log in to the app using your campus username and password . You use this username and password to log on to campus mail, library computers, to pay your fees, etc. \n" +
                    "\n" +
                    "If you don't know your username, or don't remember your password, contact campus IT. \n" +
                    "\n" +
                    "Problems logging in? \n" +
                    "\n" +
                    "If you can't log in to the campus app, first update the app and try again.\n" +
                    "\n" +
                    "If that doesn't fix it, log in to a university computer, such as:\n" +
                    "\n" +
                    "Campus mail\n" +
                    "Did any of those work?\n" +
                    "\n" +
                    "\"NO, nothing works:\" Contact campus IT to fix your login.\n" +
                    "\"YES, I can log in to other university services:\" You might need technical support. Keep reading.\n" +
                    " Send us: \n" +
                    "\n" +
                    "Your student ID #\n" +
                    "Your full name and email address\n" +
                    "Any error messages, blank screens, or problems in the app\n" +
                    "Don't send your password.\n" +
                    "\n" +
                    "Please submit a request at HenzoTech Help Center.\n" +
                    "\n" +
                    "Screenshots\n" +
                    "\n" +
                    "To take a screenshot in the app (Android):\n" +
                    "\n" +
                    "Press and hold the Power and Volume-down buttons\n" +
                    "The laikipia Univisity App is developed and maintained by HenzoTech" +
                    " If you have questions about your school, including IT-related questions (such as password reset, login issue, email concerns, etc) please check your school website or do a Google search to find contact information for your relevant department on your school.\n" +
                    "\n" +
                    "If you have any technical questions, concerns, or feedback about this app in particular, you can contact HenzoTech Support by submitting a request."));
            arrDummyData.add(new DummyParentDataItem("Getting Started", childDataItems));
            /////////
            childDataItems = new ArrayList<>();
            childDataItems.add(new DummyChildDataItem("It is important not to turn notifications off completely, so that you can still receive urgent and emergency notifications from your school. For more information, see Turn App Notifications On."));
            childDataItems.add(new DummyChildDataItem("Turn App Notifications On\n" +
                    "You can get important messages from your school even if the app is closed. \n" +
                    "\n" +
                    "Log in to your campus app and tap 15.png (bottom right).\n" +
                    "Tap 16.png(top right), then tap \"Notifications.\"\n" +
                    "Make sure the \"App Notifications\" slider is on.\n" +
                    "To receive campus announcements, make sure the \"Campus Announcements\" slider is on.\n" +
                    "Make sure to turn notifications on in your phone.\n" +
                    "\n" +
                    "Here are the steps (Android):\n" +
                    "\n" +
                    "Go to your phone's Settings app.\n" +
                    "Tap \"Notifications\" then select the campus app.\"\n" +
                    "Turn on notifications.\n" +
                    "Getting too many notifications?"));
            childDataItems.add(new DummyChildDataItem("You can receive emergency notifications even if your campus app is closed.  For more information, see \"Turn App Notifications On.\"Receive Campus Announcements\n" +
                    "Your campus app lets you receive important announcements, including emergency campus notifications.  You can find out immediately about school closures, weather warnings, and last-minute changes.\n" +
                    "\n" +
                    "To view your campus announcements:\n" +
                    "\n" +
                    "Log in to your campus app.\n" +
                    "Click 12.png (second last tab, bottom).\n" +
                    "Your announcements are shown with the newest on top. \n" +
                    "\n" +
                    "You can receive emergency notifications even if your campus app is closed.  For more information, see \"Turn App Notifications On.\""));
            arrDummyData.add(new DummyParentDataItem("Get Notified", childDataItems));
            /////////
            childDataItems = new ArrayList<>();
            childDataItems.add(new DummyChildDataItem("My Courses\n" +
                    "Problems?\n" +
                    "\n" +
                    "If you have problems viewing your courses, send us a message. Send us: \n" +
                    "\n" +
                    "Your student ID #\n" +
                    "Your full name and email address\n" +
                    "Any error messages, blank screens, or problems in the app\n" +
                    "The courses you expect to see, \n" +
                    "Don't send your password.\n" +
                    "\n" +
                    "Please submit a request at Henzotech Help Center.\n" +
                    "\n" +
                    "Screenshots\n" +
                    "\n" +
                    "To take a screenshot in the app (Android):\n" +
                    "\n" +
                    "Press and hold the Power and Volume-down buttons"));
            childDataItems.add(new DummyChildDataItem(" Exams\n" +
                    "Problems?\n" +
                    "\n" +
                    "If you have problems with exams, send us a message. Send us: \n" +
                    "\n" +
                    "Your student ID #\n" +
                    "Your full name and email address\n" +
                    "The name of the course\n" +
                    "The date and time of the exam\n" +
                    "Any error messages, blank screens, or problems in the app\n" +
                    "\n" +
                    "Don't send your password.\n" +
                    "\n" +
                    "Please submit a request at Henzotech help  Center.\n" +
                    "\n" +
                    "Screenshots\n" +
                    "\n" +
                    "To take a screenshot in the app (Android):\n" +
                    "\n" +
                    "Press and hold the Power and Volume-down buttons\n" +
                    " "));
            childDataItems.add(new DummyChildDataItem(" Course Announcements\n" +
                    "Problems?\n" +
                    "\n" +
                    "If you have problems getting a course announcement, send us a message.  \n" +
                    "\n" +
                    "Send us:Your student ID #\n" +
                    "Your full name and email address\n" +
                    "The date and time of the notification\n" +
                    "Don't send your password.\n" +
                    "\n" +
                    "Please submit a request at HenzoTech Help Center.\n" +
                    "\n" +
                    "You can fine-tune your notifications settings: Getting Too Many Notifications?\n" +
                    "\n" +
                    "Screenshots\n" +
                    "\n" +
                    "To take a screenshot in the app (Android):\n" +
                    "\n" +
                    "Press and hold the Power and Volume-down buttons\n" +
                    " "));
            childDataItems.add(new DummyChildDataItem("To view the assignments for a particular course, tap on the course from the list. You will be directed to a page with course information, assignments and grades where you can view the assignments for the selected course.View Course Assignments\n" +
                    "Your campus app lets you view assignments for the courses you are enrolled in. You can view assignment details and deadlines as well as past assignments.\n" +
                    "\n" +
                    "To view your course assignments:\n" +
                    "\n" +
                    "Log in to your campus app using your campus username and password.\n" +
                    "Tap 15.png (\"Profile\"- bottom row, last tab), then tap \"My courses.\"\n" +
                    "You can tap \"Assignments\" at top of the screen to view assignments for all courses.\n" +
                    "To view the assignments for a particular course, tap on the course from the list. You will be directed to a page with course information, assignments and grades where you can view the assignments for the selected course."));
            arrDummyData.add(new DummyParentDataItem("My Courses", childDataItems));
            /////////
            childDataItems = new ArrayList<>();
            childDataItems.add(new DummyChildDataItem("D Child 1"));
            arrDummyData.add(new DummyParentDataItem("Events", childDataItems));
            /////////
            childDataItems = new ArrayList<>();
            arrDummyData.add(new DummyParentDataItem("General", childDataItems));
            /////////
            childDataItems = new ArrayList<>();
            childDataItems.add(new DummyChildDataItem("F Child 1"));
            childDataItems.add(new DummyChildDataItem("F Child 2"));
            arrDummyData.add(new DummyParentDataItem("Calender", childDataItems));
            /////////
            childDataItems = new ArrayList<>();
            childDataItems.add(new DummyChildDataItem("G Child 1"));
            childDataItems.add(new DummyChildDataItem("G Child 2"));
            childDataItems.add(new DummyChildDataItem("G Child 3"));
            childDataItems.add(new DummyChildDataItem("G Child 4"));
            arrDummyData.add(new DummyParentDataItem("Clubs & Groups", childDataItems));
            /////////
            childDataItems = new ArrayList<>();
            childDataItems.add(new DummyChildDataItem("H Child 1"));
            childDataItems.add(new DummyChildDataItem("H Child 2"));
            childDataItems.add(new DummyChildDataItem("H Child 3"));
            childDataItems.add(new DummyChildDataItem("H Child 4"));
            childDataItems.add(new DummyChildDataItem("H Child 5"));
            childDataItems.add(new DummyChildDataItem("H Child 6"));
            childDataItems.add(new DummyChildDataItem("H Child 7"));
            childDataItems.add(new DummyChildDataItem("H Child 8"));
            arrDummyData.add(new DummyParentDataItem("Community", childDataItems));
            /////////
            childDataItems = new ArrayList<>();
            childDataItems.add(new DummyChildDataItem("I Child 1"));
            arrDummyData.add(new DummyParentDataItem("My Friends", childDataItems));
            /////////
            childDataItems = new ArrayList<>();

            childDataItems.add(new DummyChildDataItem("kindly contact us by navigating to menu in navigation drawer and let us know how are going to help you"));
            arrDummyData.add(new DummyParentDataItem("Contact Us", childDataItems));

            /////////
            childDataItems = new ArrayList<>();
            childDataItems.add(new DummyChildDataItem(" This is the End"));
            arrDummyData.add(new DummyParentDataItem("End", childDataItems));
            ////////
            return arrDummyData;
        }
    }
