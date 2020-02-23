package com.example.visitid.mainPage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.visitid.R;

import java.util.Arrays;
import java.util.List;

public class customAdapter extends ArrayAdapter<listtem>{

    List<listtem> listEvents;

    Context context;

    int resource;

    public customAdapter(@NonNull Context context, int resource, List<listtem> listEvents) {
        super(context, resource, listEvents);

        this.context = context;
        this.listEvents = listEvents;
        this.resource = resource;

    }
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //we need to get the view of the xml for our list item
        //And for this we need a layoutinflater
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        //getting the view
        View view = layoutInflater.inflate(resource, null, false);

        //getting the view elements of the list from the view
        TextView nameEv = view.findViewById(R.id.nameEvent);
        TextView nameOrg = view.findViewById(R.id.nameOrg);
        TextView datetime = view.findViewById(R.id.datetime);
        TextView location = view.findViewById(R.id.location);
        ImageView imageView = view.findViewById(R.id.imageView);

        //getting the hero of the specified position
        listtem event = listEvents.get(position);

        //adding values to the list item
        nameEv.setText(event.getNameEvent());
        nameOrg.setText(event.getNameOrg());
        datetime.setText(event.getStartTime() + " - " + event.getEndTime());
        location.setText(event.getLocation());


        imageView.setImageBitmap(stringToBitMap(event.getBitm()));


        //adding a click listener to the button to remove item from the list
//        buttonDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //we will call this method to remove the selected value from the list
//                //we are passing the position which is to be removed in the method
//                removeHero(position);
//            }
//        });

        //finally returning the view
        return view;
    }

    public Bitmap stringToBitMap(String encodedString){
        List<String> list = Arrays.asList(encodedString.split(","));
        try {
            byte [] encodeByte= Base64.decode(list.get(1), Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
//    //this method will remove the item from the list
//    private void removeHero(final int position) {
//        //Creating an alert dialog to confirm the deletion
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle("Are you sure you want to delete this?");
//
//        //if the response is positive in the alert
//        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//                //removing the item
//                heroList.remove(position);
//
//                //reloading the list
//                notifyDataSetChanged();
//            }
//        });

//        //if response is negative nothing is being done
//        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });

//        //creating and displaying the alert dialog
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();
//    }
}
