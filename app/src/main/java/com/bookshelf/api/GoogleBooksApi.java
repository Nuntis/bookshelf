package com.bookshelf.api;

import com.bookshelf.model.Books;
import com.example.maxime.bookshelf.Book;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by jolyn on 08/12/2016.
 */

public interface GoogleBooksApi {
    public static final String APIPath = "https://www.googleapis.com/books/v1/";

    @GET("volumes")
    Call<Books> searchByIsbn(@Query("q") String isbn);
}