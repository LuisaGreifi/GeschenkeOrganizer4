package com.example.geschenkeorganizer.database;

//todo: Neu
// https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#10

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.geschenkeorganizer.R;

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

            // todo: NEU
            // https://medium.com/@apeapius/how-i-usually-code-recyclerview-adapter-class-65e30bcf30f
            // onClickListener für einzelnes Item erstellen
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        Log.d("PresentListAdapter", "Listener != null");
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

    //todo: Neu
    //https://medium.com/@apeapius/how-i-usually-code-recyclerview-adapter-class-65e30bcf30f
    //listener Variable in Adapter
    private PersonEventListClickListener listener;

    public PersonEventListAdapter(Context context) {

        mInflater = LayoutInflater.from(context);
        //todo: neu
        // https://thedeveloperworldisyours.com/android/listener-from-fragment-to-activity/
        // Initialisierung listener (Kontext übergeben)
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
            //todo: int kannst du nicht als Text setten --> umwandeln + in schöneres Format!
            holder.eventDateView.setText(eventDateToString(current.getEventDate()));
        } else {
            // Covers the case of data not being ready yet.
            holder.personFirstNameView.setText("Keine Person vorhanden");
        }
    }
    //todo: Neu, selbstgemacht
    private String eventDateToString(int eventDate){
        String result = "";

        // das EventDate wurde nicht initial auf 0 gesetzt (in Geschenk hinzufügen Dialog) und wird deswegen in Format angezeigt
        if(eventDate != 0){
            // http://javatricks.de/tricks/int-in-string-umwandeln
            // String in Int umwandeln
            String eventDateString = Integer.toString(eventDate);
            Log.d("PELA_date", eventDateString);

            //Fall: vorangehende 0, wird weggelöscht --> wieder hinzufügen, damit richtig angezeigt werden kann
            if(eventDateString.length() == 3){
                eventDateString = "0" + eventDateString;
            }

            // https://www.journaldev.com/18361/java-remove-character-string
            // String kürzen
            String eventDateDay = eventDateString.substring(0, 2);
            Log.d("PELA_day", eventDateString);
            String eventDateMonth = eventDateString.substring(2, 4);
            Log.d("PELA_month", eventDateMonth);
            result = eventDateDay + "." + eventDateMonth;

        }

        return result;
    }


    public void setPersonEvents(List<PersonEventRepresentation> mPersonEvents){
        personEvents = mPersonEvents;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (personEvents != null)
            return personEvents.size();
        else return 0;
    }

}
