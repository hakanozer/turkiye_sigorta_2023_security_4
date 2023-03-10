package com.works.configs;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

@RestControllerAdvice
public class GlobalAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity methodArgumentNotValid( MethodArgumentNotValidException ex ) {
        Map map = new LinkedHashMap();
        List<FieldError> ls = ex.getFieldErrors();
        map.put("status", false);
        map.put("errors", parseError(ls));
        return new ResponseEntity(map, HttpStatus.BAD_REQUEST);
    }

    private List parseError(List<FieldError> ls) {
        List list = new ArrayList();
        for( FieldError item : ls ) {
            Map<String, String> hm = new HashMap<>();
            hm.put("field", item.getField());
            hm.put("message", item.getDefaultMessage());
            list.add(hm);
        }
        return list;
    }


}
