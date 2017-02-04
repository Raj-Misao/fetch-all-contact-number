package misao.fetchallphonenumber;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.app.AlertDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;

    private List<ContactDetail> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.list);
        listView.setOnItemClickListener(this);

        Cursor phone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);

        while (phone.moveToNext())
        {
            String name = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            String number = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            ContactDetail contactDetail = new ContactDetail();
            contactDetail.setName(name);
            contactDetail.setPhoneNo(number);
            list.add(contactDetail);
        }
        phone.close();

        ContanctAdapter objAdapter = new ContanctAdapter(MainActivity.this, R.layout.all_user, list);
        listView.setAdapter(objAdapter);

        if(list != null && list.size()>0)
        {
            Collections.sort(list, new Comparator<ContactDetail>() {
                @Override
                public int compare(ContactDetail lhs, ContactDetail rhs) {
                    return lhs.getName().compareTo(rhs.getName());
                }
            });

            final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();

            dialog.setTitle("Contact Detail");
            dialog.setMessage(list.size()+"Contact Found");

            dialog.setButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
        else
        {
            showToast("No Coantact Found");
        }
    }

    private void showToast(String msg) {
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            ContactDetail detail = (ContactDetail)listView.getItemAtPosition(position);
        showCallDialog(detail.getName(), detail.getPhoneNo());
    }

    private void showCallDialog(String name, final String phoneNo) {
        AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();
        alert.setTitle("Call ?");
        alert.setMessage("Are you sure want to call" + name +"?");

        alert.setButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alert.setButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String  phoneno = "tel:"+phoneNo;
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(phoneno));

                if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                {
                    return;
                }
                startActivity(intent);
            }
        });

        alert.show();
    }
}
