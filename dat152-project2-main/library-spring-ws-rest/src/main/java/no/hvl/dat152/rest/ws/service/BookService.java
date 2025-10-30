/**
 * 
 */
package no.hvl.dat152.rest.ws.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.BookNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UpdateBookFailedException;
import no.hvl.dat152.rest.ws.model.Author;
import no.hvl.dat152.rest.ws.model.Book;
import no.hvl.dat152.rest.ws.repository.BookRepository;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author tdoy
 */
@Service
public class BookService {

	@Autowired
	private BookRepository bookRepository;
	
	
	public Book saveBook(Book book) {
		
		return bookRepository.save(book);
		
	}
	
	public List<Book> findAll(){
		
		return (List<Book>) bookRepository.findAll();
		
	}
	
	
	public Book findByISBN(String isbn) throws BookNotFoundException {
		
		Book book = bookRepository.findByIsbn(isbn)
				.orElseThrow(() -> new BookNotFoundException("Book with isbn = "+isbn+" not found!"));
		
		return book;
	}
	
	// TODO public Book updateBook(Book book, String isbn)
	public Book updateBook(Book book, String isbn) throws BookNotFoundException {
		Book existing = findByISBN(isbn);

		existing.setTitle(book.getTitle());
		existing.setAuthors(book.getAuthors());

		return bookRepository.save(existing);
	}
	
	// TODO public List<Book> findAllPaginate(Pageable page)
	public List<Book> findAllPaginate(Pageable page) {
		Page<Book> pagedBooks = bookRepository.findAll(page);
		return pagedBooks.getContent();
	}
	
	// TODO public Set<Author> findAuthorsOfBookByISBN(String isbn)
	public Set<Author> findAuthorsOfBookByISBN(String isbn) throws BookNotFoundException {
		Book book = findByISBN(isbn);
		return book.getAuthors();
	}
	
	// TODO public void deleteById(long id)
	public void deleteBookById(long id) {
		Book book = bookRepository.findById(id);
		bookRepository.delete(book);
	}
	
	// TODO public void deleteByISBN(String isbn)
	public void deleteBookByISBN(String isbn) throws BookNotFoundException {
		Book book = findByISBN(isbn);
		bookRepository.delete(book);
	}
	
}
