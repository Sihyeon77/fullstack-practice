package io.getarrays.server.service.implementation;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Random;

import javax.transaction.Transactional;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.getarrays.server.enumeration.Status;
import io.getarrays.server.model.Server;
import io.getarrays.server.repo.ServerRepository;
import io.getarrays.server.service.ServerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class ServerServiecImpl implements ServerService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * io.getarrays.server.service.ServerService#create(io.getarrays.server.model.
     * Server)
     */
    private final ServerRepository serverRepository;

    @Override
    public Server create(Server server) {
        // TODO Auto-generated method stub
        log.info("Saving new server: {}", server.getName());
        server.setImageUrl(setServerImageUrl());
        return serverRepository.save(server);
    }

    @Override
    public Server ping(String ipAddress) throws IOException {
        // TODO Auto-generated method stub
        log.info("Pinging server IP: {}", ipAddress);
        Server server = serverRepository.findByIpAddress(ipAddress);
        InetAddress address = InetAddress.getByName(ipAddress);
        server.setStatus(address.isReachable(10000) ? Status.SERVER_UP : Status.SERVER_DOWN);
        serverRepository.save(server);
        return server;
    }

    @Override
    public Collection<Server> list(int limit) {
        // TODO Auto-generated method stub
        log.info("Fetching all servers");
        return serverRepository.findAll(PageRequest.of(0, limit)).toList();
    }

    @Override
    public Server get(Long id) {
        // TODO Auto-generated method stub
        log.info("Fetching server by id: {}", id);
        return serverRepository.findById(id).get();
    }

    @Override
    public Server update(Server server) {
        // TODO Auto-generated method stub
        log.info("Updating new server: {}", server.getName());
        return serverRepository.save(server);
    }

    @Override
    public Boolean delete(Long id) {
        // TODO Auto-generated method stub
        log.info("Deleting server by ID: {}", id);
        serverRepository.deleteById(id);
        return true;
    }

    private String setServerImageUrl() {
        String[] imageNames = {
                "....../resouces/images/server.png",
                "....../resources/images/server-icon-vector-sign-symbol-isolated-white-background-server-icon-vector-isolated-white-background-your-web-133761922.jpg"
        };
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/server/image" + imageNames[new Random().nextInt(2)]).toUriString();
    }

}
