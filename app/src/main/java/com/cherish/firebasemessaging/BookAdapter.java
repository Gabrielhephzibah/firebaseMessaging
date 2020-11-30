package com.cherish.firebasemessaging;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.squareup.picasso.Picasso;

public class BookAdapter extends FirestorePagingAdapter<Book, BookAdapter.BookViewHolder> {
    private Context context;
    private SwipeRefreshLayout swipeRefreshLayout;


    public BookAdapter(@NonNull FirestorePagingOptions<Book> options, Context context, SwipeRefreshLayout swipeRefreshLayout) {
        super(options);
        this.context = context;
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    @Override
    protected void onBindViewHolder(@NonNull BookViewHolder holder, int position, @NonNull Book model) {
        holder.bookTitle.setText(model.getBookName());
        holder.bookDescp.setText(model.getBookDescription());
        holder.bookcategory.setText(model.getBookCategory());
        Picasso.get().load(model.getBookImage()).into(holder.bookImage);


    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item_layout, parent, false);
        return new BookViewHolder(view);
    }

    public  class  BookViewHolder extends RecyclerView.ViewHolder{
        TextView bookTitle, bookDescp, bookcategory;
        ImageView bookImage;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            bookTitle = itemView.findViewById(R.id.bookTitle);
            bookDescp = itemView.findViewById(R.id.bookDescription);
            bookcategory = itemView.findViewById(R.id.bookCategory);
            bookImage = itemView.findViewById(R.id.bookImage);

        }
    }

    @Override
    protected void onLoadingStateChanged(@NonNull LoadingState state) {
        switch (state){
            case ERROR:
                Toast.makeText(context, "ERROR", Toast.LENGTH_LONG).show();
                break;
            case LOADED:
            case FINISHED:
                swipeRefreshLayout.setRefreshing(false);
                break;
            case LOADING_MORE:
            case LOADING_INITIAL:
                swipeRefreshLayout.setRefreshing(true);
                break;

        }
        super.onLoadingStateChanged(state);
    }
}
