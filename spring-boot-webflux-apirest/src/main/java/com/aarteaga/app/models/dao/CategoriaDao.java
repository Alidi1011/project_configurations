package com.aarteaga.app.models.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.aarteaga.app.models.documents.Categoria;

public interface CategoriaDao extends ReactiveMongoRepository<Categoria, String>{

}
