package com.example.roadsideassistance;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterCustomerDocumentListView extends ArrayAdapter {


    private String[] DocumentName;
    private String[] DocumentNumber;
    private Integer[] DocumentImage; // Integer using for Document Image
    private  Activity context;

    public AdapterCustomerDocumentListView(Activity context, String[] DocumentName, String[] DocumentNumber,Integer[] DocumentImage){

        super(context,R.layout.custom_customer_document_listview,DocumentName);
        this.context = context;
        this.DocumentName = DocumentName;
        this.DocumentNumber = DocumentNumber;
        this.DocumentImage = DocumentImage;
    }


    @Override
    public View getView(int position,View convertView,ViewGroup parent) {

        View ress = convertView;
        AdapterCustomerDocumentListView.ViewHolder viewHolder = null;

        if(ress == null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            ress = layoutInflater.inflate(R.layout.custom_customer_document_listview,null,true );
            viewHolder = new AdapterCustomerDocumentListView.ViewHolder(ress);
            ress.setTag(viewHolder);
        }else{

            viewHolder = (AdapterCustomerDocumentListView.ViewHolder) ress.getTag();
        }

        viewHolder.DocumentName.setText(DocumentName[position]);
        viewHolder.DocumentNumber.setText(DocumentNumber[position]);
        viewHolder.DocumentImage.setImageResource(DocumentImage[position]);

       return ress;
    }

    static class ViewHolder
    {
        TextView DocumentName;
        TextView DocumentNumber;
        ImageView DocumentImage;

        ViewHolder(View v){
            DocumentName =v.findViewById(R.id.custdocumentName);
            DocumentNumber=v.findViewById(R.id.custdocumentNumber);
            DocumentImage = v.findViewById(R.id.custdocumentImg);

        }

    }



}
