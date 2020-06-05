package com.example.group21;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class AdapterSynagogues extends RecyclerView.Adapter<AdapterSynagogues.HolderSynagoguesGabay> implements Filterable {

    private Context context;
    public ArrayList<ModelShowSynagogues> synagoguesArrayList, filterList;
    private FilterSynagogues filterSynagogues;

    public AdapterSynagogues(Context context, ArrayList<ModelShowSynagogues> synagoguesArrayList) {
        this.context = context;
        this.synagoguesArrayList = synagoguesArrayList;
        this.filterList = synagoguesArrayList;
    }

    @NonNull
    @Override
    public HolderSynagoguesGabay onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.row_synagogues_gabay, parent, false);
        return new HolderSynagoguesGabay(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderSynagoguesGabay holder, int position) {
        //get data
        ModelShowSynagogues modelShowSynagogues = synagoguesArrayList.get(position);
        String SynId = modelShowSynagogues.getSynId();
        String uid = modelShowSynagogues.getUid();
        String negishutWomens = modelShowSynagogues.getNegishut_nashim();
        String negishutNehim = modelShowSynagogues.getNegishut_nehim();
        String negishutAvailable = modelShowSynagogues.getNegishutAvailable();
        String negishutNote = modelShowSynagogues.getNegishotNote();
        String NameSyn = modelShowSynagogues.getSynName();
        String city = modelShowSynagogues.getCity();
        String country = modelShowSynagogues.getCountry();
        String fullAddress = modelShowSynagogues.getFullAddress();
        String category = modelShowSynagogues.getCategory();
        String shacharit = modelShowSynagogues.getShacharit();
        String minha = modelShowSynagogues.getMinha();
        String arvit = modelShowSynagogues.getArvit();
        String events = modelShowSynagogues.getEvents();
        String synImage = modelShowSynagogues.getSynImage();
        String timestamp = modelShowSynagogues.getTimestamp();

        //set data
        holder.nameSynRow.setText(NameSyn);
        holder.addressSynRow.setText(fullAddress);
        holder.negishutNote.setText("יש נגישות");

        if(negishutAvailable.equals("true")){
            holder.negishutNote.setVisibility(View.VISIBLE);

        }else {
            holder.negishutNote.setVisibility(View.GONE);


        }

        try{
            Picasso.get().load(synImage).placeholder(R.drawable.icons8_synagogue_40).into(holder.synagoguesIcon);

        }catch (Exception e){
            holder.synagoguesIcon.setImageResource(R.drawable.icons8_synagogue_40);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handle item clicks, show syn info
                detailsButtomSheet(modelShowSynagogues); //contains details of clicked synagouges
            }
        });

    }

    private void detailsButtomSheet(ModelShowSynagogues modelShowSynagogues) {
        //Bottom sheet
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        //inflate view for botto msheet
        View view = LayoutInflater.from(context).inflate(R.layout.bs_synagogues_gabay, null);
        //set view to bottom sheet
        bottomSheetDialog.setContentView(view);


        //init views of bottom sheet
        ImageButton backBtn_editSyn = view.findViewById(R.id.backBtn_editSynID);
        ImageButton deleteBtn_editSyn = view.findViewById(R.id.deleteBtn_editSynID);
        ImageButton editBtn_editSyn = view.findViewById(R.id.editBtn_editSynID);
        ImageView synImageEdit = view.findViewById(R.id.synImageEdit_ID_editSyn);
        TextView negishutEditSyn = view.findViewById(R.id.negishutEditSynId);
        TextView titleTv = view.findViewById(R.id.titleTv);
        TextView addressTv = view.findViewById(R.id.addressTv);
        TextView categoryTv = view.findViewById(R.id.categoryTv);
        TextView shacharitTv = view.findViewById(R.id.shacharitTv);
        TextView minhaTv = view.findViewById(R.id.minhaTv);
        TextView arvitTv = view.findViewById(R.id.arvitTv);
        TextView eventsTv = view.findViewById(R.id.eventsTv);
        TextView nehimTv = view.findViewById(R.id.nehimTv);
        TextView womenTv = view.findViewById(R.id.womenTv);

        //get data
        final String SynId = modelShowSynagogues.getSynId();
        String uid = modelShowSynagogues.getUid();
        String negishutWomens = modelShowSynagogues.getNegishut_nashim();
        String negishutNehim = modelShowSynagogues.getNegishut_nehim();
        String negishutAvailable = modelShowSynagogues.getNegishutAvailable();
        String negishutNote = modelShowSynagogues.getNegishotNote();
        String NameSyn = modelShowSynagogues.getSynName();
        String fullAddress = modelShowSynagogues.getFullAddress();
        String category = modelShowSynagogues.getCategory();
        String shacharit = modelShowSynagogues.getShacharit();
        String minha = modelShowSynagogues.getMinha();
        String arvit = modelShowSynagogues.getArvit();
        String events = modelShowSynagogues.getEvents();
        String synImage = modelShowSynagogues.getSynImage();
        String timestamp = modelShowSynagogues.getTimestamp();

        //set data
        negishutEditSyn.setText(negishutNote);
        titleTv.setText(NameSyn);
        addressTv.setText(fullAddress);
        categoryTv.setText(category);
        shacharitTv.setText(shacharit);
        minhaTv.setText(minha);
        arvitTv.setText(arvit);
        eventsTv.setText(events);
        nehimTv.setText(negishutNehim);
        womenTv.setText(negishutWomens);

        if(negishutAvailable.equals("true")){
            negishutEditSyn.setVisibility(View.VISIBLE);
            nehimTv.setVisibility(View.VISIBLE);
            womenTv.setVisibility(View.VISIBLE);
        }
        else{
            negishutEditSyn.setVisibility(View.GONE);
            nehimTv.setVisibility(View.GONE);
            womenTv.setVisibility(View.GONE);
        }
        try{
            Picasso.get().load(synImage).placeholder(R.drawable.icons8_synagogue_40).into(synImageEdit);

        }catch (Exception e){
            synImageEdit.setImageResource(R.drawable.icons8_synagogue_40);
        }

        //show dialog
        bottomSheetDialog.show();

        //edit click
        editBtn_editSyn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                //open edit activity, pass id of synagogue
                Intent intent = new Intent(context, EditSynagogueActivity.class);
                intent.putExtra("synId", SynId);
                context.startActivity(intent);
            }
        });

        //delete click
        deleteBtn_editSyn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                //show confirm delete dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("מחיקה")
                        .setMessage("האם אתה בטוח שברצונך למחוק את בית הכנסת " + NameSyn + " ?")
                        .setPositiveButton("כן", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //delete
                                deleteSynagogue(SynId);
                            }
                        })
                        .setNegativeButton("לא", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //cancel, dismiss
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        backBtn_editSyn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

    }

    
    private void deleteSynagogue(String synId) {
        //delete synagogue using its id
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("SynagogueDetails").child(synId).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "מוחק את בית הכנסת ...", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed deleting synagogue
                        Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return synagoguesArrayList.size();
    }

    @Override
    public Filter getFilter() {
        if(filterSynagogues == null){
            filterSynagogues = new FilterSynagogues(this, filterList);
        }
        return filterSynagogues;
    }

    class HolderSynagoguesGabay extends RecyclerView.ViewHolder{

        private ImageView synagoguesIcon;
        private TextView negishutNote, nameSynRow, addressSynRow, womenNegishut, nehimNegishut;

        public HolderSynagoguesGabay(@NonNull View itemView) {
            super(itemView);

            synagoguesIcon = itemView.findViewById(R.id.synagoguesIconID);
            negishutNote = itemView.findViewById(R.id.negishutNoteID);
            nameSynRow = itemView.findViewById(R.id.nameSynRowID);
            addressSynRow = itemView.findViewById(R.id.addressSynRowID);
            womenNegishut = itemView.findViewById(R.id.NegishutAzratNashimAddSynID);
            nehimNegishut = itemView.findViewById(R.id.NegishutNehimAddSynID);
        }
    }
}
