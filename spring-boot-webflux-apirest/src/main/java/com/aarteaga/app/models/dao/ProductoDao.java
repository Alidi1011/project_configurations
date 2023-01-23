package com.aarteaga.app.models.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.aarteaga.app.models.documents.Producto;

public interface ProductoDao extends ReactiveMongoRepository<Producto, String>{

}
