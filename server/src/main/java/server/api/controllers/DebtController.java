package server.api.controllers;

import commons.Debt;
import commons.Quote;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.DebtRepository;
import server.database.EventRepository;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class DebtController {
    private final DebtRepository repo;
    private final EventRepository eventRepo;

    /**
     * @param repo      event rep
     * @param eventRepo
     */
    public DebtController(DebtRepository repo, EventRepository eventRepo) {
        this.repo = repo;
        this.eventRepo = eventRepo;
    }

    /**
     *
     * @return all debts asociated to the event
     */
    @GetMapping("/{id}/debts")
    public List<Debt> getDebts(@PathVariable("id") Long id){
        return repo.getDebtsByEvent(eventRepo.findById(id).get());
    }

    /**
     *
     * @param debt
     * @return the saved debt
     */
    @PostMapping("{id}/debts")
    public ResponseEntity<Debt> add(@RequestBody Debt debt){
        if(debt==null || debt.getDebtFor()==null){
            return ResponseEntity.badRequest().build();
        }
        Debt saved = repo.save(debt);
        return ResponseEntity.ok(saved);
    }
}
