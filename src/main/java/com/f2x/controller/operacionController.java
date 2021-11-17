package com.f2x.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/operacion")
public class operacionController {
	
	public String session[];
	public List<Map<String, Object>> sesiones = new ArrayList<Map<String, Object>>();
	public int contadorSesion = 0;
	public String cadenaOperacion = "";
	
	@GetMapping("iniciarSesion")
	public ResponseEntity<?> sessiones() {
		Map<String, Object> objeto = new HashMap<String, Object>();
		objeto.put("identificador", contadorSesion++);
		sesiones.add(objeto);
	    return new ResponseEntity<>(sesiones, HttpStatus.OK);
	}
	
	@GetMapping("agregarOperando/{sesion}/{numero}")
	public ResponseEntity<?> agregarOperando(@PathVariable int sesion, @PathVariable int numero) {
		sesiones.forEach(item -> {
			if ((int) item.get("identificador") == sesion) {
				List<String> lista = new ArrayList<String>();
				if (!item.containsKey("items")) {
					lista.add(String.valueOf(numero));
				} else {
					lista = (List<String>) item.get("items");
					lista.add(String.valueOf(numero));
				}
				item.put("items", lista);
			}
		});
	    return new ResponseEntity<>(sesiones, HttpStatus.OK);
	}
	
	@GetMapping("agregarSigno/{sesion}/{signo}")
	public ResponseEntity<?> agregarSigno(@PathVariable int sesion, @PathVariable String signo) {
		sesiones.forEach(item -> {
			if ((int) item.get("identificador") == sesion) {
				List<String> lista = new ArrayList<String>();
				if (!item.containsKey("items")) {
					lista.add(this.validateOperator(signo));
				} else {
					lista = (List<String>) item.get("items");
					lista.add(this.validateOperator(signo));
				}
				item.put("items", lista);
			}
		});
		return new ResponseEntity<>(sesiones, HttpStatus.OK);
	}
	
	@GetMapping("calcular/{sesion}")
	public ResponseEntity<?> calcular(@PathVariable int sesion) throws ScriptException {
		this.cadenaOperacion = "";
		sesiones.forEach(item -> {
			if ((int) item.get("identificador") == sesion) {
				List<String> lista = new ArrayList<String>();
				if (item.containsKey("items")) {
					lista = (List<String>) item.get("items");
					lista.forEach(itemOperators -> {
						this.cadenaOperacion = cadenaOperacion + itemOperators;
					});
				}
			}
		});
		
		
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	private String validateOperator(String operator) {
		switch (operator) {
		case "suma":
			return "+";
		case "resta":
			return "-";
		case "multiplicacion":
			return "*";
		case "division":
			return "/";
		case "potenciacion":
			return "^";	
		default:
			return "Error";
		}
	}

}
