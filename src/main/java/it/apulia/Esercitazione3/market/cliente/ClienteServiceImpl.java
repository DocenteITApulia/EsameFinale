package it.apulia.Esercitazione3.market.cliente;

import it.apulia.Esercitazione3.accessManagement.UserService;
import it.apulia.Esercitazione3.accessManagement.model.Role;
import it.apulia.Esercitazione3.accessManagement.model.Utente;
import it.apulia.Esercitazione3.market.errors.MyNotAcceptableException;
import it.apulia.Esercitazione3.market.errors.MyNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
@AllArgsConstructor
public class ClienteServiceImpl implements ClienteService {
    private final ClienteRepo clienteRepo;
    private final UserService userService;

    @Override
    public Cliente getCliente(String email) {
        if(!clienteRepo.existsById(email))
            throw new MyNotFoundException("Il cliente da te cercato non è presente all'interno del db");
        return clienteRepo.findById(email).get();
    }

    @Override
    public List<Cliente> getAllClients() {
        return clienteRepo.findAll();
    }

    @Override
    public Cliente saveCliente(ClienteDTO cliente) {
        if(clienteRepo.existsById(cliente.getEmail()))
            throw new MyNotAcceptableException("L'email da te inserita corrisponde già ad un altro cliente");

        Cliente temp = new Cliente(cliente);
        Utente user = new Utente();
        user.setUsername(cliente.getEmail());
        user.getRoles().add(new Role("ROLE_USER"));
        user.setPassword(cliente.getPassword());
        userService.saveUtente(user);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/clientManagement/" + cliente.getEmail()).toUriString());
        temp.setSelf(uri.toString());
        clienteRepo.save(temp);
        return temp;

    }

    @Override
    public void updateCliente(Cliente cliente) {
        if(!clienteRepo.existsById(cliente.getEmail()))
            throw new MyNotFoundException("Il cliente che vuoi aggiornare non esiste");
        Cliente client = clienteRepo.save(cliente);
    }

    @Override
    public void deleteCliente(String email) {
        clienteRepo.deleteById(email);
    }
}
