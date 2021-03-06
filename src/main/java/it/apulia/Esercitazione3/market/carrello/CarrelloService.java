package it.apulia.Esercitazione3.market.carrello;

import java.util.List;

public interface CarrelloService {

    public Carrello addCarrello(NotaSpesa notaSpesa, String email);
    public Carrello getCarrellobyId(Integer id);
    public List<Carrello> getAllCarrelli();
    public RicercaCarrello findCarrelliByAnno(Integer anno);
    public RicercaCarrello findCarrelliByDataAndTotaleRange(Integer anno, Double min, Double max);
}
