package server.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/connection")
public class ServerConnectionController {

    @Autowired
    private ServerProperties serverProperties;

    /**
     * Creates a new ServerConnectionController
     * @param serverProperties the ServerProperties
     */
    public ServerConnectionController(ServerProperties serverProperties)
    {
        this.serverProperties = serverProperties;
    }

    /**
     * Checks the server connection
     */
    @GetMapping(path = {"", "/"})
    public ResponseEntity checkServer(){
        return ResponseEntity.ok().build();
    }
}
