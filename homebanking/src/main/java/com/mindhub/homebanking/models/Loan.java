package com.mindhub.homebanking.models;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Loan {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO, generator="native")
   @GenericGenerator(name="native", strategy="native")
   private long id;
   String name;
   private double maxAmount;

   @ElementCollection
   @Column(name = "payment")
   private List<Integer> payments= new ArrayList<>();

   public Loan(){}

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }
}
