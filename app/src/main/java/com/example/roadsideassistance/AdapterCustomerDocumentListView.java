package com.example.roadsideassistance;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class AdapterCustomerDocumentListView extends ArrayAdapter<Document> {


    private List<Document> documentList;

    private Context context;
    public AdapterCustomerDocumentListView(List<Document> documentList, Context context){

        super(context,R.layout.custom_customer_document_listview,documentList);
        this.context = context;
        this.documentList = documentList;
    }


    @Override
    public View getView(int position,View convertView,ViewGroup parent) {

        View ress = convertView;
        AdapterCustomerDocumentListView.ViewHolder viewHolder = null;

        if(ress == null){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            ress = layoutInflater.inflate(R.layout.custom_customer_document_listview,null,true );
            viewHolder = new AdapterCustomerDocumentListView.ViewHolder(ress);
            ress.setTag(viewHolder);
        }else{

            viewHolder = (AdapterCustomerDocumentListView.ViewHolder) ress.getTag();
        }

        Document document = documentList.get(position);

        viewHolder.DocumentType.setText(document.getDocumentType());
        viewHolder.DocumentNumber.setText(document.getDocumentNumber());
        Glide.with(context)
                .load(document.getDocumentFile())
                .into(viewHolder.DocumentImage);

       return ress;
    }

    static class ViewHolder
    {
        TextView DocumentNumber;
        TextView DocumentType;
        ImageView DocumentImage;

        ViewHolder(View v){
            DocumentType =v.findViewById(R.id.custdocumentName);
            DocumentNumber=v.findViewById(R.id.custdocumentNumber);
            DocumentImage = v.findViewById(R.id.custdocumentImg);

        }

    }



}
