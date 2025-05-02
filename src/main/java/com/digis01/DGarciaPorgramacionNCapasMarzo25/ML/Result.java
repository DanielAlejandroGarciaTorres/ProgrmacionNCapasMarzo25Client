package com.digis01.DGarciaPorgramacionNCapasMarzo25.ML;

import java.util.List;

public class Result<T> {
    
    public boolean correct; // cuando se ejecuta ok o no ok 
    public String errorMessage; // descripción del error del mensaje
    public Exception ex; // excepciones
    public T object; // int, string, alumno, lista<Alumno>, ARREGLO, MATRIZ 
    public List<T> objects;
    
}
