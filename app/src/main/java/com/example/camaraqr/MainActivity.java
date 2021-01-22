package com.example.camaraqr;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    EditText editTexto;
    Button btnGenerar;
    ImageView imgCode;
    private AdView mAdView;
    final private int REQUEST_CODE_ASK_PERMISSION=111;
    private ZXingScannerView MyScannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        solicitarPermisos();
        iniciarComponentes();
        clickButton();

    }

    private void solicitarPermisos() {
        int permisoCamera= ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA);
        if(permisoCamera!=PackageManager.PERMISSION_GRANTED){
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_ASK_PERMISSION );
            }
        }
    }

    private void clickButton() {
        btnGenerar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generarQr();
            }
        });
    }

    private void generarQr() {
        String texto= editTexto.getText().toString();
        MultiFormatWriter multiFormatWriter =new MultiFormatWriter();
        try {
            BitMatrix bitMatrix=multiFormatWriter.encode(texto, BarcodeFormat.QR_CODE, 2000, 2000);
            BarcodeEncoder barcodeEncoder=new BarcodeEncoder();
            Bitmap bitmap= barcodeEncoder.createBitmap(bitMatrix);
            imgCode.setImageBitmap(bitmap);
        }catch (WriterException e){
            e.printStackTrace();
        }
    }

    private void iniciarComponentes() {
        editTexto=(EditText)findViewById(R.id.txt_texto);
        btnGenerar=(Button)findViewById(R.id.btn_generar);
        imgCode=(ImageView)findViewById(R.id.img_code);
    }


    public void btn_escanear(View v){
        MyScannerView= new ZXingScannerView(this);
        setContentView(MyScannerView);
        MyScannerView.setResultHandler(this);
        MyScannerView.startCamera();
    }

    @Override
    public void handleResult(Result result) {
        Log.v("HandleResult",result.getText());
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Resultado del scanner");
        builder.setMessage(result.getText());
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
      //  MyScannerView.resumeCameraPreview(this);


    }

}