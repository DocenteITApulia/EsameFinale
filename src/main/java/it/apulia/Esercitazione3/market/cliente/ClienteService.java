package it.apulia.Esercitazione3.market.cliente;

import it.apulia.Esercitazione3.market.utils.Ricarica;

import java.util.List;

public interface ClienteService {
    Cliente getCliente(String email);
    List<Cliente> getAllClients();
    Cliente saveCliente(ClienteDTO cliente);
    void updateCliente(Cliente cliente);
    void deleteCliente(String email);
    void updateBudgetCliente(Ricarica ricarica);
}
