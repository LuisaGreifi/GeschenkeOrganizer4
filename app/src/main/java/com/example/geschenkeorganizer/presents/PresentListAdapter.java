package com.example.geschenkeorganizer.presents;
// eigener Adapter, der die Daten des Geschenks den passenden Views zuweist

/**Google Developers Codelabs. (n.d.).
 * Add a RecyclerView.
 * Retrieved from https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#10.
 * grundlegende Erstellung Adapter für RecyclerView*/

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.geschenkeorganizer.R;
import com.example.geschenkeorganizer.database.representations.PresentRepresentation;

import java.util.List;

public class PresentListAdapter extends RecyclerView.Adapter<PresentListAdapter.PresentViewHolder>  {
    class PresentViewHolder extends RecyclerView.ViewHolder {

        private final TextView presentNameView;
        private final TextView personFirstNameView;
        private final TextView personLastNameView;
        private final TextView eventNameView;
        private final TextView priceView;
        private final TextView shopView;
        private final TextView statusView;


        private PresentViewHolder(View itemView) {
            super(itemView);

            presentNameView = itemView.findViewById(R.id.presentName);
            personFirstNameView = itemView.findViewById(R.id.personFirstName);
            personLastNameView = itemView.findViewById(R.id.personLastName);
            eventNameView = itemView.findViewById(R.id.eventName);
            priceView = itemView.findViewById(R.id.price);
            shopView = itemView.findViewById(R.id.shop);
            statusView = itemView.findViewById(R.id.status);

            /**Aboyi, P. (07.08.2019).
             * How I usually code RecyclerView adapter class.
             * Retrieved from: https://medium.com/@apeapius/how-i-usually-code-recyclerview-adapter-class-65e30bcf30f.
             * Erstellung onClickListener für einzelnes Item */
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        String presentNameString = presentNameView.getText().toString();
                        String personFirstNameString = personFirstNameView.getText().toString();
                        String personLastNameString = personLastNameView.getText().toString();
                        String eventNameString = eventNameView.getText().toString();
                        String priceString = priceView.getText().toString();
                        String shopString = shopView.getText().toString();
                        String statusString = statusView.getText().toString();

                        listener.onPresentItemClick(presentNameString, personFirstNameString, personLastNameString, eventNameString, priceString, shopString, statusString);
                    }
                }
            });
        }
    }

    private final LayoutInflater mInflater;
    private List<PresentRepresentation> presents;

    private PresentListClickListener listener;


    public PresentListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);

        /**Cabezas. (19.05.2016).
         * Listener from fragment to activity.
         * Retrieved from: //thedeveloperworldisyours.com/android/listener-from-fragment-to-activity/.
         * Kontext zur Initialisierung des listeners übergeben */
        listener = (PresentListClickListener) context;
        }


    @Override
    public PresentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_present_item, parent, false);

        return new PresentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PresentViewHolder holder, int position) {
        if (presents != null) {
            PresentRepresentation current = presents.get(position);

            holder.presentNameView.setText(current.getPresentName());
            holder.personFirstNameView.setText(current.getFirstName());
            holder.personLastNameView.setText(current.getLastName());
            holder.eventNameView.setText(current.getEventName());
            holder.priceView.setText(current.getPrice() + " €");
            holder.shopView.setText(current.getShop());
            holder.statusView.setText(current.getStatus());
        } else {
            holder.presentNameView.setText("Kein Geschenk vorhanden");
        }
    }

    public void setPresents(List<PresentRepresentation> mPresents){
        presents = mPresents;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (presents != null)
            return presents.size();
        else return 0;
    }
}
