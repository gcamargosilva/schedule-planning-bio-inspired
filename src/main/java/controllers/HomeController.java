package controllers;

import java.util.List;

import com.google.gson.Gson;

import core.CronogramaGenetico;
import core.Tarefa;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

@Path("/home")
public class HomeController {
    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance index(String name);
        public static native TemplateInstance result(List<Tarefa> tarefas);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public  TemplateInstance index() {
        return Templates.index("Gustavo");
    }
    

    @Path("/result")
    @POST
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance result(@FormParam("tasks") String tasks) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Tarefa>>() {}.getType();
        List<Tarefa> tarefas = gson.fromJson(tasks, listType);
        CronogramaGenetico cronogramaGenetico = new CronogramaGenetico(tarefas);
        cronogramaGenetico.run();
        return Templates.result(cronogramaGenetico.melhoresTarefas);
    }
}
