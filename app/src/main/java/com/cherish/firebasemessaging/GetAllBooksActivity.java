package com.cherish.firebasemessaging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class GetAllBooksActivity extends AppCompatActivity {
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    BookAdapter bookAdapter;
    private CollectionReference collectionReference;
    private FirebaseAuth.AuthStateListener firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private List<Book>books = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_all_books);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        recyclerView = findViewById(R.id.recyclerView);

        firebaseFirestore = FirebaseFirestore.getInstance();
        authListernerSetup();

        collectionReference = firebaseFirestore.collection("books");
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               bookAdapter.refresh();
            }
        });
        setUpRecyclerView();


    }

    private  void  authListernerSetup(){
        firebaseAuth = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent intent = new Intent(GetAllBooksActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        };
        FirebaseAuth.getInstance().addAuthStateListener(firebaseAuth);
    }


    private  void  setUpRecyclerView(){
// This is another Way

//        firebaseFirestore.collection("books").get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        if (queryDocumentSnapshots.isEmpty()){
//                            Log.i("EMPTY", "Array is empty");
//
//                        }else{
//                            List<Book>newBooks = queryDocumentSnapshots.toObjects(Book.class);
//                            books.addAll(newBooks);
//
//                            Log.i("Books", books.toString());
//
//
//
//                        }
//                    }
//                });


        Query query = collectionReference.orderBy("bookName",Query.Direction.DESCENDING);
        Log.i("DATA",String.valueOf(query));
        PagedList.Config config  = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(2)
                .setPageSize(10)
                .build();

        FirestorePagingOptions<Book> firestorePagingOptions = new FirestorePagingOptions.Builder<Book>()
                .setLifecycleOwner(this)
                .setQuery(query,config,Book.class)
                .build();

        Log.i("Array", firestorePagingOptions.toString());

        bookAdapter =  new BookAdapter(firestorePagingOptions,this,swipeRefreshLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(bookAdapter);
        bookAdapter.startListening();
        bookAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(firebaseAuth);
        bookAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().addAuthStateListener(firebaseAuth);
        bookAdapter.stopListening();
    }
}
