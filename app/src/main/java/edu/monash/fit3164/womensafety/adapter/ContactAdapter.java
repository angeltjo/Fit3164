package edu.monash.fit3164.womensafety.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.monash.fit3164.womensafety.R;
import edu.monash.fit3164.womensafety.provider.Contacts;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> implements RecyclerViewInterface{

    private final RecyclerViewInterface rInterface;

    Context context;
    ArrayList<Contacts> cList;

    public ContactAdapter(Context context, ArrayList<Contacts> cList,
                          RecyclerViewInterface rInterface){
        this.context = context;
        this.cList = cList;
        this.rInterface = rInterface;
    }

    @NonNull
    @Override
    public ContactAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_contact, parent, false);
        return new ContactAdapter.ContactViewHolder(view, rInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ContactViewHolder holder, int position) {
        Contacts contacts = cList.get(position);
        String fLetter = contacts.firstName.substring(0,1);
        String name = contacts.firstName + " " + contacts.secondName;

        holder.tv_1Letter.setText(fLetter);
        holder.tv_name.setText(name);
        holder.tv_mobile.setText(contacts.mobile);

    }

    @Override
    public int getItemCount() {
        return cList.size();
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onSmsClick(int position) {

    }

    @Override
    public void onPhoneCallClick(int position) {

    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder{

        TextView tv_1Letter, tv_name, tv_mobile;
        ImageButton btn_phoneCall, btn_sms;

        public ContactViewHolder(@NonNull View itemView, RecyclerViewInterface rInterface) {
            super(itemView);
            tv_1Letter = itemView.findViewById(R.id.tv_first_letter);
            tv_name = itemView.findViewById(R.id.tv_contact_name);
            tv_mobile = itemView.findViewById(R.id.tv_contact_mobile);
            btn_phoneCall = itemView.findViewById(R.id.btn_phone_call);
            btn_sms = itemView.findViewById(R.id.btn_sms);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (rInterface != null){
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            rInterface.onItemClick(pos);
                        }
                    }
                }
            });

            btn_sms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (rInterface != null){
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            rInterface.onSmsClick(pos);
                        }
                    }
                }
            });

            btn_phoneCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (rInterface != null){
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            rInterface.onPhoneCallClick(pos);
                        }
                    }
                }
            });
        }
    }
}
