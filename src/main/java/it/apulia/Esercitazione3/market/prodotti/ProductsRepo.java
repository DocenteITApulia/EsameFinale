package it.apulia.Esercitazione3.market.prodotti;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductsRepo extends MongoRepository<Prodotto,Integer> {

    public Prodotto findByNome(String nome);

    public List<Prodotto> findByNomeStartingWith(String regexp);

    //oppure passando una regex "pura",ad esempio "^A" da quelle che cominciano per A, nota è CASE SENSITIVE
    @Query("{ 'nome' : { $regex: ?0 } }")
    List<Prodotto> findByRegexpName(String regexp); //.latte. dovrebbe funzionare, da testare->test regex https://regex101.com/

    public List<Prodotto> findByPrezzoGreaterThan(Double prezzo);


}
