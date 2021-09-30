package it.apulia.Esercitazione3.market.cliente;

import it.apulia.Esercitazione3.accessManagement.UserService;
import it.apulia.Esercitazione3.market.utils.Ricarica;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@AllArgsConstructor
@RequestMapping("/clientManagement")
@RestController
public class ClienteController {
    private final UserService userService;
    private final ClienteService clienteService;

    @GetMapping
    public ResponseEntity<List<Cliente>> getAllClients(){
        return ResponseEntity.ok().body(clienteService.getAllClients());
    }

    @GetMapping("/{email}")
    public ResponseEntity<Cliente> getCliente(@PathVariable String email){
        return ResponseEntity.ok().body(clienteService.getCliente(email));
    }

    @PostMapping
    public ResponseEntity<Cliente> addCliente(@RequestBody ClienteDTO clienteDTO){
        Cliente temp = clienteService.saveCliente(clienteDTO);
        return ResponseEntity.created(URI.create(temp.getSelf())).body(temp);
    }

    @PutMapping("/{email}")
    public ResponseEntity updateCliente(@PathVariable String email, @RequestBody Cliente cliente){
        clienteService.updateCliente(cliente);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{email}")
    public ResponseEntity deleteCliente(@PathVariable String email){
        clienteService.deleteCliente(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{email}/ricarica")
    public ResponseEntity updateBudget(@RequestBody Ricarica ricarica, @PathVariable String email){
        clienteService.updateBudgetCliente(ricarica);
        return ResponseEntity.ok().build();
    }


}
