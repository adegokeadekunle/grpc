package com.adekunle;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.ArrayList;
import java.util.List;

@GrpcService
public class BookAuthorServerService extends BookAuthorServiceGrpc.BookAuthorServiceImplBase {

    @Override
    public void getAuthor(Author request, StreamObserver<Author> responseObserver) {
        TempDb.getAuthorsFromTempDb()
                .stream()
                .filter(author -> author.getAuthorId() == request.getAuthorId())
                .findFirst()
                .ifPresent(responseObserver::onNext);
        responseObserver.onError(new Throwable("User not found")); // in a case of exception when user is not found
        responseObserver.onCompleted();
        ;
    }

    @Override
    public void getBooksByAuthor(Author request, StreamObserver<Book> responseObserver) {
        TempDb.getBooksFromTempDb()
                .stream()
                .filter(book -> book.getAuthorId() == request.getAuthorId())
                .forEach(responseObserver::onNext);
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<Book> getExpensiveMostBook(StreamObserver<Book> responseObserver) {
        return new StreamObserver<Book>() {

            Book mostExpensiveBook = null;
            float priceTracer = 0;

            //            this will consume all the book
            @Override
            public void onNext(Book book) {
                if (book.getPrice() > priceTracer) {
                    priceTracer = book.getPrice();
                    mostExpensiveBook = book;
                }
            }

            @Override
            public void onError(Throwable throwable) {
                responseObserver.onError(throwable);
            }

            @Override
            public void onCompleted() {
                //this check
                responseObserver.onNext(mostExpensiveBook);
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<Book> getBookByAuthorGenders(StreamObserver<Book> responseObserver) {
        return new StreamObserver<Book>() {
            List<Book> bookList = new ArrayList<Book>();

            @Override
            public void onNext(Book book) {
                TempDb.getBooksFromTempDb()
                        .stream()
                        .filter(booksFromDb -> book.getAuthorId() == booksFromDb.getAuthorId())
                        .forEach(bookList::add);

            }

            @Override
            public void onError(Throwable throwable) {
                responseObserver.onError(throwable);
            }

            @Override
            public void onCompleted() {
                bookList.forEach(responseObserver::onNext);
                responseObserver.onCompleted();
            }
        };
    }
}
