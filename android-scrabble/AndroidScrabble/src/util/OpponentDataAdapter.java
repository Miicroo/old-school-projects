package util;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.scrabble.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Listadapter which makes it possible to view opponentdata in listViews
 *
 */
public class OpponentDataAdapter extends ArrayAdapter<OpponentData> {

        private ArrayList<OpponentData> items;
        private Context context = null;

        public OpponentDataAdapter(Context context, int textViewResourceId, ArrayList<OpponentData> items) {
                super(context, textViewResourceId, items);
                this.items = items;
                this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.row, null);
                }
                OpponentData o = items.get(position);
                if (o != null) {
                        TextView tt = (TextView) v.findViewById(R.id.toptext);
                        TextView bt = (TextView) v.findViewById(R.id.bottomtext);
                        if(tt != null){
                              tt.setText("Match against "+o.getUsername()); 
                        }
                        if(bt != null){
                        	  if(o.isOnline()){
                        		  bt.setTextColor(Color.GREEN);
                        	  }
                        	  else{
                        		  bt.setTextColor(Color.RED);
                        	  }
                              bt.setText(o.isOnline() ? "Online" : "Offline");
                        }
                }
                return v;
        }
}