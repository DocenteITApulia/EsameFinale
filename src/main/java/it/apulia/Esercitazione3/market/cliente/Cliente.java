package it.apulia.Esercitazione3.market.cliente;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "client")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
    String nome;
    @Indexed
    String cognome;
    @Id
    String email;
    LocalDate birthdate;
    //da valutare classe innestata
    String address;
    String city;
    Integer cap;
    //valutare libphonenumber google
    String phonenumber;
    String self;
    Double budget;

    public Cliente(String nome, String cognome, String email, LocalDate birthdate, String address, String city, Integer cap, String phonenumber, Double budget) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.birthdate = birthdate;
        this.address = address;
        this.city = city;
        this.cap = cap;
        this.phonenumber = phonenumber;
        this.budget = budget;
    }

    public Cliente(ClienteDTO cliente) {
        this.nome = cliente.getNome();
        this.cognome = cliente.getCognome();
        this.email = cliente.getEmail();
        this.birthdate = cliente.getBirthdate();
        this.address = cliente.getAddress();
        this.city = cliente.getCity();
        this.cap = cliente.getCap();
        this.phonenumber = cliente.getPhonenumber();
        this.budget = cliente.getBudget();
    }
}
