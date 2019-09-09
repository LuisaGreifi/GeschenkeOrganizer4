package com.example.geschenkeorganizer.database;

//todo: Neu
// https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#10

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.geschenkeorganizer.R;

import java.util.List;

public class PresentListAdapter extends RecyclerView.Adapter<PresentListAdapter.PresentViewHolder>  {
    class PresentViewHolder extends RecyclerView.ViewHolder {
        // todo: NEU: angepasst an mich
        private final TextView presentNameView;
        private final TextView personFirstNameView;
        private final TextView personLastNameView;
        private final TextView eventNameView;
        private final TextView priceView;
        private final TextView shopView;
        private final TextView statusView;

        // todo: NEU: angepasst an mich
        private PresentViewHolder(View itemView) {
            super(itemView);
            presentNameView = itemView.findViewById(R.id.presentName);
            personFirstNameView = itemView.findViewById(R.id.personFirstName);
            personLastNameView = itemView.findViewById(R.id.personLastName);
            eventNameView = itemView.findViewById(R.id.eventName);
            priceView = itemView.findViewById(R.id.price);
            shopView = itemView.findViewById(R.id.shop);
            statusView = itemView.findViewById(R.id.status);
        }
    }

    private final LayoutInflater mInflater;
    private List<PresentRepresentation> presents;

    public PresentListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public PresentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new PresentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PresentViewHolder holder, int position) {
        if (presents != null) {
            PresentRepresentation current = presents.get(position);
            // todo: NEU: angepasst an mich
            holder.presentNameView.setText(current.getPresentName());
            holder.personFirstNameView.setText(current.getFirstName());
            holder.personLastNameView.setText(current.getLastName());
            holder.eventNameView.setText(current.getEventName());
            holder.priceView.setText(current.getPrice() + " €");
            holder.shopView.setText(current.getShop());
            holder.statusView.setText(current.getStatus());
        } else {
            // Covers the case of data not being ready yet.
            holder.presentNameView.setText("Kein Geschenk vorhanden");
        }
    }

    public void setPresents(List<PresentRepresentation> mPresents){
        presents = mPresents;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (presents != null)
            return presents.size();
        else return 0;
    }
}
