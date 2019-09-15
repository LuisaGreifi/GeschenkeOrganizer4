package com.example.geschenkeorganizer.PersonsFile;
// eigener Adapter, der die Daten der Person und des Events den passenden Views zuweist

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
import com.example.geschenkeorganizer.database.representations.PersonEventRepresentation;

import java.util.List;

public class PersonEventListAdapter extends RecyclerView.Adapter<PersonEventListAdapter.PersonEventViewHolder>  {
    class PersonEventViewHolder extends RecyclerView.ViewHolder {
        private final TextView personFirstNameView;
        private final TextView personLastNameView;
        private final TextView eventNameView;
        private final TextView eventDateView;

        private PersonEventViewHolder(View itemView) {
            super(itemView);
            personFirstNameView = itemView.findViewById(R.id.personFirstNameItem);
            personLastNameView = itemView.findViewById(R.id.personLastNameItem);
            eventNameView = itemView.findViewById(R.id.eventNameItem);
            eventDateView = itemView.findViewById(R.id.eventDateItem);

            /**Aboyi, P. (07.08.2019).
             * How I usually code RecyclerView adapter class.
             * Retrieved from: https://medium.com/@apeapius/how-i-usually-code-recyclerview-adapter-class-65e30bcf30f.
             * Erstellung onClickListener für einzelnes Item */
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        String personFirstNameString = personFirstNameView.getText().toString();
                        String personLastNameString = personLastNameView.getText().toString();
                        String eventNameString = eventNameView.getText().toString();
                        String eventDateString = eventDateView.getText().toString();

                        listener.onPersonEventItemClicked(personFirstNameString, personLastNameString, eventNameString, eventDateString);
                    }
                }
            });
        }
    }

    private final LayoutInflater mInflater;
    private List<PersonEventRepresentation> personEvents;

    private PersonEventListClickListener listener;

    public PersonEventListAdapter(Context context) {

        mInflater = LayoutInflater.from(context);

        /**Cabezas. (19.05.2016).
         * Listener from fragment to activity.
         * Retrieved from: //thedeveloperworldisyours.com/android/listener-from-fragment-to-activity/.
         * Kontext zur Initialisierung des listeners übergeben */
        listener = (PersonEventListClickListener) context;
    }

    @Override
    public PersonEventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_person_event_item, parent, false);
        return new PersonEventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PersonEventViewHolder holder, int position) {
        if (personEvents != null) {
            PersonEventRepresentation current = personEvents.get(position);

            holder.personFirstNameView.setText(current.getFirstName());
            holder.personLastNameView.setText(current.getLastName());
            holder.eventNameView.setText(current.getEventName());
            holder.eventDateView.setText(eventDateToString(current.getEventDate()));
        } else {
            holder.personFirstNameView.setText("Keine Person vorhanden");
        }
    }

    private String eventDateToString(int eventDate){
        String result = "";

        // das EventDate wurde nicht initial auf 0 gesetzt (in Geschenk hinzufügen Dialog) und wird deswegen im passenden Format angezeigt
        if(eventDate != 0){
            /**int in String umwandeln. (09.11.2015).
             * Retrieved from: http://javatricks.de/tricks/int-in-string-umwandeln
             * String in Int umwandeln*/
            String eventDateString = Integer.toString(eventDate);

            //Fall: vorangehende 0, die automatisch gelöscht wird; wieder hinzufügen, damit Datum richtig angezeigt werden kann
            if(eventDateString.length() == 3){
                eventDateString = "0" + eventDateString;
            }

            /**Pankaj. (n.d.).
             * Java Remove Character from String.
             * Retrieved from: https://www.journaldev.com/18361/java-remove-character-string.
             * String kürzen*/
            String eventDateDay = eventDateString.substring(0, 2);
            String eventDateMonth = eventDateString.substring(2, 4);
            result = eventDateDay + "." + eventDateMonth;
        }
        return result;
    }


    public void setPersonEvents(List<PersonEventRepresentation> mPersonEvents){
        personEvents = mPersonEvents;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (personEvents != null)
            return personEvents.size();
        else return 0;
    }
}
