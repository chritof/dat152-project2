/**
 * 
 */
package no.hvl.dat152.rest.ws.repository;


import no.hvl.dat152.rest.ws.model.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import no.hvl.dat152.rest.ws.model.Author;
import org.springframework.data.repository.query.Param;

/**
 * @author tdoy
 */
public interface AuthorRepository extends CrudRepository<Author, Integer> {

}
