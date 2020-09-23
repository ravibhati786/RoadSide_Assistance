package com.example.roadsideassistance;

import android.net.Uri;

import java.net.URL;

public class Document {
    String DocumentNumber, DocumentType;
    String  DocumentFile;

    public Document(String documentNumber,String documentType, String documentFile ){
        this.DocumentNumber = documentNumber;
        this.DocumentType = documentType;
        this.DocumentFile = documentFile;

    }

    public String getDocumentNumber(){
        return DocumentNumber;
    }

    public String getDocumentType(){
        return DocumentType;
    }

    public String getDocumentFile(){
        return DocumentFile;
    }


}
