package com.example.sih2020;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.internal.TaskApiCall;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GenerateQRActivity extends AppCompatActivity {

    EditText department,serviceTime,serialNumber;// Serial Number mentioned on Machine
    TextView installationDate;
    ImageView qrcode;

    Button GenerateQR;
    String generationCode; // Our code which is used for generating qrCode.
    Button save;
    OutputStream outputStream;
    int generationCodeValue;

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference generationCodeReference, machineReference;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    StorageReference machineQRCodeRefernce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr);


        serialNumber = (EditText)findViewById(R.id.serialNumber);
        department = findViewById(R.id.department);
        serviceTime = findViewById(R.id.serviceTime);

        installationDate = findViewById(R.id.installationdate);

        qrcode = (ImageView)findViewById(R.id.qrcode);

        save = (Button)findViewById(R.id.save);
        GenerateQR = (Button)findViewById(R.id.generateQRButton);

        firebaseDatabase  = FirebaseDatabase.getInstance();
        generationCodeReference = firebaseDatabase.getReference("generationCode");
        machineReference = firebaseDatabase.getReference("machines");

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();


        // Get initial value of Code Generator on app startup
        generationCodeReference.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                generationCode = dataSnapshot.getValue().toString();
                generationCodeValue = Integer.parseInt(generationCode);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDisplayDate = (TextView) findViewById(R.id.installationdate);

        //Permission
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }

        GenerateQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(generationCode!=null) {

                    generationCode = String.valueOf(generationCodeValue+1);
                    generationCodeReference.setValue(generationCode);

                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();


                    try {
                        BitMatrix bitMatrix = multiFormatWriter.encode(generationCode, BarcodeFormat.QR_CODE, 200, 200);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        qrcode.setImageBitmap(bitmap);

                        uploadQR(bitmap);


                    } catch (WriterException e) {
                        e.printStackTrace();
                    }


                }
                else
                {
                    Toast.makeText(GenerateQRActivity.this,"failed",Toast.LENGTH_SHORT).show();
                }
            }
        });

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        GenerateQRActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                mDisplayDate.setText(date);
            }
        };

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage();
            }
        });
    }


    private void uploadQR(Bitmap bitmap){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        machineQRCodeRefernce = storageReference.child(generationCode+".jpg");

        UploadTask uploadTask = machineQRCodeRefernce.putBytes(data);

        addMachineToDatabase(uploadTask);


    }

    public void addMachineToDatabase(UploadTask uploadTask)
    {
        final Machine machine = new Machine();
        final String serialNo,dept;
        final int servicetime;
        final Date installationdate;


        serialNo = serialNumber.getText().toString();
        dept = department.getText().toString();
        servicetime = Integer.parseInt(serviceTime.getText().toString());
        try {
            installationdate = new SimpleDateFormat("dd/MM/yyyy").parse(installationDate.getText().toString());
            machine.setInstallationDate(installationdate);
        }
        catch (Exception e)
        {

        }

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                Toast.makeText(GenerateQRActivity.this, "HEllo", Toast.LENGTH_SHORT).show();
                // Continue with the task to get the download URL
                return machineQRCodeRefernce.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {

                    machine.setLink(task.getResult().toString());
                    machine.setDepartment(dept);
                    machine.setSerialNo(serialNo);
                    machine.setServiceTime(servicetime);

                    machineReference.child(generationCode).setValue(machine);

                } else {

                }
            }
        });

    }
    private void saveImage() {

        ImageView img = (ImageView) findViewById(R.id.qrcode);
        BitmapDrawable draw = (BitmapDrawable) img.getDrawable();
        Bitmap bitmap = draw.getBitmap();
        FileOutputStream outStream = null;
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + "/QRcode");
        dir.mkdirs();
        File outFile = new File(dir, generationCode +".jpg");
        try {
            outStream = new FileOutputStream(outFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
        Toast.makeText(this, "Image Saved Successfully", Toast.LENGTH_SHORT).show();
        try {
            outStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}























// Some extra code to use further
// 1. Dialog box for QR code
//Starting of dialog box
/*
    public void myAlertbox()
    {
        MyDialog = new Dialog(GenerateQRActivity.this);
        MyDialog.setContentView(R.layout.qrcode_dialog);
        MyDialog.setTitle("QR CODE");
        save = (Button)findViewById(R.id.dialogSave);
        exit = (Button)findViewById(R.id.dialogExit);
        ImageinDialog = (ImageView)findViewById(R.id.ImageInDialog);
        ImageinDialog.setImageDrawable(getResources().getDrawable(R.drawable.bhj));

        save.setEnabled(true);
        exit.setEnabled(true);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.cancel();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GenerateQRActivity.this, "Save your image here", Toast.LENGTH_SHORT).show();
            }
        });

    }
    *///------>Ending


