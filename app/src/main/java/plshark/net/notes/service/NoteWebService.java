package plshark.net.notes.service;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import plshark.net.notes.Note;

public class NoteWebService {

    // TODO put in settings
    private static final String URL = "http://notes.plshark.net/api/notes";

    public Note getNote(long id) {
        return null;
    }

    public Note createNote(Note note) {
        return null;
    }

    public void updateNote(Note note) {
        Client client = ClientBuilder.newClient();
        client.register(HttpAuthenticationFeature.basic("alex", "password"));
        WebTarget target = client.target(URL).path(note.getId().toString());

        Response response = target.request(MediaType.APPLICATION_JSON_TYPE).put(Entity.entity(note,
                MediaType.APPLICATION_JSON_TYPE));
    }

    public void deleteNote(long id) {

    }
}
