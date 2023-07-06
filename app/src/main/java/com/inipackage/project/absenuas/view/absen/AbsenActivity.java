package com.inipackage.project.absenuas.view.absen;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.exifinterface.media.ExifInterface;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.inipackage.project.absenuas.R;
import com.inipackage.project.absenuas.viewmodel.AbsenViewModel;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class AbsenActivity extends AppCompatActivity {
    private EditText inputLokasi;
    private TextView tvTitle;
    private Toolbar toolbar;
    private Button btnAbsen;
    private EditText inputNama;
    private EditText inputKeterangan;
    private EditText inputTanggal;
    private LinearLayout layoutImage;
    private ImageView imageSelfie;
    private static final int REQ_CAMERA = 101;
    private double strCurrentLatitude = 0.0;
    private double strCurrentLongitude = 0.0;
    private String strFilePath = "";
    private String strLatitude = "0";
    private String strLongitude = "0";
    private File fileDirectory;
    private File imageFilename;
    private ExifInterface exifInterface;
    private String strBase64Photo;
    private String strCurrentLocation;
    private String strTitle;
    private String strTimeStamp;
    private String strImageName;
    private AbsenViewModel absenViewModel;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absen);
        tvTitle = findViewById(R.id.tvTitle);
        inputTanggal = findViewById(R.id.inputTanggal);
        layoutImage = findViewById(R.id.layoutImage);
        imageSelfie = findViewById(R.id.imageSelfie);
        inputLokasi = findViewById(R.id.inputLokasi);
        tvTitle = findViewById(R.id.tvTitle);
        toolbar = findViewById(R.id.toolbar);
        btnAbsen = findViewById(R.id.btnAbsen);
        inputNama = findViewById(R.id.inputNama);
        inputKeterangan = findViewById(R.id.inputKeterangan);

        setInitLayout();
        setCurrentLocation();
        setUploadData();
    }


    private void setCurrentLocation() {
        progressDialog.show();
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            progressDialog.dismiss();
            if (location != null) {
                strCurrentLatitude = location.getLatitude();
                strCurrentLongitude = location.getLongitude();
                Geocoder geocoder = new Geocoder(AbsenActivity.this, Locale.getDefault());
                try {
                    List<android.location.Address> addressList = geocoder.getFromLocation(strCurrentLatitude, strCurrentLongitude, 1);
                    if (addressList != null && addressList.size() > 0) {
                        strCurrentLocation = addressList.get(0).getAddressLine(0);
                        inputLokasi.setText(strCurrentLocation);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                progressDialog.dismiss();
                Toast.makeText(AbsenActivity.this, "Ups, gagal mendapatkan lokasi. Silahkan periksa GPS atau koneksi internet Anda!", Toast.LENGTH_SHORT).show();
                strLatitude = "0";
                strLongitude = "0";
            }
        });
    }

    private void setInitLayout() {
        progressDialog = new ProgressDialog(this);
        strTitle = getIntent().getStringExtra(DATA_TITLE);

        if (strTitle != null) {
            tvTitle.setText(strTitle);
        }

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        absenViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(AbsenViewModel.class);

        inputTanggal.setOnClickListener(view -> {
            final Calendar tanggalAbsen = Calendar.getInstance();
            DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    tanggalAbsen.set(Calendar.YEAR, year);
                    tanggalAbsen.set(Calendar.MONTH, monthOfYear);
                    tanggalAbsen.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm", Locale.getDefault());
                    inputTanggal.setText(simpleDateFormat.format(tanggalAbsen.getTime()));
                }
            };
            new DatePickerDialog(AbsenActivity.this, date, tanggalAbsen.get(Calendar.YEAR),
                    tanggalAbsen.get(Calendar.MONTH), tanggalAbsen.get(Calendar.DAY_OF_MONTH)).show();
        });

        layoutImage.setOnClickListener(view -> {
            Dexter.withContext(AbsenActivity.this)
                    .withPermissions(
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    )
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                try {
                                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(cameraIntent, REQ_CAMERA);
                                } catch (ActivityNotFoundException ex) {
                                    Toast.makeText(AbsenActivity.this, "Aplikasi kamera tidak ditemukan", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CAMERA && resultCode == RESULT_OK) {
            if (strFilePath != null) {
                convertImage(strFilePath);
            }
        }
    }

    private void setUploadData() {
        btnAbsen.setOnClickListener(view -> {
            String strNama = inputNama.getText().toString();
            String strTanggal = inputTanggal.getText().toString();
            String strKeterangan = inputKeterangan.getText().toString();
            if (strNama.isEmpty() || strTanggal.isEmpty() || strKeterangan.isEmpty()) {
                Toast.makeText(AbsenActivity.this, "Data tidak boleh ada yang kosong!", Toast.LENGTH_SHORT).show();
            } else {
                // Lakukan absen tanpa foto jika memenuhi syarat
                absenViewModel.addDataAbsen(strBase64Photo, strNama, strTanggal, strCurrentLocation, strKeterangan, "");
                Toast.makeText(AbsenActivity.this, "Laporan Anda terkirim, tunggu info selanjutnya ya!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + ".jpg";

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = new File(storageDir, imageFileName);
        strFilePath = imageFile.getAbsolutePath();

        return imageFile;
    }
    private void convertImage(String imageFilePath) {
        File imageFile = new File(imageFilePath);
        if (imageFile.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap bitmapImage = BitmapFactory.decodeFile(strFilePath, options);

            try {
                exifInterface = new ExifInterface(imageFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }

            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                matrix.postRotate(90);
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                matrix.postRotate(180);
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                matrix.postRotate(270);
            }

            bitmapImage = Bitmap.createBitmap(bitmapImage, 0, 0, bitmapImage.getWidth(), bitmapImage.getHeight(), matrix, true);

            if (bitmapImage == null) {
                Toast.makeText(AbsenActivity.this, "Ups, foto kamu belum ada!", Toast.LENGTH_LONG).show();
            } else {
                int resizeImage = (int) (bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth()));
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmapImage, 512, resizeImage, true);

                // Simpan gambar ke dalam direktori cache aplikasi
                File cacheDir = getCacheDir();
                File imageCacheFile = new File(cacheDir, "selfie_image.jpg");
                try {
                    FileOutputStream outputStream = new FileOutputStream(imageCacheFile);
                    scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Glide.with(this)
                        .load(scaledBitmap)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.ic_photo_camera)
                        .into(imageSelfie);

                // Ubah strBase64Photo menjadi path gambar yang disimpan di cache
                strBase64Photo = imageCacheFile.getAbsolutePath();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_GRANTED) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static final String DATA_TITLE = "TITLE";
}