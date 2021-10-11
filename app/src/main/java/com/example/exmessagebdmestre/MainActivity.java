package com.example.exmessagebdmestre;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exmessagebdmestre.model.Mensagem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;

import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;


import java.text.Format;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
private List<Mensagem> mensagemList = new ArrayList<>();
EditText edEnviarMensagens, edtQtdVezes, edUser, edFriend, edMensagensRecebidas;
Button btEnviar, btReceber;
TextView txQtdEnviadas, txQtdRecebidas;
int contador=0;
private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);
        db.getApp();
        carregaWidgets();
        botoes(db);

      }

    private void botoes(FirebaseFirestore db) {
        btEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gravar(db);
            }
        });

        btReceber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consulta(db);
            }
        });

    }

    private void gravar(FirebaseFirestore db) {
        Date dt = new Date();
        Mensagem msg = new Mensagem();
        msg.setData(dt);
        msg.setMensagem(edEnviarMensagens.getText().toString());
        msg.setUser(edUser.getText().toString().trim());

        for (int i = 0; i < Integer.parseInt(edtQtdVezes.getText().toString()); i++) {

            db.collection(msg.getUser().toString())
                    .add(msg)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                            Trace trace = FirebasePerformance.getInstance().newTrace("test_trace");

// Update scenario.
                            trace.putAttribute("experiment", "A");

// Reading scenario.
                            String experimentValue = trace.getAttribute("experiment");

// Delete scenario.
                            trace.removeAttribute("experiment");

// Read attributes.
                            Map<String, String> traceAttributes = trace.getAttributes();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Toast.makeText(MainActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    })
            ;
        }
        //consulta(db);
        Bundle bundle = new Bundle();
        bundle.putString(FirebasePerformance.HttpMethod.CONNECT, "rede");
        mFirebaseAnalytics.logEvent(FirebasePerformance.HttpMethod.POST, bundle);
    }


    private void removerLista(){
        int c=mensagemList.size();
        while (c>0){
            mensagemList.remove(c-1);
            Log.d("teste", String.valueOf(c));
            c--;
        }
    }

    private void consulta(FirebaseFirestore db){
        //db.collection(edFriend.getText().toString())
        //final QuerySnapshot[] querySnapshot = new QuerySnapshot[1];
        //mensagemList.clear();
       //removerLista();
        Trace myTrace = FirebasePerformance.getInstance().newTrace("test_trace");
        myTrace.start();
        contador=0;
        Log.d("teste", edFriend.getText().toString().trim())  ;
        db.collection(edFriend.getText().toString().trim())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                               //Mensagem mensagem = document.toObject(Mensagem.class);
                               Mensagem mensagem = document.toObject(Mensagem.class);
                               mensagemList.add(mensagem);
                               contador += 1;
                               Log.d("teste", document.getId());
                               Log.d("teste", String.valueOf(document.getData()));
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Erro ao pesquisar", Toast.LENGTH_LONG).show();
                            //Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        String textos="";
        Toast.makeText(MainActivity.this, "size: " + String.valueOf(mensagemList.size()), Toast.LENGTH_LONG).show();
        for (int conta=0;conta<=mensagemList.size()-1;conta++){
            //textos= String.valueOf(mensagemList.get(conta).getData().getDate());
            //textos = textos.concat(String.valueOf(mensagemList.get(conta).getData().getHours()));
            textos=textos.concat(mensagemList.get(conta).getMensagem());
            textos=textos.concat("\n");
            //(mensagemList.get(conta).getMensagem());
            Log.d("teste","teste passou");
        }
        edMensagensRecebidas.setText(textos);
        txQtdRecebidas.setText("Qtd. msg recebidas: "+String.valueOf(mensagemList.size()));
        txQtdEnviadas.setText("Qtd. enviadas: "+ String.valueOf(edtQtdVezes.getText()));


        myTrace.stop();
    }



/*
    private String caracteresGerados() {
        StringBuilder caracteres= new StringBuilder();
        for (int i = 0; i < Integer.parseInt(edQtdCarEnviarUsuario2.getText().toString()); i++)
            {
                caracteres.append("a");
            }
        return caracteres.toString();
    }
*/
    private void carregaWidgets() {
        edEnviarMensagens=(EditText) findViewById(R.id.edtMsgEnviar);
        edMensagensRecebidas=(EditText)findViewById(R.id.txtMsgRecebidas);
        edtQtdVezes=(EditText)findViewById(R.id.edtQtdMsgEnviar);
        edUser=(EditText)findViewById(R.id.edtUser);
        edFriend=(EditText)findViewById(R.id.edtFriend);
        btEnviar=(Button)findViewById(R.id.btnEnviar);
        btReceber=(Button)findViewById(R.id.btnReceber);
        txQtdEnviadas=(TextView)findViewById(R.id.txtQtdMensagensEnviadas);
        txQtdRecebidas=(TextView)findViewById(R.id.txtQtdMensagensRecebidas);
    }

    private void iniciarFirebase() {
        //FirebaseApp.initializeApp(MainActivity.this);
        //db = FirebaseFirestore.getInstance(FirebaseApp.initializeApp(MainActivity.this));
         // db = FirebaseFirestore.getInstance();
    }




}