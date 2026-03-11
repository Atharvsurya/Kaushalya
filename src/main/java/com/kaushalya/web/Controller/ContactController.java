package com.kaushalya.web.controller;

import com.kaushalya.web.dto.ApiResponse;
import com.kaushalya.web.dto.ContactRequest;
import com.kaushalya.web.dto.ContactResponse;
import com.kaushalya.web.entity.ContactMessage;
import com.kaushalya.web.repository.ContactMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Contact endpoints:
 *
 *  POST   /api/contact           → contact.html submit (public)
 *  GET    /api/contact           → admin.html — view all messages
 *  PATCH  /api/contact/{id}/read → admin marks message as READ
 *  DELETE /api/contact/{id}      → admin deletes message
 */
@RestController
@RequestMapping("/api/contact")
@CrossOrigin(origins = "*")
public class ContactController {

    @Autowired
    private ContactMessageRepository repo;

    /** contact.html — public submit */
    @PostMapping
    public ResponseEntity<ApiResponse<ContactResponse>> submit(@RequestBody ContactRequest req) {
        if (req.getName() == null || req.getName().isBlank())
            return ResponseEntity.badRequest().body(ApiResponse.fail("Name is required."));
        if (req.getEmail() == null || req.getEmail().isBlank())
            return ResponseEntity.badRequest().body(ApiResponse.fail("Email is required."));
        if (req.getSubject() == null || req.getSubject().isBlank())
            return ResponseEntity.badRequest().body(ApiResponse.fail("Subject is required."));
        if (req.getMessage() == null || req.getMessage().isBlank())
            return ResponseEntity.badRequest().body(ApiResponse.fail("Message is required."));

        ContactMessage msg = new ContactMessage();
        msg.setName(req.getName().trim());
        msg.setEmail(req.getEmail().toLowerCase().trim());
        msg.setPhone(req.getPhone() != null ? req.getPhone().trim() : null);
        msg.setSubject(req.getSubject().trim());
        msg.setMessage(req.getMessage().trim());
        msg.setStatus("PENDING");

        ContactMessage saved = repo.save(msg);
        return ResponseEntity.ok(ApiResponse.ok("Message sent successfully!", toResponse(saved)));
    }

    /** admin.html — list all contact messages (newest first) */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ContactResponse>>> getAll() {
        List<ContactResponse> list = repo.findAllByOrderByCreatedAtDesc()
                .stream().map(this::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.ok("Messages fetched.", list));
    }

    /** admin.html — mark a message as READ */
    @PatchMapping("/{id}/read")
    public ResponseEntity<ApiResponse<ContactResponse>> markRead(@PathVariable Long id) {
        Optional<ContactMessage> opt = repo.findById(id);
        if (opt.isEmpty())
            return ResponseEntity.status(404).body(ApiResponse.fail("Message not found."));
        ContactMessage msg = opt.get();
        msg.setStatus("READ");
        repo.save(msg);
        return ResponseEntity.ok(ApiResponse.ok("Marked as read.", toResponse(msg)));
    }

    /** admin.html — delete a message */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        if (!repo.existsById(id))
            return ResponseEntity.status(404).body(ApiResponse.fail("Message not found."));
        repo.deleteById(id);
        return ResponseEntity.ok(ApiResponse.ok("Message deleted.", null));
    }

    private ContactResponse toResponse(ContactMessage m) {
        return new ContactResponse(
            m.getId(), m.getName(), m.getEmail(), m.getPhone(),
            m.getSubject(), m.getMessage(), m.getStatus(), m.getCreatedAt()
        );
    }
}
