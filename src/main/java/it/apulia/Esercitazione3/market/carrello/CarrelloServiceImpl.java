package it.apulia.Esercitazione3.market.carrello;

import it.apulia.Esercitazione3.market.cliente.Cliente;
import it.apulia.Esercitazione3.market.cliente.ClienteRepo;
import it.apulia.Esercitazione3.market.errors.MyNotAcceptableException;
import it.apulia.Esercitazione3.market.prodotti.ProductsRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class CarrelloServiceImpl implements CarrelloService{
    private final CarrelloRepo carrelloRepo;
    private final ProductsRepo productsRepo;
    private final ClienteRepo clienteRepo;

    private Integer counter =0;// riferito ai Carrelli

    @Override
    public Carrello addCarrello(NotaSpesa notaSpesa, String email) {
        this.counter++;
        Carrello temp = new Carrello();
        temp.set_id(counter);
        List<ProdottoSpesa> listaspesa = notaSpesa.getListaspesa();
        //scorro la lista ed effettuo il calcolo dei parziali
        listaspesa.forEach(prodottoinlista -> {
            Double prezzo = productsRepo.findByNome(prodottoinlista.getNome()).getPrezzo();
            VoceScontrino voceScontrino = new VoceScontrino(prodottoinlista.getNome(), prodottoinlista.getQuantity(),
                    Double.valueOf(prodottoinlista.getQuantity().toString())*prezzo);
            temp.getListaspesa().add(voceScontrino);
        });
        //adesso devo sommare i parziali
        Double temptot = 0.00;
        //https://www.baeldung.com/java-stream-sum
        temptot = temp.getListaspesa().stream().map(x -> x.getSubtot()).reduce(0.00,Double::sum);
        log.info("Il totale ottenuto è {}", temptot.toString());
        temp.setTotale(temptot);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/market/carrelli/"+counter).toUriString());
        temp.setSelfUrl(uri.toString());
        //teoricamente si dovrebbe fare il check se esiste il carrello
        Cliente cliente = clienteRepo.findById(email).get();
        Double delta = cliente.getBudget() - temptot;
        if(delta >= 0){
            carrelloRepo.save(temp);
            cliente.setBudget(delta);
            clienteRepo.save(cliente);
            log.info("L'utente {} ha un budget rimanente pari a {}€",email,delta);
        }
        else{
            this.counter--;
            log.warn("L'utente non ha abbastanza soldi per effettuare la spesa");
            throw new MyNotAcceptableException("Al cliente mancano "+(delta*-1.00)+"€ per poter concludere l'acquisto");
        }

        return temp;
    }

    @Override
    public Carrello getCarrellobyId(Integer id) {
        return carrelloRepo.findById(id).get();
    }

    @Override
    public List<Carrello> getAllCarrelli() {
        return carrelloRepo.findAll();
    }

    @Override
    public RicercaCarrello findCarrelliByAnno(Integer anno) {
/*
        Date temp1 = new Date(anno+1,1,1);
        Date temp2 = new Date(anno-1,1,1);

        List<Carrello> temp = carrelloRepo.findByDatascontrinoGreaterThanAndLessThan(temp2,temp1);*/
        List<Carrello> temp = carrelloRepo.findByDatascontrino(anno.toString()+"$");
        Double tot = temp.stream().map(x -> x.getTotale()).reduce(0.00,Double::sum);
        RicercaCarrello tempresult = new RicercaCarrello(anno,temp,tot);
        return tempresult;

    }

    @Override
    public RicercaCarrello findCarrelliByDataAndTotaleRange(Integer anno, Double min, Double max) {
        List<Carrello> temp = carrelloRepo.findByDataAndTotaleRange(anno.toString()+"$", min, max);
        Double tot = temp.stream().map(x -> x.getTotale()).reduce(0.00,Double::sum);
        RicercaCarrello tempresult = new RicercaCarrello(anno,temp,tot);
        return tempresult;
    }

}
