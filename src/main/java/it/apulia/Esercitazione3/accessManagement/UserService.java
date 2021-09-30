package it.apulia.Esercitazione3.accessManagement;


import it.apulia.Esercitazione3.accessManagement.model.Role;
import it.apulia.Esercitazione3.accessManagement.model.Utente;

import java.util.List;

public interface UserService {
    Utente saveUtente(Utente utente);
    Role saveRole(Role role);
    void addRoleToUtente(String username, String roleName);
    Utente getUtente(String username);
    List<Utente> getUtenti();
    void resetAll();

    void deleteAll();
}
