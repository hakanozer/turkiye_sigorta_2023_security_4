package com.works.restcontrollers;

import com.works.entities.Note;
import com.works.entities.Product;
import com.works.services.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/note")
public class NoteRestController {

    final NoteService service;

    @PostMapping("/save")
    public Note save(@Valid @RequestBody Note note ) {
        return service.save(note);
    }

    @GetMapping("/list")
    public List<Note> list() {
        return service.list();
    }


}
