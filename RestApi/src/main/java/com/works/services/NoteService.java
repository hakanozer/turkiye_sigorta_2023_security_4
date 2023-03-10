package com.works.services;

import com.works.entities.Note;
import com.works.entities.Product;
import com.works.repositories.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService {

    final NoteRepository repository;

    public Note save(Note note ) {
        repository.save(note);
        return note;
    }

    public List<Note> list() {
        return repository.findAll();
    }

}
