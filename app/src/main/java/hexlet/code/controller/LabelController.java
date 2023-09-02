package hexlet.code.controller;

import hexlet.code.dto.LabelDTO;
import hexlet.code.model.Label;
import hexlet.code.reporsitory.LabelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${base-url}" + LabelController.LABEL_CONTROLLER_PATH)
public class LabelController {
    public static final String LABEL_CONTROLLER_PATH = "/labels";
    public static final String ID = "/{id}";
    private final LabelRepository labelRepository;

    @PostMapping
    public Label createLabel(@RequestBody LabelDTO labelDTO) {
        Label label = new Label(labelDTO.getName());
        return labelRepository.save(label);
    }

    @PutMapping(ID)
    public Label updateLabel(@PathVariable Long id, @RequestBody LabelDTO labelDTO) {
        Label label = labelRepository.findById(id).orElseThrow();
        label.setName(labelDTO.getName());
        return labelRepository.save(label);
    }

    @DeleteMapping(ID)
    public String deleteLabel(@PathVariable Long id) {
        labelRepository.deleteById(id);
        return "success";
    }

    @GetMapping(ID)
    public Label getLabelById(@PathVariable Long id) {
        return labelRepository.findById(id).orElseThrow();
    }

    @GetMapping
    public List<Label> getLabel() {
        return labelRepository.findAll();
    }
}
