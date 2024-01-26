package com.lms.bcs.Controller;

import com.lms.bcs.Dto.CopyDTO;
import com.lms.bcs.Entity.Copy;
import com.lms.bcs.Service.CopyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/copies")
public class CopyController {
    @Autowired
    private CopyService copyService;

    @PostMapping
    public Copy createCopy(@RequestBody CopyDTO copy) {
        return copyService.saveCopy(copy);
    }

    @GetMapping
    public List<Copy> getAllCopies() {
        return copyService.findAllCopies();
    }

    @GetMapping("/{id}")
    public Copy getCopyById(@PathVariable Long id) {
        return copyService.findCopyById(id);
    }

}
