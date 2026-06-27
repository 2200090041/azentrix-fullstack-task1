package com.project.dailyincome;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository repository;

    public List<Transaction> getAll() {
        return repository.findAll();
    }

    public List<Transaction> getByUser(User user) {
        return repository.findByUser(user);
    }

    // PAGINATION METHOD
    public Page<Transaction> getTransactionsByUser(
            User user,
            Pageable pageable) {

        return repository.findByUser(user, pageable);
    }

    public void save(Transaction transaction) {
        repository.save(transaction);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Transaction getById(Long id) {
        return repository.findById(id).orElse(null);
    }
}