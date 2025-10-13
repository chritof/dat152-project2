/**
 * 
 */
package no.hvl.dat152.rest.ws.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import no.hvl.dat152.rest.ws.exceptions.AuthorNotFoundException;
import no.hvl.dat152.rest.ws.model.Author;
import no.hvl.dat152.rest.ws.model.Book;
import no.hvl.dat152.rest.ws.service.AuthorService;

/**
 * 
 */
@RestController
@RequestMapping("/elibrary/api/v1")
public class AuthorController {

    @Autowired
    private AuthorService authorService;
	
	// TODO - getAllAuthor (@Mappings, URI, and method)
	@GetMapping("/authors")
    public ResponseEntity<List<Author>> getAllAuthors() {
        List<Author> authors = authorService.findAll();
        return new ResponseEntity<>(authors, HttpStatus.OK);
    }
	// TODO - getAuthor (@Mappings, URI, and method)
	@GetMapping("/authors/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable int id) throws AuthorNotFoundException {
        Author author = authorService.findById(id);
        return new ResponseEntity<>(author, HttpStatus.OK);
    }
	// TODO - getBooksByAuthorId (@Mappings, URI, and method)
    @GetMapping("/authors/{id}/books")
    public ResponseEntity<Set<Book>> getBooksByAuthorId(@PathVariable int id) throws AuthorNotFoundException {
        Author author = authorService.findById(id);
        return new ResponseEntity<>(author.getBooks(), HttpStatus.OK);
    }
	// TODO - createAuthor (@Mappings, URI, and method)
    @PostMapping("/authors")
	public ResponseEntity<Author> createAuthor(@RequestBody Author author) throws AuthorNotFoundException {
       Author author1 = authorService.saveAuthor(author);
       return new ResponseEntity<>(author1, HttpStatus.CREATED);
    }
	// TODO - updateAuthor (@Mappings, URI, and method)
    @PostMapping("/authors/{id}")
    public ResponseEntity<Author> updateAuthor(@PathVariable int id, @RequestBody Author author) throws AuthorNotFoundException {
        Author author1 = authorService.findById(id);
        author1.setFirstname(author.getFirstname());
        author1.setLastname(author.getLastname());
        Author author2 = authorService.saveAuthor(author1);
        return new ResponseEntity<>(author2, HttpStatus.OK);
    }

    //delete Author
    @DeleteMapping("/authors/{id}")
    public ResponseEntity<Void> deleteBookById(@PathVariable int id) throws AuthorNotFoundException {
        authorService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
