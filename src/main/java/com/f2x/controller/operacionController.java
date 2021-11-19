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
	public int validacion = 0;
	
	@GetMapping("iniciarSesion")
	public ResponseEntity<?> sessiones() {
		Map<String, Object> objeto = new HashMap<String, Object>();
		objeto.put("identificador", contadorSesion++);
		sesiones.add(objeto);
	    return new ResponseEntity<>(sesiones, HttpStatus.OK);
	}
	
	@GetMapping("agregarOperando/{sesion}/{numero}")
	public ResponseEntity<?> agregarOperando(@PathVariable int sesion, @PathVariable int numero) {
		
		try {
			
			if(validacion==0) {
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
				this.validacion = 1;
			}
			else {
				return new ResponseEntity<>("Error, debe agregar un signo ", HttpStatus.CONFLICT);
			}
		   
			
		} catch (Exception e) {
			return new ResponseEntity<>("Error, debe ingresar un numero ", HttpStatus.CONFLICT);
            
        }
		 return new ResponseEntity<>(sesiones, HttpStatus.OK);
		
		
	}
	
	@GetMapping("agregarSigno/{sesion}/{signo}")
	public ResponseEntity<?> agregarSigno(@PathVariable int sesion, @PathVariable String signo) {
		
		if(this.validacion==1) {
			if(this.validateOperator(signo).equals("Error")) {
				
				return new ResponseEntity<>("Error, el signo "+signo+" no existe", HttpStatus.CONFLICT);
			}
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
			
			this.validacion=0;
		}
		else {
			return new ResponseEntity<>("Error, debe agregar primero numeros", HttpStatus.OK);
		}
		
		
		return new ResponseEntity<>(sesiones, HttpStatus.OK);
	}
	
	@GetMapping("calcular/{sesion}")
	public ResponseEntity<?> calcular(@PathVariable int sesion) throws ScriptException {
		
		final char SUMA='+';
	    final char RESTA='-';
	    final char MULTIPLICACION='*';
	    boolean firstSign=true;
	    char lastOp='+';
	    int operacion=0;        
	    String numero = "";
		
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
		


	    for (int x = 0; x < cadenaOperacion.length(); x++) { // numero de caracteres de la cadena


	        if (cadenaOperacion.charAt(x) == SUMA || cadenaOperacion.charAt(x) == RESTA) {
	            if(firstSign)//Solo lo usamos para la primera operacion
	            {
	                operacion = Integer.parseInt(numero);
	                firstSign=false;        
	            }
	            else
	            {
	                if(lastOp == SUMA)
	                {
	                    //Suma
	                    operacion = operacion + Integer.parseInt(numero);

	                }
	                else if(lastOp == MULTIPLICACION)
	        	    {
	        	        //multiplicacion
	        	        operacion = operacion + Integer.parseInt(numero);

	        	    }
	                else
	                {
	                    //Resta
	                    operacion = operacion - Integer.parseInt(numero);

	                }
	            }
	            lastOp=cadenaOperacion.charAt(x);//Almacenamos la ultima operacion

	            numero = "";
	        } else {
	            numero = numero + Character.toString(cadenaOperacion.charAt(x));
	        }
	    }


	    // sumamos/restamos el ultimo digito
	    if(lastOp == SUMA)
	    {
	        //Suma
	        operacion = operacion + Integer.parseInt(numero);

	    }
	    else if(lastOp == MULTIPLICACION)
	    {
	        //multiplicacion
	        operacion = operacion + Integer.parseInt(numero);

	    }
	    else
	    {
	        //Resta
	        operacion = operacion - Integer.parseInt(numero);

	    }
	    
	   
	    
		return new ResponseEntity<>(operacion, HttpStatus.OK);
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
