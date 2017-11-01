package com.example.batut.loginfb;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Acesso extends AppCompatActivity {

    private static int RESULT_LOAD_IMAGE = 1;
    private DatabaseReference mDatabase;
    private TextView nomeEscola;
    private TextView emailEscola;
    private TextView pesquisaEscola;
    private String nome;
    private Button pesquisarEscola;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acesso);


        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        nomeEscola = (TextView)findViewById(R.id.nome_escola);
        emailEscola = (TextView)findViewById(R.id.email_escola);
        pesquisaEscola = (TextView)findViewById(R.id.nome_escola_pesquisa);


        pesquisarEscola = (Button)findViewById(R.id.botao_pesquisar);
        pesquisarEscola.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nome = pesquisaEscola.getText().toString();



                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://mobile-aceite.tcu.gov.br/nossaEscolaRS/rest/escolas/nome/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();



                EscolaService service = retrofit.create(EscolaService.class);
                Call<Escola> call = service.getEscola(nome);

                call.enqueue(new Callback<Escola>() {

                    @Override
                    public void onResponse(Call<Escola> call, retrofit2.Response<Escola> response) {
                        if (response.isSuccessful()) {
                            Escola escola = response.body();

                            String strNomeEscola = "Nome: " + escola.getNome();
                            String strEmailEscola = "Email :" + escola.getEmail();

                            nomeEscola.setText(strNomeEscola);
                            emailEscola.setText(strEmailEscola);
                        }
                    }

                    @Override
                    public void onFailure(Call<Escola> call, Throwable throwable) {
                        Toast.makeText(Acesso.this,
                                "Não foi possível realizar a requisição",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });









        String nome = "Joao";
        String sobrenome = "Das Neves";
        String uid = null;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            uid = user.getUid();
        }

        writeNewUser(uid, nome, sobrenome);




    }

    private void writeNewUser(String userId, String nome, String sobrenome){
        User usuario = new User(nome, sobrenome);

        mDatabase.child("user").child(userId).setValue(usuario);
    }
}
