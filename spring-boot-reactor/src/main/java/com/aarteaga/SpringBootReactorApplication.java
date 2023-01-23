package com.aarteaga;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.aarteaga.models.Comentarios;
import com.aarteaga.models.Usuario;
import com.aarteaga.models.UsuarioComentarios;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner{

	private static final Logger log = LoggerFactory.getLogger(SpringBootReactorApplication.class);
			
	public static void main(String[] args) {
		SpringApplication.run(SpringBootReactorApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		pruebasStream2();
	}
	
	public void pruebasStream2() throws Exception {
		
		ArrayList<Integer> precios = new ArrayList<Integer>();
		
		int size = 40;
		for(int i = 0; i < size; i++){
            Double random = Math.random()*100+1;
            precios.add(random.intValue());
        }
		
		//precios.stream().forEach(elemento -> log.info("Elemento: "+ elemento));
			
		int precioTotal1 = precios.stream()
                 .mapToInt(Integer::intValue)
                 .sum();
		 
		int precioTotal2 = precios.stream().mapToInt(precio -> precio.intValue()).sum();
		
		log.info("Precio Total: " + precioTotal1);
		
		int cantidadConDescuento = 89;
		
		Optional<Integer> primerPrecio = precios.stream()
                .filter(precio -> precio.intValue() >= cantidadConDescuento)
                .findFirst();
		
		if(primerPrecio.isPresent()) {
			log.info("Precio: " + primerPrecio.get().toString());
		}
		

	
	}
	
    public void pruebasStream() throws Exception {
		
	 List<String> list = new ArrayList<>();
     list.add("India");
     list.add("US");
     list.add("China");
     list.add("Russia");
     
     Long quantity = list.stream().count();
     log.info("quantity: " + quantity);
     
     Optional<String> optional = Optional.of("Alisson");
     optional.ifPresent(nombre -> log.info(nombre));
		
	}
	
public void ejemploIterable5() throws Exception {
		
		List<Usuario> usuariosList = new ArrayList();
		usuariosList.add(new Usuario("Andres", "Ramirez"));
		usuariosList.add(new Usuario("Pedro", "Gallo"));
		usuariosList.add(new Usuario("Alicia", "Manrique"));
		usuariosList.add(new Usuario("Juan", "Pazos"));
		usuariosList.add(new Usuario("Bruce", "Lee"));
		usuariosList.add(new Usuario("Bruce", "Willi"));
		usuariosList.add(new Usuario("Bruce", "Adams"));
		
		Mono<Usuario> usuarioMono = Flux.fromIterable(usuariosList)
				.filter(usuario -> usuario.getNombre().equalsIgnoreCase("as"))
				.map(usuario -> {
					log.info(usuario.getApellido());
					return usuario;
				})
				.next()
				.defaultIfEmpty(new Usuario("Alisson", "Rosario"));
		
	
		
		log.info("Elemento usuario:");
		usuarioMono.subscribe(texto -> log.info("elemento obtenido:" + texto.getApellido()));
		
		
	}
	public void ejemploDelayElements() {
		Flux<Integer> rangos = Flux.range(1, 12)
				.delayElements(Duration.ofSeconds(1))
				.doOnNext(i -> log.info(i.toString()));
		
		//se ejecuta cada delay en segundo plano
		//rangos.subscribe();
		
		rangos.blockLast();
	}
	
	public void ejemploInterval() {
		Flux<Integer> rangos = Flux.range(1, 12);
		Flux<Long> retraso = Flux.interval(Duration.ofSeconds(1));
		
		rangos.zipWith(retraso, (ra, re) -> ra)
		.doOnNext(i -> log.info(i.toString()))
		.blockLast();
	}
	
	public void ejemploZipWithRangos() {
		Flux<Integer> rangos = Flux.range(0, 4);
		Flux.just(1,2,3,4)
		.map(e -> (e*2))
		.zipWith(rangos, (uno, dos) -> String.format("Primer Flux : %d / Segundo Flux: %d", uno, dos))
		.subscribe(texto -> log.info(texto));
	}
	
	public Usuario crearUsuario() {
		return new Usuario("Jhon", "Diaz");
	}
	
	public void ejemploUsuarioComentarioZipWithVersion2() {
		Mono<Usuario> monoUsuario = Mono.fromCallable(() -> new Usuario("Jhon", "Diaz"));
		Mono<Comentarios> monoComentario = Mono.fromCallable(()-> {
			Comentarios comentarios = new Comentarios();
			comentarios.addComentario("Hola que tal");
			comentarios.addComentario("Como estas?");
			comentarios.addComentario("A que hora vienes");
			return comentarios;
		});
		
		Mono<UsuarioComentarios> usuarioConComentarios = monoUsuario
				.zipWith(monoComentario)
				.map(tuple -> {
					Usuario u = tuple.getT1();
					Comentarios c = tuple.getT2();
					return new UsuarioComentarios(u,c);
				});
				
		usuarioConComentarios.subscribe(uc -> log.info(uc.toString()));
	}
	
	public void ejemploUsuarioComentarioZipWith() {
		Mono<Usuario> monoUsuario = Mono.fromCallable(() -> new Usuario("Jhon", "Diaz"));
		Mono<Comentarios> monoComentario = Mono.fromCallable(()-> {
			Comentarios comentarios = new Comentarios();
			comentarios.addComentario("Hola que tal");
			comentarios.addComentario("Como estas?");
			comentarios.addComentario("A que hora vienes");
			return comentarios;
		});
		
		Mono<UsuarioComentarios> usuarioConComentarios = monoUsuario.zipWith(monoComentario, (usuario, comentario) -> new UsuarioComentarios(usuario, comentario));
				usuarioConComentarios.subscribe(uc -> log.info(uc.toString()));
	}
	
	public void ejemploUsuarioComentarioFlatMap() {
		Mono<Usuario> monoUsuario = Mono.fromCallable(() -> new Usuario("Jhon", "Diaz"));
		Mono<Comentarios> monoComentario = Mono.fromCallable(()-> {
			Comentarios comentarios = new Comentarios();
			comentarios.addComentario("Hola que tal");
			comentarios.addComentario("Como estas?");
			comentarios.addComentario("A que hora vienes");
			return comentarios;
		});
		
		monoUsuario.flatMap(u -> monoComentario.map(c -> new UsuarioComentarios(u,c)))
		.subscribe(uc -> log.info(uc.toString()));
	}
	
public void ejemploCollectList() throws Exception {
		
		List<Usuario> usuariosList = new ArrayList();
		usuariosList.add(new Usuario("Andres", "Ramirez"));
		usuariosList.add(new Usuario("Pedro", "Gallo"));
		usuariosList.add(new Usuario("Alicia", "Manrique"));
		usuariosList.add(new Usuario("Juan", "Pazos"));
		usuariosList.add(new Usuario("Bruce", "Lee"));
		usuariosList.add(new Usuario("Bruce", "Willi"));
		usuariosList.add(new Usuario("Bruce", "Adams"));
		
		Flux.fromIterable(usuariosList)
				.collectList()
				.subscribe(lista -> {
					lista.forEach(item -> log.info(item.toString()));
				});
		
	}
	
public void ejemploToString() throws Exception {
		
		List<Usuario> usuariosList = new ArrayList();
		usuariosList.add(new Usuario("Andres", "Ramirez"));
		usuariosList.add(new Usuario("Pedro", "Gallo"));
		usuariosList.add(new Usuario("Alicia", "Manrique"));
		usuariosList.add(new Usuario("Juan", "Pazos"));
		usuariosList.add(new Usuario("Bruce", "Lee"));
		usuariosList.add(new Usuario("Bruce", "Willi"));
		usuariosList.add(new Usuario("Bruce", "Adams"));
		
		Flux.fromIterable(usuariosList)
				.map(usuario -> usuario.getNombre().toUpperCase().concat(" ").concat(usuario.getApellido().toUpperCase()))
				.flatMap(nombre -> {
					if(nombre.contains("bruce".toUpperCase())) {
						return Mono.just(nombre);
					}else {
						return Mono.empty();
					}
				})
				.map(nombre -> nombre.toLowerCase())
				.subscribe(u -> log.info(u.toString()));
		
	}
	
public void ejemploFlatMap() throws Exception {
		
		List<String> usuariosList = new ArrayList();
		usuariosList.add("Andres Ramirez");
		usuariosList.add("Pedro Gallo");
		usuariosList.add("Alicia Manrique");
		usuariosList.add("Juan Pazos");
		usuariosList.add("Bruce Lee");
		usuariosList.add("Bruce Willi");
		usuariosList.add("Bruce Adams");
		
		Flux<Usuario> list = Flux.fromIterable(usuariosList)
				.map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()));
		
		Flux.fromIterable(usuariosList)
				.map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
				.flatMap(usuario -> {
					if(usuario.getNombre().equalsIgnoreCase("bruce")) {
						return Mono.just(usuario);
					}else {
						return Mono.empty();
					}
				})
				.map(usuario -> {
					String nombre = usuario.getNombre().toLowerCase();
					usuario.setNombre(nombre);
					return usuario;
				})
				.subscribe(u -> log.info(u.toString()));
		
	}
	
	public void ejemploIterable() throws Exception {
		
		List<String> usuariosList = new ArrayList();
		usuariosList.add("Andres Ramirez");
		usuariosList.add("Pedro Gallo");
		usuariosList.add("Alicia Manrique");
		usuariosList.add("Juan Pazos");
		usuariosList.add("Bruce Lee");
		usuariosList.add("Bruce Willi");
		usuariosList.add("Bruce Adams");

		//Example of Flux.just
		//Flux<String> nombres = Flux.just("Andres Ramirez", "Pedro Gallo", "Alicia Manrique", "Juan Pazos", "Bruce Lee", "Bruce Willi");
		
		
		Flux<String> nombres = Flux.fromIterable(usuariosList);
		
		
		Flux<Usuario> usuarios = nombres.map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
				.filter(usuario -> usuario.getNombre().equalsIgnoreCase("bruce"))
				.doOnNext(usuario -> {
					if(usuario == null) {
						throw new RuntimeException("Nombres no puede ser vacio");
					}else {
						System.out.println(usuario.getNombre().concat(" ").concat(usuario.getApellido()));
					}
				})
				.map(usuario -> {
					String nombre = usuario.getNombre().toLowerCase();
					usuario.setNombre(nombre);
					return usuario;
				});
		
		usuarios.subscribe(e-> log.info(e.toString()), error -> log.error(error.getMessage()),
				new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						log.info("Ha finalizado la ejecucion del observable con exito!!!");
					}
		});
		
	}

}
