package io.github.dead_i.bungeehttpconsole;

import com.google.gson.Gson;
import net.md_5.bungee.api.plugin.Plugin;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class WebHandler extends AbstractHandler {
    private Plugin plugin;
    private Gson gson = new Gson();

    public WebHandler(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void handle(String target, Request baseReq, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        res.setStatus(HttpServletResponse.SC_OK);
        res.setCharacterEncoding("utf8");
        baseReq.setHandled(true);

        if (target.equalsIgnoreCase("/console")) {
            String apikey = req.getParameter("apikey");
            String cmd = req.getParameter("cmd");
            HashMap<String, Object> out = new HashMap<String, Object>();

            if (apikey == null || cmd == null) {
                out.put("error", "Missing parameters. Required parameters: apikey, cmd");
            }else{
                if (BungeeHTTPConsole.getConfig().getList("apikeys").contains(apikey)) {
                    out.put("result", plugin.getProxy().getPluginManager().dispatchCommand(plugin.getProxy().getConsole(), cmd));
                }else{
                    out.put("error", "The API key you provided is invalid.");
                }
            }

            res.getWriter().print(gson.toJson(out));
        }else{
            res.getWriter().print("BungeeHTTPConsole");
        }
    }
}
