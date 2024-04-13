package server.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("api/connection")
public class ServerConnectionController {

    @Autowired
    private ServerProperties serverProperties;

    public ServerConnectionController(ServerProperties serverProperties)
    {
        this.serverProperties = serverProperties;
    }
    @GetMapping(path = {"", "/"})
    public ResponseEntity checkServer(){
        return ResponseEntity.ok().build();
    }

}
