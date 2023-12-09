package controllers;

import java.util.List;

import com.google.gson.Gson;

import core.CronogramaGenetico;
import core.Tarefa;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

class Payload {
    public List<Tarefa> tasks;
    public Number mutation;
    public Number crossover;
}

@Path("/")
public class HomeController {
    @CheckedTemplate(requireTypeSafeExpressions = false)
    public static class Templates {
        public static native TemplateInstance index(String name);
        public static native TemplateInstance result(List<Tarefa> tarefas, List<List<Tarefa>> geracoes);
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
        Type listType = new TypeToken<Payload>() {}.getType();
        Payload payload = gson.fromJson(tasks, listType);
        
        CronogramaGenetico cronogramaGenetico = new CronogramaGenetico(payload.tasks);
        CronogramaGenetico.TAXA_MUTACAO = payload.mutation.doubleValue();
        CronogramaGenetico.TAXA_CRUZAMENTO = payload.crossover.doubleValue();
        cronogramaGenetico.run();
        
        return Templates.result(cronogramaGenetico.melhoresTarefas, cronogramaGenetico.geracoes);
    }
}
