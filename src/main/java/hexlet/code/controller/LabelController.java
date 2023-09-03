package hexlet.code.controller;

import hexlet.code.dto.LabelDTO;
import hexlet.code.model.Label;
import hexlet.code.reporsitory.LabelRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "spring_app")
@RequestMapping("${base-url}" + LabelController.LABEL_CONTROLLER_PATH)
public class LabelController {
    public static final String LABEL_CONTROLLER_PATH = "/labels";
    public static final String ID = "/{id}";
    private final LabelRepository labelRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create label")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @io.swagger.v3.oas.annotations.media.Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @io.swagger.v3.oas.annotations.media.Content),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @io.swagger.v3.oas.annotations.media.Content)
    })
    public Label createLabel(@Parameter(description = "Label") @RequestBody LabelDTO labelDTO) {
        Label label = new Label(labelDTO.getName());
        return labelRepository.save(label);
    }

    @PutMapping(ID)
    @Operation(summary = "Update label")
    public Label updateLabel(@PathVariable Long id, @RequestBody LabelDTO labelDTO) {
        Label label = labelRepository.findById(id).orElseThrow();
        label.setName(labelDTO.getName());
        return labelRepository.save(label);
    }

    @DeleteMapping(ID)
    @Operation(summary = "Delete label")
    public String deleteLabel(@PathVariable Long id) {
        labelRepository.deleteById(id);
        return "success";
    }

    @GetMapping(ID)
    @Operation(summary = "Get label by id")
    public Label getLabelById(@PathVariable Long id) {
        return labelRepository.findById(id).orElseThrow();
    }

    @GetMapping
    @Operation(summary = "Get all labels")
    public List<Label> getLabel() {
        return labelRepository.findAll();
    }
}
