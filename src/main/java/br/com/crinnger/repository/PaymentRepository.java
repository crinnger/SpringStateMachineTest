package br.com.crinnger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import br.com.crinnger.domain.Payment;

@Repository
public interface PaymentRepository  extends JpaRepository<Payment,Long>{

}
