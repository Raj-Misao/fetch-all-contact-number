package misao.fetchallphonenumber;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Sonu on 2/1/2017.
 */

public class ContanctAdapter extends ArrayAdapter<ContactDetail> {

    private Activity activity;
    private List<ContactDetail> items;
    private  int row;
    private ContactDetail objDetail;

    public ContanctAdapter(Activity act, int row, List<ContactDetail> items)
    {
        super(act, row, items);
        this.activity = act;
        this.row = row;
        this.items = items;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        ViewHolder holder;

        if(view == null)
        {
            LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(row,null);
            holder = new ViewHolder();
            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)view.getTag();
        }

        if((items == null) || ((position+1)>items.size()))
        {
            return view;
        }

        objDetail = items.get(position);

        holder.tvname = (TextView)view.findViewById(R.id.tvname);
        holder.tvphone = (TextView)view.findViewById(R.id.tvnumber);

        if(holder.tvname != null && objDetail.getName() != null && objDetail.getName().trim().length() > 0)
        {
            holder.tvname.setText(Html.fromHtml(objDetail.getName()));
        }

        if(holder.tvphone != null && objDetail.getPhoneNo() != null && objDetail.getPhoneNo().trim().length()>0)
        {
            holder.tvphone.setText(Html.fromHtml(objDetail.getPhoneNo()));
        }

        return view;
    }

    class ViewHolder{
        public TextView tvname, tvphone;
    }
}
